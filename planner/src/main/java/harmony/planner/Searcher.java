package harmony.planner;

import harmony.core.api.plan.Plan;

public interface Searcher {

	public Plan search() throws NoSolutionException;
	
}
