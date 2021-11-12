import java.util.ArrayList;
import java.util.List;

public class TopFiveCities implements Observer {
	private List<City> cities = new ArrayList<>();
	
	public List<City> getTopFive() {
		return cities;
	}	
	
	@Override
	public void update(Observable observable) {
		if (observable instanceof CensusOffice) {
			CensusOffice office = (CensusOffice)observable;
			City latest = office.getReported();
			
			// Check if the latest city has a higher population than any 
			// cities in the top 5, and if so, add the city to the top 5.
			for (int i=0; i<cities.size(); i++) {
				if ( latest.population >= cities.get(i).population ) {
					cities.add(i, latest);
					break;
				}
			}
			
			// If there are less than 5 cities in the top 5 and the latest city 
			// did not get added to the list, add it to the end.
			if ( cities.size() < 5 && !cities.contains(latest) ) {
				cities.add(latest);
			}
			
			// If the top 5 ever gets above five cities, remove cities from the end.
			while (cities.size() > 5) {
				cities.remove(cities.size() - 1);
			}
		}
	}

}
