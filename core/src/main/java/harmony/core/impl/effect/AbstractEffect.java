package harmony.core.impl.effect;

import harmony.core.api.effect.Effect;
import harmony.core.api.operator.Operator;

@Deprecated
public abstract class AbstractEffect implements Effect {

	private Operator operator = null;

	public AbstractEffect(Operator operator) {
		this.operator = operator;
	}

	protected Operator getOperator(){
		return operator;
	}
}
