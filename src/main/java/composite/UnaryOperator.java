package composite;

public abstract class UnaryOperator implements Term {
	private Term term;
	
	protected UnaryOperator(Term one) {
		if (one == null) {
			throw new IllegalArgumentException("Term cannot be null");
		}
		term = one;
	}
	
	public Term getTerm() {
		return term;
	}
}
