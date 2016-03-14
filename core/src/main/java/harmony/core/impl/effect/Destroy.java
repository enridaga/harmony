package harmony.core.impl.effect;

import harmony.core.api.effect.Effect;
import harmony.core.api.effect.GroundEffect;
import harmony.core.api.fact.Fact;
import harmony.core.api.state.State;
import harmony.core.api.thing.Thing;

import java.util.List;

public final class Destroy implements Effect {
	private Thing thing;

	public Destroy(Thing thing) {
		this.thing = thing;
	}

	@Override
	public GroundEffect asGroundEffect(final State state) {
		final List<Fact> toRemove = state.getFactRegistry().getFacts(thing);
		return new GroundEffect() {

			@Override
			public Fact[] remove() {
				return toRemove.toArray(new Fact[toRemove.size()]);
			}

			@Override
			public Fact[] add() {
				return new Fact[0];
			}

			@Override
			public Thing[] create() {
				return new Thing[0];
			}

			@Override
			public Thing[] destroy() {
				return new Thing[] { thing };
			}
		};
	}

	@Override
	public String toString() {
		return new StringBuilder().append("(destroy ").append(thing.toString())
				.append(")").toString();
	}
}
