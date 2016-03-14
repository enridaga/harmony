package harmony.planner.bestfirst.heuristic;

import harmony.planner.bestfirst.Node;


public class HierarchicalCompositeBestNodeHeuristic extends
		AbstractCompositeBestNodeHeuristic {

	private BestNodeHeuristic lastApplied = null;
	@Override
	public int compare(Node o1, Node o2) {
		for(BestNodeHeuristic heuristic : getHeuristics()){
			int res = heuristic.compare(o1, o2);
			if(res != 0){
				this.lastApplied = heuristic;
				return res;
			}
		}
		this.lastApplied = null;
		return 0;
	}
	
	@Override
	public BestNodeHeuristic getLastApplied() {
		return lastApplied;
	}
	
	@Override
	public int getGoalDistance(Node node) {
		int best = 100;
		for(BestNodeHeuristic heuristic : getHeuristics()){
			int res = heuristic.getGoalDistance(node);
			if(res < best){
				best = res;
			}
		}
		return best;
	}
}
