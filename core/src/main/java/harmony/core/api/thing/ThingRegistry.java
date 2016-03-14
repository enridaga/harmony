package harmony.core.api.thing;


import java.util.Set;

public interface ThingRegistry {

	public Set<? extends Thing> get(Class<? extends Thing> T);
	
	public boolean put(Thing o);
	
	public Set<Class<? extends Object>> types();
	
	public int size();
	
	public int sizeOf(Class<?> c);
	
	public boolean remove(Class<? extends Thing> c);
	
	public boolean remove(Thing o);
	
	public boolean contains(Thing o);
	
	public boolean contains(Class<?> o);

	public Set<? extends Thing> asSet();
}
