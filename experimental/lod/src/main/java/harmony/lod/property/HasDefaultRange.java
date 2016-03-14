package harmony.lod.property;

import harmony.core.impl.property.BasicProperty;
import harmony.lod.model.api.symbol.IRI;
import harmony.lod.model.api.symbol.Symbol;

public class HasDefaultRange extends BasicProperty {

	@SuppressWarnings("unchecked")
	public HasDefaultRange() {
		super("HasDefaultRange", IRI.class, Symbol.class);
	}
}
