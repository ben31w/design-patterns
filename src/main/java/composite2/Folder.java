package composite2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Folder extends Component {
	private List<Component> children;
	
	public Folder(String name) {
		super(name);
		children = new ArrayList<>();
	}
	
	@Override
	public int getSize() {
		int sum = 0;
		for (Component c: children) {
			sum += c.getSize();
		}
		return sum;
	}
	
	public Folder add(Component...components) {
//		for (Component c: components) {
//			children.add(c);
//		}
		Component[] copy = Arrays.copyOf(components, components.length);
		Collections.addAll(children, copy);
		
		return this;
	}
	
	public static void main(String[] args) {
		Folder f = new Folder("");
		File a = new File("", 0);
		File b = new File("", 0);
		File c = new File("", 0);
		File d = new File("", 0);
		
		f.add(a, b, c, d);
	}
}
