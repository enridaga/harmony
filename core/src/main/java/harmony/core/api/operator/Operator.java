package harmony.core.api.operator;

import harmony.core.api.condition.Condition;
import harmony.core.api.effect.Effect;
import harmony.core.api.parameters.ParametersOwner;
import harmony.core.api.thing.Thing;

public interface Operator extends ParametersOwner {

	public String getName();
	
	public Action build(Thing... parameters) throws OperatorException;
	
	public Condition getPrecondition(Thing... things) throws OperatorException;

	public Effect getEffect(Thing... things) throws OperatorException;

	
	public int cost();
}
