package harmony.core.api.effect;

import harmony.core.api.parameters.ParametersOwner;
import harmony.core.api.thing.Thing;

public interface ParametrizedEffect extends Effect, ParametersOwner {

	public void setParameters(Class<? extends Thing>... types);

	public Effect getEffect(Thing... things);
}
