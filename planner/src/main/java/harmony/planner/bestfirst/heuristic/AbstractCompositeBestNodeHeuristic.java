package harmony.planner.bestfirst.heuristic;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractCompositeBestNodeHeuristic implements
		CompositeBestNodeHeuristic {
	private List<BestNodeHeuristic> heuristics;

	public AbstractCompositeBestNodeHeuristic() {
		heuristics = new ArrayList<BestNodeHeuristic>();
	}

	@Override
	public List<BestNodeHeuristic> getHeuristics() {
		return Collections.unmodifiableList(heuristics);
	}

	@Override
	public void attach(BestNodeHeuristic heuristic) {
		heuristics.add(heuristic);
	}

	@Override
	public void detach(BestNodeHeuristic heuristic) {
		heuristics.remove(heuristic);
	}
}
