package harmony.planner.graphplan;

import harmony.core.api.thing.Thing;

import java.util.Set;

public interface Level<T extends Level<?, ?>, O> {
	public Set<O> asSet();

	public boolean contains(O o);

	public Mutex<O> getMutex();

	public T next();
	
	public Set<Thing> getthings();

}
