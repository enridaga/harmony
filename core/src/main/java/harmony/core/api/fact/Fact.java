package harmony.core.api.fact;

import harmony.core.api.property.Property;
import harmony.core.api.thing.Thing;

import java.util.List;

public interface Fact {

	public Property getProperty();
	
	public Thing getThing(int index) throws ArrayIndexOutOfBoundsException;
	
	public int indexOf(Thing thing);
	
	public List<Thing> getThings();
	
}
