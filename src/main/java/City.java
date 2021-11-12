public final class City {
	public final String name;
	public final String state;
	public final int    population;

	public City(String name, String state, int population) {
		this.name       = name;
		this.state      = state;
		this.population = population;
	}
	@Override
	public String toString() {
		return "City [name=" + name + ", state=" + state + ", population=" + population + "]";
	}
}