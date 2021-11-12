package iterator2;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * RangeIterator can iterate through a given range of adjacent numbers. The 
 * range is generated using a starting integer, an ending integer, and a 
 * boolean stating whether the range should be ascending or descending.
 * 
 * @author ben31w
 *
 */
public class RangeIterator implements Iterator<Integer> {
	private int initial;
	private int end;
	private boolean ascending;
	
	/**
	 * Construct a new RangeIterator. If the range is ascending, then the 
	 * initial value must be less than the end value. If the range is 
	 * descending (!ascending), then the initial value must be greater than the 
	 * end value. Otherwise, an IllegalArgumentException will be thrown.
	 * 
	 * @param initial
	 * 			the first number in the range
	 * @param end
	 * 			the last number in the range
	 * @param ascending
	 * 			true if the numbers in the range increase from initial to end, 
	 * 			false otherwise 
	 */
	public RangeIterator(int initial, int end, boolean ascending) {
		if (initial > end && ascending) {
			throw new IllegalArgumentException(initial + " > " + end);
		}
		else if (initial < end && !ascending) {
			throw new IllegalArgumentException(initial + " < " + end);
		}
		this.initial = initial;
		this.end = end;
		this.ascending = ascending;
	}
	
	
	/**
	 * Return true if the range has any more numbers to iterate through.
	 * 
	 * @return true if the iterator has not reached the end
	 */
	@Override
	public boolean hasNext() {
		if (ascending) {
			return initial <= end;
		}
		return initial >= end;
	}
	
	
	/**
	 * Return the next number in the range. This method adjusts the number to 
	 * return by changing the given initial value. If the range is ascending,
	 * initial is incremented after it is returned. If the range is descending,
	 * initial is decremented after it is returned.
	 * 
	 * @return initial value, then increment or decrement this value based on 
	 * 			whether the range is ascending/descending
	 */
	@Override
	public Integer next() {
		if (!hasNext()) {
			throw new NoSuchElementException("end of iterations reached");
		}
		
		if (ascending) {
			return initial++;
		}
		return initial--;
	}
	
	
	// To test the code
	public static void main(String[] args) {
		Iterator<Integer> iter = new RangeIterator(15, 20, true);
		while (iter.hasNext()) {
			int i = iter.next();
			System.out.println(i);
		}
	}
	

}
