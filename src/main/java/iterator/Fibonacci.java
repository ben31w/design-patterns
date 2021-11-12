package iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Fibonacci implements Iterable<Long> {

	@Override
	public Iterator<Long> iterator() {
		
		return new Iterator<Long>() {
			private long value1 = 1;
			private long value2 = 1;
			private int index = 0; // only tracks index of first two fibonacci numbers
			@Override
			public boolean hasNext() {
				return value1 + value2 <= Long.MAX_VALUE;
			}

			@Override
			public Long next() {
				if (!hasNext()) {
					throw new NoSuchElementException("term is too high");
				}
				
				if (index == 0 || index == 1) {
					++index;
					return (long) 1;
				}
				
				// store the sum of values 1 and 2
				long sum = value1 + value2;
				
				// shift the values
				value1 = value2;
				value2 = sum;
				
				return sum;
			}		
		};
		
	}

}
