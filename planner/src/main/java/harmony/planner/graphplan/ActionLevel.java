package harmony.planner.graphplan;

import harmony.core.api.fact.Fact;
import harmony.core.api.operator.Action;
import harmony.core.impl.assessment.FactsCollector;

import java.util.Set;

public interface ActionLevel extends Level<PropositionLevel, Action> {

	public FactsCollector preconditions(Action action);

	public Set<Fact> additions(Action action);

	public Set<Fact> deletions(Action action);

	public Set<Fact> additions();
	
	public Set<Fact> deletions();

}
