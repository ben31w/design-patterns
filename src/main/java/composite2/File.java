package composite2;

public class File extends Component {
	private int size;
	
	public File(String name, int size) {
		super(name);
		
		if (size < 0) {
			throw new IllegalArgumentException("size cannot be negative");
		}
		this.size = size;
	}

	@Override
	public int getSize() {
		return size;
	}
}
