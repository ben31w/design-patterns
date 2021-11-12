package composite2;

public abstract class Component {
	private String name;
	
	protected Component(String name) {
		if (name == null) {
			throw new IllegalArgumentException("name cannot be null");
		}
		if (name.isBlank()) {
			throw new IllegalArgumentException("name cannot be blank");
		}
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract int getSize();
}
