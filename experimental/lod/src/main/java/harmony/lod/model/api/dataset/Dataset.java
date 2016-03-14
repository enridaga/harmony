package harmony.lod.model.api.dataset;

import harmony.core.api.thing.Thing;

public interface Dataset extends Thing{

	public String getName();
	
	public boolean isReadOnly();
}
