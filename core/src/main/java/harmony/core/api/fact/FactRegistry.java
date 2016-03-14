package harmony.core.api.fact;

import harmony.core.api.property.Property;
import harmony.core.api.thing.Thing;

import java.util.List;

public interface FactRegistry  {

	public boolean put(Fact fact);

	public void putAll(List<Fact> facts);

	public void putAll(Fact... facts);

	public boolean contains(Fact fact);

	public boolean contains(List<Fact> facts);

	public boolean contains(Fact... fact);

	public boolean contains(FactRegistry factRegistry);
	
	public List<Fact> getFacts(Property property);
	
	public List<Fact> getFacts(Thing object);
	
	public boolean remove(Fact fact);

	public void removeAll(List<Fact> facts);

	public void removeAll(Fact... facts);
	
	public int size();
	
	public int size(Property property);
	
	public int size(Thing thing);
	
	public List<Fact> asList();
	
	public Fact[] toArray();

}
