import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import builder.Coffee;

class CoffeeTest {
	private static final Class<?> CLASS   = Coffee.class;
	private static final Class<?> BUILDER = Coffee.Builder.class;
	
	@Test
	void testAllFieldsPrivateNonStatic() {
		Field[]  iFields = CLASS.getDeclaredFields();

		for (Field f : iFields) {
			if (!f.isSynthetic()) {
				assertTrue ( Modifier.isPrivate( f.getModifiers() ), () -> "Field \""+f.getName()+"\" should be private" );
				assertFalse( Modifier.isStatic ( f.getModifiers() ), () -> "Field \""+f.getName()+"\" can't be static" );
			}
		}
	}
	@Test
	void testClassInterface() {
		Class<?> clazz = CLASS;

		Constructor<?>[] con = clazz.getDeclaredConstructors();
		assertEquals( 1, con.length, "unexpected number of constructors" );
		assertTrue  ( Modifier.isPrivate( con[0].getModifiers() ), "Constructor is not private" );

		List<String> expected = new ArrayList<>( Arrays.asList( "getBrand","getExpiration","toString"));
		for (Method a : clazz.getDeclaredMethods()) {
			String name = a.getName();
			if (!name.contains("$")) {
				assertTrue( expected.remove(name), ()->String.format( "method '%s' not found in %s", name, expected ));
				assertTrue( Modifier.isPublic( a.getModifiers() ), ()->String.format( "method '%s' not public", name ));
			}
		}
	}
	@Test
	void testBuilderInterface() {
		Class<?> clazz = BUILDER;

		assertTrue  ( Modifier.isPublic( clazz.getModifiers() ), "Class is not public" );
		assertTrue  ( Modifier.isStatic( clazz.getModifiers() ), "Class is not static" );

		Constructor<?>[] con = clazz.getDeclaredConstructors();
		assertEquals( 1, con.length, "unexpected number of constructors" );
		assertTrue  ( Modifier.isPublic( con[0].getModifiers() ), "Constructor is not public");

		List<String> expected = new ArrayList<>( Arrays.asList( "build","brand","expiration","isValid"));
		for (Method a : clazz.getDeclaredMethods()) {
			String name = a.getName();
			if (!name.contains("$")) {
				assertTrue( expected.contains(name), ()->String.format( "method '%s' not found in %s", name, expected ));
				assertTrue( Modifier.isPublic( a.getModifiers() ), ()->String.format( "method '%s' not public", name ));
				expected.remove(name);
			}
		}
		if (!expected.isEmpty()) {
			fail( String.format( "Unexpected additional methods %s", expected ));
		}
	}
	@Test
	void testNewCoffee() {
		Coffee.Builder a = new Coffee.Builder();
		assertFalse (  a.isValid(), "brand and expiration missing" );
		Coffee.Builder b = a.brand( "Illy Espresso" );
		assertTrue  (  a == b, "builders should be the same" );
		assertFalse (  a.isValid(), "expiration missing" );
		Coffee.Builder c = a.expiration( 2025 );
		assertTrue  (  a == c, "builders should be the same" );
		assertTrue  (  a.isValid(), "nothing missing" );
		Coffee         p = c.build();
		assertEquals( "Illy Espresso", p.getBrand() );
		assertEquals( 2025,            p.getExpiration() );
		assertEquals( "Coffee [brand=Illy Espresso,year=2025]", p.toString() );
	}
	@Test
	void testTwoConcurrentBuilders() {
		Coffee.Builder one = new Coffee.Builder();
		Coffee.Builder two = new Coffee.Builder();
		assertFalse (  one.isValid(), "brand and expiration missing" );
		assertFalse (  two.isValid(), "brand and expiration missing" );
		{   // populating one
			Coffee.Builder b = one.brand( "Illy Espresso" );
			assertTrue  (  one == b, "builders should be the same" );
			assertFalse (  one.isValid(), "expiration missing" );
			Coffee.Builder c = one.expiration( 2025 );
			assertTrue  (  one == c, "builders should be the same" );
			assertTrue  (  one.isValid(), "nothing missing" );
		}{  // populating two
			Coffee.Builder b = two.brand( "Pilon" );
			assertTrue  (  two == b, "builders should be the same" );
			assertFalse (  two.isValid(), "expiration missing" );
			Coffee.Builder c = two.expiration( 2028 );
			assertTrue  (  two == c, "builders should be the same" );
			assertTrue  (  two.isValid(), "nothing missing" );
		}
		// building foo and bar
		Coffee         foo = one.build();
		Coffee         bar = two.build();
		
		assertEquals( "Illy Espresso", foo.getBrand() );
		assertEquals( 2025,            foo.getExpiration() );
		assertEquals( "Coffee [brand=Illy Espresso,year=2025]", foo.toString() );

		assertEquals( "Pilon", bar.getBrand() );
		assertEquals( 2028,    bar.getExpiration() );
		assertEquals( "Coffee [brand=Pilon,year=2028]", bar.toString() );
	}
	@Test
	void testExceptionThrownWithIncompleteData() {
		{ 
			Throwable exception = assertThrows( IllegalStateException.class, () -> {
				new Coffee.Builder().build();
			});
			assertEquals("missing data [brand,year]", exception.getMessage());
		}{
			Throwable exception = assertThrows( IllegalStateException.class, () -> {
				new Coffee.Builder().brand("Dunkin").build();
			});
			assertEquals("missing data [year]", exception.getMessage());
		}{
			Throwable exception = assertThrows( IllegalStateException.class, () -> {
				new Coffee.Builder().expiration(2042).build();
			});
			assertEquals("missing data [brand]", exception.getMessage());
		}{
			for (String brand : new String[]{ null, new String("") }) {
				Throwable exception = assertThrows( IllegalArgumentException.class, 
						() -> new Coffee.Builder().brand( brand ));
				assertEquals("brand cannot be null or empty", exception.getMessage());
			}
		}{
			for (int year : new int[]{ Integer.MIN_VALUE, 0, 2023 }) {
				Throwable exception = assertThrows( IllegalArgumentException.class, () ->
				new Coffee.Builder().expiration( year ));
				assertEquals("expiration cannot be before 2024", exception.getMessage());
			}
		}
	}
}