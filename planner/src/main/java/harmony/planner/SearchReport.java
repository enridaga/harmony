package harmony.planner;

import harmony.core.api.plan.Plan;
import harmony.planner.bestfirst.Node;

public interface SearchReport {
	
	public boolean goalFound();

	public Plan getPlan();

	public Node getGoalNode() throws NoSolutionException;
}
