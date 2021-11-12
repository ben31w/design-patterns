package observer2;

import java.util.ArrayList;
import java.util.List;

public class Person implements Observable {
	private List<Genie> genies = new ArrayList<>();
	private String wish = "";
	
	public void makeWish(String words) {
		if ( words == null || words.isEmpty() ) {
			throw new IllegalArgumentException("words cannot be null or empty");
		}
		wish = words;
		notifyObservers();
	}
	
	public String getWish() {
		return wish;
	}
	
	private void notifyObservers() {
		for ( Observer o : List.copyOf(genies) ) {
			o.update(this);
		}
		// Do a reverse for loop because observers might get removed as they update
//		for (int i=genies.size()-1; i>-1; i--) {
//			Genie g = genies.get(i);
//			g.update(this);
//		}
	}
	
	@Override
	public boolean addObserver(Observer observer) {
		if (observer instanceof Genie && !genies.contains(observer)) {
			genies.add( (Genie)observer );
			return true;
		}
		return false;
	}

	@Override
	public boolean removeObserver(Observer observer) {
		if (observer instanceof Genie) {
			genies.remove( (Genie)observer );
			return true;
		}
		return false;
	}

	@Override
	public boolean hasObservers() {
		return !genies.isEmpty();
	}
	
	
}