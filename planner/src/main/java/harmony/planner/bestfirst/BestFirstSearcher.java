package harmony.planner.bestfirst;

import harmony.planner.SearchReport;
import harmony.planner.Searcher;
import harmony.planner.bestfirst.heuristic.BestNodeHeuristic;

public interface BestFirstSearcher extends Searcher {

	public Node getBest();

	public BestNodeHeuristic getHeuristic();

	public OpenNodes<Node> getOpenNodes();

	public SearchReport getLastSearchReport();
}
