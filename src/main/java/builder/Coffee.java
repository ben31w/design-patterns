package builder;

public class Coffee {
	
	private String brand;
	private int year;
	
	public static class Builder {
		private String brand;
		private int year;
		
		public Builder brand(String name) {
			if (name == null || name.isBlank()) {
				throw new IllegalArgumentException("brand cannot be null or empty");
			}
			this.brand = name;
			return this;
		}
		public Builder expiration(int year) {
			if (year < 2024) {
				throw new IllegalArgumentException("expiration cannot be before 2024");
			}
			this.year = year;
			return this;
		}
		public boolean isValid() {
			return brand != null && year != 0;
		}
		public Coffee build() {
			if ( isValid() ) {
				return new Coffee(this);
			}
			
			StringBuilder s = new StringBuilder();
			if (brand == null) {
				s.append("brand");
			}
			if (year == 0) {
				if(s.length() > 0) {
					s.append(",");
				}
				s.append("year");
			}
			throw new IllegalStateException(String.format("missing data [%s]", s));
		}
	}
	
	private Coffee(Builder builder) {
		this.brand = builder.brand;
		this.year = builder.year;
	}
	
	public String getBrand() {
		return brand;
	}
	
	public int getExpiration() {
		return year;
	}
	
	public String toString() {
		return String.format("Coffee [brand=%s,year=%d]", brand, year);
	}

}
