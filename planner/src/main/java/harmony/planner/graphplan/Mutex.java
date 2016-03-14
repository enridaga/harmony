package harmony.planner.graphplan;

import java.util.Map;
import java.util.Set;

public interface Mutex<T> {

	public boolean hasMutex(T object);
	
	public Set<T> getMutex(T object);
	
	public void put(T mutex0, T mutex1);
	
	public Map<T,Set<T>> asMap();
}
