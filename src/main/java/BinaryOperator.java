
public abstract class BinaryOperator implements Term {
	private Term left; 
	private Term right;
	
	protected BinaryOperator(Term left, Term right) {
		if (left == null) {
			throw new IllegalArgumentException("Left term cannot be null");
		}
		if (right == null) {
			throw new IllegalArgumentException("Right term cannot be null");
		}
		
		this.left = left;
		this.right = right;
	}
	
	public Term getLeft() {
		return left;
	}
	
	public Term getRight() {
		return right;
	}
}
