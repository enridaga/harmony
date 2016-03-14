package harmony.planner.bestfirst;


import harmony.planner.bestfirst.heuristic.BestNodeHeuristic;

import java.util.HashMap;
import java.util.Map;

public class AstarHeuristic implements BestNodeHeuristic {

	private Map<Node, Integer> costMap = new HashMap<Node, Integer>();
	 private ScoreCache goalDistance = new ScoreCache(new
	 GoalDistanceProvider());
//	private ScoreCache rpGoalDistance = new ScoreCache(
//			new RelaxedStateGoalDistanceProvider());
	private ScoreCache actionCost = new ActionCostCache();

	@Override
	public int getGoalDistance(Node n) {
		// return (goalDistance.getScore(n) + rpGoalDistance.getScore(n)) /2;
		// int gd = goalDistance.getScore(n);
		// int rgd = rpGoalDistance.getScore(n);
		// if(gd > rgd){
		// //System.out.println("rgd");
		// return rgd;
		// }else{
		// //System.out.println("gd");
		// return gd;
		// }
		return goalDistance.getScore(n);
		//return rpGoalDistance.getScore(n);
	}

	public int getActionCost(Node n) {
		return actionCost.getScore(n);
	}

	public int getPathCost(Node n) {
		return getCost(n);
	}

	
	public int getCost(Node n) {
		if (costMap.containsKey(n)) {
			return costMap.get(n);
		}

		if (n.isRoot()) {
			 return goalDistance.getScore(n);
			//return rpGoalDistance.getScore(n);
		}

		/**
		 * Depth + Goal distance + Action cost
		 */
		int cost = n.getDepth() + getGoalDistance(n) + getActionCost(n);

		costMap.put(n, cost);

		int pcost = getCost((Node) n.getParent());
		// If it is lower then the parent, keep the parent cost (Pathmax
		// equation)
		if (cost < pcost) {
			return pcost;
		}

		return cost;
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
