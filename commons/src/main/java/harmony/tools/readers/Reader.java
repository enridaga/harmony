package harmony.tools.readers;

import java.util.Collection;
import java.util.Iterator;

public interface Reader<T> extends Iterator<Object>{

	public void read(T input);
	
	public Collection<Object> getObjects();
}
