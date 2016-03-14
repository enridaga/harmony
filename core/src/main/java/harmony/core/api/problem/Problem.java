package harmony.core.api.problem;

import harmony.core.api.goal.Goal;
import harmony.core.api.thing.Thing;
import harmony.core.impl.state.InitialState;

public interface Problem {

	public InitialState getInitialState();
	
	public Goal getGoal();
	
	public Thing[] getObjects();
}
