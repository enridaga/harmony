package harmony.core.api.state;

import harmony.core.api.fact.Fact;
import harmony.core.api.fact.FactRegistry;
import harmony.core.api.operator.ParametersRegistry;
import harmony.core.api.thing.ThingRegistry;

import java.util.List;

public interface State {

	public List<Fact> getFacts();

	public FactRegistry getFactRegistry();

	public ThingRegistry getThingRegistry();

	public ParametersRegistry getParametersRegistry();

}
