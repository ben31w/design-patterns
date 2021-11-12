
public class Factorial extends UnaryOperator {
	
	public Factorial(Term one) {
		super(one);
		// Factorial cannot have negative values.
		if (one.getValue() < 0) {
			throw new IllegalArgumentException( "negative value: " + one.getValue() );
		}
	}

	@Override
	public int getValue() {
		int factorial = 1;
		for (int i=2; i<=super.getTerm().getValue(); i++) {
			factorial *= i;
		}
		return factorial;
	}

}
