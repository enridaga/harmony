package harmony.core.impl.property;

import harmony.core.api.property.Property;
import harmony.core.api.property.PropertyRegistry;
import harmony.core.api.thing.Thing;

import java.util.HashMap;
import java.util.Map;

public class PropertyRegistryImpl implements PropertyRegistry {

	private Map<String, Property> registered = null;

	public PropertyRegistryImpl() {
		registered = new HashMap<String, Property>();
	}

	public void register(Property property) {
		registered.put(property.getName(), property);
	}

	public void register(String propertyName,
			Class<? extends Thing>... argTypes) {
		register(new BasicProperty(propertyName, argTypes));
	};

	public void unregister(String propertyName) {
		registered.remove(propertyName);
	}

	public boolean isRegistered(String propertyName) {
		return registered.containsKey(propertyName);
	}

	public Property getProperty(String name) {
		return registered.get(name);
	}

	public int size() {
		return registered.size();
	}
}
