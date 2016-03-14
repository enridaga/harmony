package harmony.planner.bestfirst.heuristic;


import java.util.List;

public interface CompositeBestNodeHeuristic extends BestNodeHeuristic {

	public List<BestNodeHeuristic> getHeuristics();

	public void attach(BestNodeHeuristic heuristic);

	public void detach(BestNodeHeuristic heuristic);
	
	public BestNodeHeuristic getLastApplied();
}
