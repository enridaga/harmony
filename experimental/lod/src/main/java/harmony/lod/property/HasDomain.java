package harmony.lod.property;

import harmony.core.impl.property.BasicProperty;
import harmony.lod.model.api.symbol.IRI;

public class HasDomain extends BasicProperty {

	@SuppressWarnings("unchecked")
	public HasDomain() {
		super("HasDomain", IRI.class, IRI.class);
	}
}
