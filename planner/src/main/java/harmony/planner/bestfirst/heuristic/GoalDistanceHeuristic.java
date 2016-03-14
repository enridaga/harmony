package harmony.planner.bestfirst.heuristic;

import harmony.planner.bestfirst.GoalDistanceProvider;
import harmony.planner.bestfirst.Node;
import harmony.planner.bestfirst.ScoreCache;


public class GoalDistanceHeuristic implements BestNodeHeuristic {

	 private ScoreCache goalDistance = new ScoreCache(new
	 GoalDistanceProvider());

	@Override
	public int getGoalDistance(Node n) {
		return goalDistance.getScore(n);
	}
	
	public int getCost(Node n) {
		return goalDistance.getScore(n);
	}

	public int compare(Node t, Node o) {

		if (o.equals(t)) {
			return 0;
		}

		int tcost = getCost(t);
		int ocost = getCost(o);

		// The lower must go at the end of the list
		if (tcost < ocost) {
			return 1;
		} else if (tcost > ocost) {
			return -1;
		}

		//
		return 0;

	}
}
