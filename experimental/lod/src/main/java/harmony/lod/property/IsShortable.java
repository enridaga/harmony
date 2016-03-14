package harmony.lod.property;

import harmony.core.impl.property.BasicProperty;
import harmony.lod.model.api.slice.Path;
import harmony.lod.model.api.symbol.IRI;

public class IsShortable extends BasicProperty {
	@SuppressWarnings("unchecked")
	public IsShortable() {
		super("IsShortable", Path.class, IRI.class);
	}
}
