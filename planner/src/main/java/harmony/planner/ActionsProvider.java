package harmony.planner;

import harmony.core.api.operator.Action;
import harmony.core.api.operator.Operator;
import harmony.core.api.state.State;

import java.util.Collection;
import java.util.Set;

public interface ActionsProvider {
	
	public Set<Action> buildActions(Collection<Operator> o, State state);

}
