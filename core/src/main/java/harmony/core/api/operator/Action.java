package harmony.core.api.operator;

import harmony.core.api.condition.Condition;
import harmony.core.api.effect.Effect;
import harmony.core.api.state.State;
import harmony.core.api.thing.Thing;

public interface Action {

	public Operator operator();
	
	public Thing[] parameters();
	
	public Condition precondition();
	
	public Effect effect();
	
	public GroundAction asGroundAction(State state);
	
	public int cost();
}
