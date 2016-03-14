package harmony.core.impl.state;

import harmony.core.api.effect.GroundEffect;
import harmony.core.api.fact.Fact;
import harmony.core.api.fact.FactRegistry;
import harmony.core.api.operator.ParametersRegistry;
import harmony.core.api.state.State;
import harmony.core.api.thing.Thing;
import harmony.core.api.thing.ThingRegistry;
import harmony.core.impl.fact.FactRegistryImpl;
import harmony.core.impl.parameters.ParametersRegistryImpl;
import harmony.core.impl.thing.ThingRegistryImpl;

import java.util.Collection;
import java.util.List;

public class StaticState implements State {

	private FactRegistry state = null;
	private ThingRegistry things = null;
	private ParametersRegistry parameters = null;

	public StaticState() {
		init(new Fact[0], new Thing[0]);
	}

	public StaticState(Fact[] facts, Thing[] objects) {
		init(facts, objects);
	}

	public StaticState(Collection<Fact> facts, Collection<? extends Thing> things) {
		this(facts.toArray(new Fact[facts.size()]), things
				.toArray(new Thing[things.size()]));
	}

	protected void init(Fact[] facts, Thing[] objects) {
		state = new FactRegistryImpl();
		state.putAll(facts);
		things = new ThingRegistryImpl();
		for (Thing m : objects) {
			things.put(m);
		}
		for (Fact f : facts){
			for (Thing m : f.getThings()){
				things.put(m);
			}
		}
		parameters = new ParametersRegistryImpl(things);
	}

	public boolean add(Fact fact) {
		boolean r = state.put(fact);
		if (r) {
			for (Thing m : fact.getThings()) {
				things.put(m);
			}
		}
		return r;
	}

	public boolean remove(Fact fact) {
		boolean r = state.remove(fact);
		if (r) {
			for (Thing m : fact.getThings()) {
				things.remove(m);
			}
		}
		return r;
	}

	public void add(Fact[] facts) {
		for (Fact f : facts) {
			add(f);
		}
	}

	public void remove(Fact[] facts) {
		for (Fact f : facts) {
			remove(f);
		}
	}

	public void apply(GroundEffect effect) {
		this.add(effect.add());
		this.remove(effect.remove());
	}

	@Override
	public FactRegistry getFactRegistry() {
		return state;
	}

	@Override
	public List<Fact> getFacts() {
		return state.asList();
	}

	@Override
	public ThingRegistry getThingRegistry() {
		return things;
	}

	@Override
	public ParametersRegistry getParametersRegistry() {
		return parameters;
	}
}
