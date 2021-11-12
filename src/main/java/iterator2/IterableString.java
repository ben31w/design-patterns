package iterator2;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A string that can return an iterator to loop through each of its characters. 
 * IterableString can be used inside for each loops.
 * 
 * @author ben31w
 *
 */
public class IterableString implements Iterable<Character> {
	private String data; // the string to iterate through
	
	public IterableString(String data) {
		this.data = data;
	}
	
	public String getString() {
		return data;
	}
	
	/**
	 * Return an iterator that can iterate through the characters of this 
	 * IterableString.
	 */
	@Override
	public Iterator<Character> iterator() {
		return new Iterator<Character>() {
			private int index = 0; // tracks which character the iterator is on
			
			/**
			 * Return true if this IterableString has any more characters to 
			 * iterate through.
			 * 
			 * @return true if index < the length of the IterableString
			 */
			@Override
			public boolean hasNext() {
				return index < data.length();
			}
			
			/**
			 * Return the next character in the IterableString.
			 * 
			 * @return the character at index (then increment index)
			 */
			@Override
			public Character next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				return data.charAt(index++);
			}
		};
	}
	
	// For testing the class.
	public static void main(String[] args) {
		Iterator<Character> iter = new IterableString("Hello").iterator();
		while (iter.hasNext()) {
			char c = iter.next();
			System.out.println(c);
		}
		
		// Iterable objects can be used in for-each loops.
		Iterable<Character> able = new IterableString("World");
		for (char c: able) {
			System.out.println(c);
		}
	}
	
}
