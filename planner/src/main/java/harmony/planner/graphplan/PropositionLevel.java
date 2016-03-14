package harmony.planner.graphplan;

import harmony.core.api.fact.Fact;
import harmony.core.api.operator.Action;
import harmony.core.api.state.State;

import java.util.Set;

public interface PropositionLevel extends Level<ActionLevel, Fact> {

	public Set<Action> addedBy(Fact fact);

	public Set<Action> removedBy(Fact fact);

	//public boolean isNoop(Fact fact);
	
	public State asState();
}
