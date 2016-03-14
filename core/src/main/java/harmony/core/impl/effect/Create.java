package harmony.core.impl.effect;

import harmony.core.api.effect.Effect;
import harmony.core.api.effect.GroundEffect;
import harmony.core.api.fact.Fact;
import harmony.core.api.state.State;
import harmony.core.api.thing.Thing;

public class Create implements Effect {
	private Thing thing;

	public Create(Thing thing) {
		this.thing = thing;
	}

	@Override
	public GroundEffect asGroundEffect(final State state) {
		return new GroundEffect() {

			@Override
			public Fact[] remove() {
				return new Fact[0];
			}

			@Override
			public Fact[] add() {

				return new Fact[0];
			}

			@Override
			public Thing[] create() {
				return new Thing[] { thing };
			}

			@Override
			public Thing[] destroy() {
				return new Thing[0];
			}
		};
	}

	@Override
	public String toString() {
		return new StringBuilder().append("(create ").append(thing.toString())
				.append(")").toString();
	}
}
