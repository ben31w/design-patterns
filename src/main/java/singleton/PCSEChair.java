package singleton;

public class PCSEChair {
	// Initialize static instance of this class as null.
	private static PCSEChair unique = null;
	
	// Make a private constructor.
	private PCSEChair() {
		
	}
	
	// Create a getInstance method that returns the unique instance.
	public static PCSEChair getInstance() {
		if (unique == null) {
			unique = new PCSEChair();
		}
		return unique;
	}
	
	@Override
	public String toString() {
		return "Anton Riedl";
	}
}
