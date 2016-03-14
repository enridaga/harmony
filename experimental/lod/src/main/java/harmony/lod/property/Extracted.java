package harmony.lod.property;

import harmony.core.impl.property.BasicProperty;
import harmony.lod.model.api.slice.Slice;

public class Extracted extends BasicProperty {
	@SuppressWarnings("unchecked")
	public Extracted() {
		super("Extracted", Slice.class);
	}
}
