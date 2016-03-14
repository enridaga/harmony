package harmony.lod.property;

import harmony.core.impl.property.BasicProperty;
import harmony.lod.model.api.symbol.IRI;

public class IsRevertable extends BasicProperty {
	@SuppressWarnings("unchecked")
	public IsRevertable() {
		super("IsRevertable", IRI.class, IRI.class);
	}
}
