package observer2;

public class Genie implements Observer {
	private Person master;
	private int wishesLeft;
	
	public Genie(Person master) {
		if (master == null) {
			throw new IllegalArgumentException("master cannot be null");
		}
		this.master = master;
		master.addObserver(this);
		
		this.wishesLeft = 3;
	}
	
	public Person getMaster() {
		return master;
	}
	
	public int getWishesLeft() {
		return wishesLeft;
	}
	
	@Override
	public void update(Observable observable) {
		if ( observable instanceof Person ) {
			if (observable == master) {
				String wish = master.getWish();
				if ( wish.startsWith("I wish") ) {
					wishesLeft--; // hand-waving, wish-granting powers
					if (wishesLeft == 0) {
						master.removeObserver(this);
						master = null;
					}
				} else {
					throw new IllegalArgumentException("wish not granted [must start with 'I wish']");
				}
			} else {
				throw new IllegalArgumentException("wish not granted [not my master]");
			}	
		} else {
			throw new IllegalArgumentException("wish not granted [not a person]");
		}		
	}

}
