package harmony.core.api.operator;

import harmony.core.api.effect.GroundEffect;
import harmony.core.api.state.State;

public interface GroundAction extends Action, GroundEffect {

	public State onState();
	
	public Action getAction();
}
