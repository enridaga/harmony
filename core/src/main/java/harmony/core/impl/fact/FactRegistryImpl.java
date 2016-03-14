package harmony.core.impl.fact;

import harmony.core.api.fact.Fact;
import harmony.core.api.fact.FactRegistry;
import harmony.core.api.property.Property;
import harmony.core.api.thing.Thing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FactRegistryImpl implements FactRegistry {

	private List<Fact> registry = null;
	private Map<Property, List<Fact>> propertyFactsMap = null;
	private Map<Thing, List<Fact>> thingFactsMap = null;

	public FactRegistryImpl() {
		registry = new ArrayList<Fact>();
		propertyFactsMap = new HashMap<Property, List<Fact>>();
		thingFactsMap = new HashMap<Thing, List<Fact>>();
	}

	public boolean contains(Fact fact) {
		return registry.contains(fact);
	}

	public boolean contains(FactRegistry factRegistry) {
		return contains(factRegistry.toArray());
	}

	public boolean contains(List<Fact> facts) {
		return registry.containsAll(facts);
	}

	public boolean put(Fact fact) {
		if (registry.contains(fact)) {
			return false;
		}
		synchronized (registry) {
			registry.add(fact);
		}
		synchronized (propertyFactsMap) {
			if (!propertyFactsMap.containsKey(fact.getProperty())) {
				propertyFactsMap.put(fact.getProperty(), new ArrayList<Fact>());
			}
			propertyFactsMap.get(fact.getProperty()).add(fact);

		}
		for (Thing thing : fact.getThings()) {
			synchronized (thingFactsMap) {
				if (!thingFactsMap.containsKey(thing)) {
					thingFactsMap.put(thing, new ArrayList<Fact>());
				}
				thingFactsMap.get(thing).add(fact);
			}
		}
		return true;
	}

	public List<Fact> getFacts(Property property) {
		if(!propertyFactsMap.containsKey(property)){
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(propertyFactsMap.get(property));
	}

	public List<Fact> getFacts(Thing thing) {
		if(!thingFactsMap.containsKey(thing)){
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(thingFactsMap.get(thing));
	}

	public boolean remove(Fact fact) {
		boolean done = false;
		synchronized (registry) {
			done = registry.remove(fact);
		}
		if (done) {
			synchronized (propertyFactsMap) {
				propertyFactsMap.get(fact.getProperty()).remove(fact);
			}
			for (Thing m : fact.getThings()) {
				synchronized (thingFactsMap) {
					thingFactsMap.get(m).remove(fact);
				}
			}
			return true;
		}
		return false;
	}

	public int size() {
		return registry.size();
	}

	public int size(Property property) {
		return propertyFactsMap.get(property).size();
	}

	public int size(Thing thing) {
		return thingFactsMap.get(thing).size();
	}

	public boolean contains(Fact... fact) {
		return registry.containsAll(Arrays.asList(fact));
	}

	public List<Fact> asList() {
		return Collections.unmodifiableList(registry);
	}

	public Fact[] toArray() {
		return registry.toArray(new Fact[registry.size()]);
	}

	public void putAll(List<Fact> facts) {
		for(Fact fact : facts){
			put(fact);
		}
	}

	public void putAll(Fact... facts) {
		for(Fact fact : facts){
			put(fact);
		}
	}

	public void removeAll(List<Fact> facts) {
		for(Fact fact : facts){
			remove(fact);
		}
	}

	public void removeAll(Fact... facts) {
		for(Fact fact : facts){
			remove(fact);
		}
	}
	
	public FactRegistry clone(){
		try {
			return (FactRegistry) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
}
