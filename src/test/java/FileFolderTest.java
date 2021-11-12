
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import composite2.Component;
import composite2.File;
import composite2.Folder;

public class FileFolderTest {
	@Nested
	class NonFunctional {
		@Test
		void testFieldsPrivateAndNotStaticComponentAbstract() {
			Consumer<Class<?>> foo = c -> { 
				Field[]  fields = c.getDeclaredFields();

				for (Field f : fields) {
					if (!f.isSynthetic()) {
						assertTrue ( Modifier.isPrivate( f.getModifiers() ), () -> "Field \""+f.getName()+"\" should be private" );
						assertFalse( Modifier.isStatic ( f.getModifiers() ), () -> "Field \""+f.getName()+"\" can't be static"   );
					}
				}
			};
			foo.accept( Component.class );
			foo.accept(      File.class );
			foo.accept(    Folder.class );
			// Component is abstract
			Class<?> clazz = Component.class;
			int      mod   = clazz.getModifiers();
			assertTrue( Modifier.isAbstract( mod ));
		}
	}
	@Nested
	class Exceptions {
		@Test
		void testNullName() {
			Throwable e = assertThrows( IllegalArgumentException.class, () -> new File( null, 670439 ));
			assertEquals( "name cannot be null", e.getMessage() );
		}
		@Test
		void testEmptyName() {
			StringBuilder s = new StringBuilder();
			for (int i = 0; i < 10; i++) {
				Throwable e = assertThrows( IllegalArgumentException.class, () -> new File( s.toString(), 670439 ));
				assertEquals( "name cannot be blank", e.getMessage() );
				s.append(" ");
			}
		}
		@Test
		void testNegativeSize() {
			AtomicInteger s = new AtomicInteger(0);
			for (int i = 0; i < 10; i++) {
				Throwable e = assertThrows( IllegalArgumentException.class, () -> new File( "doc.pdf", s.decrementAndGet() ));
				assertEquals( "size cannot be negative", e.getMessage() );
			}
		}
	}
	@Nested
	class NameAndSize {
		@Test
		void testFile() {
			Component f = new File( "doc.pdf", 670439 ); 
			assertEquals( "doc.pdf", f.getName() );
			assertEquals( 670439,    f.getSize() );
		}
		@Test
		void testFolderEmpty() {
			Component f = new Folder( "bin" ); 
			assertEquals( "bin", f.getName() );
			assertEquals( 0,     f.getSize() );
		}
		@Test
		void testFolderWithFiles() {
			Component f = 
				new Folder(".Trash").add(
					new File(  "doc.pdf",670439), 
					new File(  "doc.zip", 49530), 
					new File("email.zip",  4400), 
					new File( "exam.js",     70));
			assertEquals( ".Trash", f.getName() );
			assertEquals( 724439,   f.getSize() );
		}
		@Test
		void testFolderWithFilesAndFolders() {
			Component f = 
					new Folder("home").add(
							new File  (".bash_profile", 448 ),
							new File  (".profile",      463 ),
							new Folder(".ssh").add(
								new File("id_rsa",      887), 
								new File("id_rsa.pub",  222), 
								new File("known_hosts",2239)),
							new Folder("Downloads").add(
								new File("UK Advanced Cryptics Dictionary.txt",  2694650), 
								new File("Workbook3.xlsx",                         67539),
								new File("zt_java8_streams_cheat_sheet.pdf",      417003)));
			assertEquals( "home",  f.getName() );
			assertEquals( 3183451, f.getSize() );
		}
	}
}