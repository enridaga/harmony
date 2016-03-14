package harmony.core.api.effect;

import harmony.core.api.state.State;


public interface Effect {
	public GroundEffect asGroundEffect(State state);
}
