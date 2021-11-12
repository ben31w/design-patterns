
public class LastCity implements Observer {
	private City latest;
	private CensusOffice lastOffice;
	
	public City getLastCity() {
		return latest;
	}
	
	public CensusOffice getLastOffice() {
		return lastOffice;
	}
	
	@Override
	public void update(Observable observable) {
		if (observable instanceof CensusOffice) {
			CensusOffice office = (CensusOffice)observable;
			lastOffice = office;
			latest = office.getReported();			
		}
	}

}
