package harmony.core.impl.effect;

import harmony.core.api.effect.Effect;
import harmony.core.api.effect.GroundEffect;
import harmony.core.api.fact.Fact;
import harmony.core.api.state.State;
import harmony.core.api.thing.Thing;

public class NoEffect implements Effect, GroundEffect {
	
	@Override
	public GroundEffect asGroundEffect(State state) {
		return this;
	}

	@Override
	public Fact[] add() {
		return new Fact[0];
	}

	@Override
	public Fact[] remove() {
		return new Fact[0];
	}

	@Override
	public Thing[] create() {
		return new Thing[0];
	}
	
	@Override
	public Thing[] destroy() {
		return new Thing[0];
	}
	
	@Override
	public String toString() {
		return "(noeffect)";
	}
}
