package harmony.core.api.property;

import harmony.core.api.thing.Thing;

import java.util.List;

public interface Property {

	public String getName();

	public List<Class<? extends Thing>> getArgTypes();
	
	public Class<? extends Thing> getArgType(int index);

	public int getArgSize();
	
	public int getArgIndex(Class<? extends Thing> argType);
	
}
