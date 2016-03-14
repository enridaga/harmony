package harmony.core.impl.property;

import harmony.core.api.thing.Thing;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Constant extends AbstractProperty {

	@SuppressWarnings("unchecked")
	public Constant() {
		super("Constant", Thing.class);
	}

	@Override
	protected HashCodeBuilder getHashCodeBuilder() {
		return new HashCodeBuilder(17, 31).append("Constant");
	}

}
