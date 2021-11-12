package composite;

public class Number implements Term {
	private int value;
	
	public Number(int number) {
		value = number;
	}
	
	public int getValue() {
		return value;
	}
}
