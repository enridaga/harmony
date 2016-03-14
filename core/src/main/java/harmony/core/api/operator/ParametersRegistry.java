package harmony.core.api.operator;

import harmony.core.api.parameters.ParametersOwner;
import harmony.core.api.thing.Thing;

import java.util.Iterator;
import java.util.List;

public interface ParametersRegistry {
	 
	public Iterator<List<Thing>> iterator(ParametersOwner owner);
	
}
