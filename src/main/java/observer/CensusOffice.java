package observer;

import java.util.ArrayList;
import java.util.List;

public class CensusOffice implements Observable {
	private List<Observer> observers;
	private City latest;
	private int number;
	
	/**
	 * Construct a new Census Office. Initialize the office's number and the 
	 * list to store its observers.
	 * 
	 * @param number
	 * 			the office's number (must be > 0 or an IllegalArgumentException 
	 * 			will be thrown).
	 */
	public CensusOffice(int number) {
		if (number <= 0) {
			throw new IllegalArgumentException( 
					String.format("office number must greater that 0 [%d]", number) );								  
		}
		this.number = number;
		this.observers = new ArrayList<>();
	}
	
	
	/**
	 * Return this census office's number. The number is a number greater than 
	 * 0 initialized in the constructor.
	 * 
	 * @return census office number
	 */
	public int getNumber() {
		return number;
	}
	
	
	/**
	 * Return this census office's most recently reported city.
	 * 
	 * @return most recently reported City
	 */
	public City getReported() {
		return latest;
	}
	
	
	/** 
	 * Given a City to report, set it as the latest city reported, and update 
	 * all observers.
	 * 
	 * @param latest
	 * 			a City to be reported to this Census Office
	 */
	public void report(City latest) {
		this.latest = latest;
		for ( Observer o : List.copyOf(observers) ) {
			o.update(this);
		}
	}
	
	
	/**
	 * Given an observer, add it to the appropriate observer list (if applicable).
	 * Return true if the observer is added successfully.
	 * 
	 * @param observer
	 * 			an Observer to be added to this CensusOffice
	 * @return true if observer is added successfully
	 */
	@Override
	public boolean addObserver(Observer observer) {
		if ( !observers.contains(observer) ) {
			observers.add(observer);
			return true;
		}
		return false;
	}
	
	
	/**
	 * Given an observer, remove it from the appropriate observer list (if applicable).
	 * Return true if the observer is removed successfully.
	 * 
	 * @param observer
	 * 			an Observer to be removed this CensusOffice
	 * @return true if observer is removed successfully
	 */
	@Override
	public boolean removeObserver(Observer observer) {
		if ( observers.contains(observer) ) {
			observers.remove(observer);
			return true;
		}
		return false;
	}
	
	
	/**
	 * Return true if this CensusOffice has observers.
	 * 
	 * @return true if this CensusOffice has any Last City or Top Five Cities observers.
	 */
	@Override
	public boolean hasObservers() {
		return !observers.isEmpty();
	}

}
