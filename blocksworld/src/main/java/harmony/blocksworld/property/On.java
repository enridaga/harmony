package harmony.blocksworld.property;

import harmony.blocksworld.Block;
import harmony.core.impl.property.BasicProperty;

public final class On extends BasicProperty {
	@SuppressWarnings("unchecked")
	public On() {
		super("On", Block.class, Block.class);
	}
}
