package harmony.core.impl.effect;

import harmony.core.api.effect.Effect;
import harmony.core.api.effect.GroundEffect;
import harmony.core.api.fact.Fact;
import harmony.core.api.state.State;
import harmony.core.api.thing.Thing;

public class NotEffect implements Effect {

	private Effect effect;

	public NotEffect(Effect effect) {
		this.effect = effect;
	}

	@Override
	public GroundEffect asGroundEffect(final State state) {
		final GroundEffect gf = effect.asGroundEffect(state);

		return new GroundEffect() {

			@Override
			public Fact[] remove() {
				return gf.add();
			}

			@Override
			public Fact[] add() {
				return gf.remove();
			}

			@Override
			public Thing[] create() {
				// XXX The negation of a destruction has no effect
				return new Thing[0];
			}

			@Override
			public Thing[] destroy() {
				// XXX The negation of a creation has no effect
				return new Thing[0];
			}
		};
	}

	@Override
	public String toString() {
		return new StringBuilder("(not ").append(effect.toString()).append(")")
				.toString();
	}
}
