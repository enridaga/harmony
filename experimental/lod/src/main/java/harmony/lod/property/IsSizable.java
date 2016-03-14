package harmony.lod.property;

import harmony.core.impl.property.BasicProperty;
import harmony.lod.model.api.symbol.IRI;

public class IsSizable extends BasicProperty {
	@SuppressWarnings("unchecked")
	public IsSizable() {
		super("IsSizable", IRI.class, IRI.class);
	}
}
