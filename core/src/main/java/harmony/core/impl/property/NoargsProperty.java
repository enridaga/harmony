package harmony.core.impl.property;

import harmony.core.api.thing.Thing;

import java.util.Collections;
import java.util.List;

public class NoargsProperty extends BasicProperty {

	@SuppressWarnings("unchecked")
	public NoargsProperty(String name) {
		super(name);
	}

	public List<Class<? extends Thing>> getArgTypes() {
		return Collections.emptyList();
	}
}
