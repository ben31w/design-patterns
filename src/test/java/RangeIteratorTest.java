import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import iterator2.RangeIterator;

class RangeIteratorTest {
	private static final String   CLASSNAME  = "RangeIterator"; 
	private static final String[] INTERFACES = { "java.util.Iterator<java.lang.Integer>" };

	private Class<?> getClass(String className) {
		Class<?> result = null;
		try {
			Package pkg  = getClass().getPackage();
			String  path = (pkg == null || pkg.getName().isEmpty()) ? "" : pkg.getName()+".";
			result = Class.forName( path + className );
		} catch (ClassNotFoundException e) {
			fail( String.format("class '%s' not found", className ));
		}
		return result;
	}

	@Test
	void testFieldsArePrivateNonStaticNotCollections() {
		Class<?> clazz = getClass( CLASSNAME );
		if (clazz != null) {
			// superclass
			Class<?> iParent = clazz.getSuperclass();
			assertEquals( Object.class.getName(), iParent.getName(), "Class should subclass from Object" );
			// interfaces
			List<String> expected = new ArrayList<>( Arrays.asList( INTERFACES ));
			for (Type i : clazz.getGenericInterfaces()) {
				String actual = i.toString();
				if (expected.contains( actual )) {
					expected.remove  ( actual );
				} else {
					fail( "Unexpected interface found: " + actual );
				}
			}
			if (!expected.isEmpty()) {
				fail( "Interfaces(s) not implemented:" + expected );
			}
			// fields
			Field[] iFields = clazz.getDeclaredFields();
			for (Field f : iFields) {
				if (!f.isSynthetic()) {
					assertTrue ( Modifier.isPrivate(f.getModifiers()), ()->String.format( "Field '%s' should be private", f.getName()));
					assertFalse( Modifier.isStatic (f.getModifiers()), ()->String.format( "Field '%s' can't be static",   f.getName()));
					
					assertFalse( Collection .class.isAssignableFrom( f.getType() ), () -> String.format( "Field '%s' can't be a Java Collection", f.getName() ));
					assertFalse( Dictionary .class.isAssignableFrom( f.getType() ), () -> String.format( "Field '%s' can't be a Java Collection", f.getName() ));
					assertFalse( AbstractMap.class.isAssignableFrom( f.getType() ), () -> String.format( "Field '%s' can't be a Java Collection", f.getName() ));
					assertFalse( f.getType().isArray(),                             () -> String.format( "Field '%s' can't be a Java array",      f.getName() ));
				}
			}
		}
	}

	@Test
	void testNewIteratorExceptionInvalidRange() {
		{
			int[] arrayFrom = { -1,  0, 1,  42, Integer.MAX_VALUE };
			int[] arrayTo   = { -2, -1, 0, -42, Integer.MIN_VALUE };
			for (int i = 0; i < arrayTo.length; i++) {
				int from = arrayFrom[ i ];
				int to   = arrayTo  [ i ];
				Throwable t = assertThrows( IllegalArgumentException.class, ()->new RangeIterator( from, to, true ));
				assertEquals( String.format( "%d > %d", from, to ), t.getMessage() );
			}
		}{
			int[] arrayFrom = { -2, -1, 0, -42, Integer.MIN_VALUE };
			int[] arrayTo   = { -1,  0, 1,  42, Integer.MAX_VALUE };
			for (int i = 0; i < arrayTo.length; i++) {
				int from = arrayFrom[ i ];
				int to   = arrayTo  [ i ];
				Throwable t = assertThrows( IllegalArgumentException.class, ()->new RangeIterator( from, to, false ));
				assertEquals( String.format( "%d < %d", from, to ), t.getMessage() );
			}
		}
	}

	@Test
	void testIteratorExceptionNextAfterEnd() {
		int[] arrayFrom = { -1, 0, 1, 42, Integer.MIN_VALUE/10, Integer.MAX_VALUE/10 };
		int[] arrayTo   = { -1, 0, 1, 42, Integer.MIN_VALUE/10, Integer.MAX_VALUE/10 };
		{
			for (int i = 0; i < arrayTo.length; i++) {
				int         from = arrayFrom[ i ];
				int         to   = arrayTo  [ i ];
				Iterator<?> iter = new RangeIterator( from, to, true );
				assertTrue  (       iter.hasNext() );
				assertEquals( from, iter.next() );
				assertFalse (       iter.hasNext() );
				Throwable t = assertThrows( NoSuchElementException.class, ()->iter.next());
				assertEquals( String.format( "end of iterations reached", from, to ), t.getMessage() );
			}
		}{
			for (int i = 0; i < arrayTo.length; i++) {
				int         from = arrayFrom[ i ];
				int         to   = arrayTo  [ i ];
				Iterator<?> iter = new RangeIterator( from, to, false );
				assertTrue  (       iter.hasNext() );
				assertEquals( from, iter.next() );
				assertFalse (       iter.hasNext() );
				Throwable t = assertThrows( NoSuchElementException.class, ()->iter.next());
				assertEquals( String.format( "end of iterations reached", from, to ), t.getMessage() );
			}
		}
	}

	@ParameterizedTest
	@MethodSource("dataIteratorUp")
	void testIteratorUp(int from, int to ) {
		Iterator<?> iter = new RangeIterator( from, to, true );
		for (int i = from; i <= to; i++) {
			assertTrue  (    iter.hasNext() );
			assertEquals( i, iter.next() );
		}
		assertFalse( iter.hasNext() );
	}
	static Stream<Arguments> dataIteratorUp() {
	    return Stream.of(
	    	Arguments.of(                   -15,                    -7 ),
	    	Arguments.of(                  2001,                  5002 ),
	    	Arguments.of( Integer.MIN_VALUE/100, Integer.MAX_VALUE/100 )
	    );
	}

	@ParameterizedTest
	@MethodSource("dataIteratorDown")
	void testIteratorDown(int from, int to ) {
		Iterator<?> iter = new RangeIterator( from, to, false );
		for (int i = from; i >= to; i--) {
			assertTrue  (    iter.hasNext() );
			assertEquals( i, iter.next() );
		}
		assertFalse( iter.hasNext() );
	}
	static Stream<Arguments> dataIteratorDown() {
	    return Stream.of(
	    	Arguments.of(                    -7,                   -15 ),
	    	Arguments.of(                  5002,                  2005 ),
	    	Arguments.of( Integer.MAX_VALUE/100, Integer.MIN_VALUE/100 )
	    );
	}
}