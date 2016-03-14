package harmony.core.impl.state;

import harmony.core.api.fact.Fact;
import harmony.core.api.fact.FactRegistry;
import harmony.core.api.operator.ParametersRegistry;
import harmony.core.api.state.State;
import harmony.core.api.thing.Thing;
import harmony.core.api.thing.ThingRegistry;

import java.util.List;

public class InitialState implements State {
	private StaticState state;

	public InitialState(StaticState state) {
		this.state = state;
	}

	public InitialState(Fact[] facts, Thing[] things) {
		this(new StaticState(facts, things));
	}
	@Override
	public FactRegistry getFactRegistry() {
		return state.getFactRegistry();
	}
	
	@Override
	public List<Fact> getFacts() {
		return state.getFacts();
	}
	
	@Override
	public ThingRegistry getThingRegistry() {
		return state.getThingRegistry();
	}
	
	@Override
	public ParametersRegistry getParametersRegistry() {
		return state.getParametersRegistry();
	}
}
