package harmony.planner.graphplan;

import harmony.core.api.plan.Plan;
import harmony.planner.NoSolutionException;
import harmony.planner.PlannerInput;
import harmony.planner.Searcher;

public class Graphplan implements Searcher {

	private PlanningGraph graph;
	private PlannerInput input;

	public Graphplan(PlannerInput input) {
		this.input = input;
		this.graph = new PlanningGraphImpl(this.input);
	}

	public Graphplan(PlannerInput input, int precompute) {
		this.input = input;
		this.graph = new PlanningGraphImpl(this.input, precompute);
	}

	public PlanningGraph getGraph() {
		return graph;
	}

	@Override
	public Plan search() throws NoSolutionException {
		return new PlanExtractor(getGraph(), input).extract();
	}
}
