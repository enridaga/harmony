package harmony.core.impl.effect;

import harmony.core.api.condition.Condition;
import harmony.core.api.effect.Effect;
import harmony.core.api.effect.GroundEffect;
import harmony.core.api.state.State;
import harmony.core.impl.assessment.Evaluator;

public class WhenEffect implements Effect {

	private Condition condition;
	private Effect then;
	private Effect otherwise;

	public WhenEffect(Condition condition, Effect then) {
		this.condition = condition;
		this.then = then;
		this.otherwise = new NoEffect();
	}

	public WhenEffect(Condition condition, Effect then, Effect otherwise) {
		this.condition = condition;
		this.then = then;
		this.otherwise = otherwise;
	}

	@Override
	public GroundEffect asGroundEffect(State state) {
		Evaluator e = new Evaluator(state);
		boolean when = condition.accept(e);
		if (when) {
			return then.asGroundEffect(state);
		} else {
			return otherwise.asGroundEffect(state);
		}
	}

	public Condition when() {
		return condition;
	}

	public Effect then() {
		return then;
	}

	public Effect otherwise() {
		return otherwise;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("(when ")
				.append(condition.toString()).append(" ")
				.append(then.toString());
		if (otherwise != null) {
			sb.append(" ").append(otherwise.toString());
		}
		return sb.append(")").toString();
	}
}
