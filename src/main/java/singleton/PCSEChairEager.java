package singleton;

public class PCSEChairEager {
	private static PCSEChairEager unique;
	
	static {
		unique = new PCSEChairEager();
	}
	
	private PCSEChairEager() {
		
	}
	
	public static PCSEChairEager getInstance() {
		return unique;
	}
	
	public String toString() {
		return "Anton Riedl";
	}
}
