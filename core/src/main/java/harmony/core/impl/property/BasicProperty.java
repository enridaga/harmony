package harmony.core.impl.property;

import harmony.core.api.thing.Thing;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class BasicProperty extends AbstractProperty {

	public BasicProperty(String name, Class<? extends Thing>... argTypes) {
		super(name, argTypes);
	}

	public BasicProperty(String name, int numberOfthings) {
		super(name, numberOfthings);
	}

	@Override
	protected HashCodeBuilder getHashCodeBuilder() {
		return new HashCodeBuilder(17, 31).append(getName());
	}
}
