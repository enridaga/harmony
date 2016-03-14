package harmony.core.api.parameters;

import harmony.core.api.thing.Thing;

import java.util.List;

public interface ParametersOwner {
	public int getNumberOfParameters();

	public List<Class<? extends Thing>> getParametersTypes();

}
