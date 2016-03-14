package harmony.planner.bestfirst.heuristic;

import harmony.planner.bestfirst.GoalDistanceProvider;
import harmony.planner.bestfirst.Node;
import harmony.planner.bestfirst.RelaxedStateGoalDistanceProvider;
import harmony.planner.bestfirst.ScoreCache;


public class GreedyGoalDistanceHeuristic implements
		BestNodeHeuristic {


	private ScoreCache goalDistance = new ScoreCache(new GoalDistanceProvider());
	private ScoreCache relaxedPlanScore = new ScoreCache(new RelaxedStateGoalDistanceProvider());
	
	public int getGoalDistance(Node n) {
		return goalDistance.getScore(n);
	}
	

//	public int getPathCost(Node n) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
	
	public int getCost(Node node) {
		return goalDistance.getScore(node);
	};
	
	public int compare(Node t, Node o) {

		if (o.equals(t)) {
			return 0;
		}

		/**
		 * Depth + Goal distance + Action cost
		 */
		/*
		 * int td = t.getGoalDistance(); int od = o.getGoalDistance();
		 * 
		 * int ta = t.getAction().cost(); int oa = o.getAction().cost();
		 * 
		 * int tf = t.getDepth(); int of = o.getDepth();
		 * 
		 * int ts = td + ta + tf; int os = od + oa + of;
		 * 
		 * if(ts > os){ return -1; }else if (os > ts){ return 1; }
		 */

		// Check using the Goal distance score
		int ts = goalDistance.getScore(t);
		int os = goalDistance.getScore(o);

		// If the goal distance is higher then the parent, do nothing
		// if it is lower, promote it
		// if it is equal, give little penalty
		int tps = goalDistance.getScore(t.getParent());
		if (ts > tps) {
		} else if (ts < tps) {
			ts = 0; // promote it
		} else {
			ts = ts+1;
		}
		int ops = goalDistance.getScore(o.getParent());
		if (os > ops) {
		} else if (os < goalDistance.getScore(o.getParent())) {
			os = 0;
		} else {
			os = os+1;
		}

		if (ts > os) {
			return -1;
		} else if (os > ts) {
			return 1;
		}

		// Shorter is higher (breadth-first)
		int tf = t.getDepth();
		int of = o.getDepth();
		if (of < tf) {
			return 1;
		} else if (tf < of) {
			return -1;
		}
		//
		/*
		// // Check using the relaxed fact set (FIXME What U R doin here?)
		if (t instanceof RPNode && o instanceof RPNode) {
			ts = ((RPNode) t).getRPScore();
			os = ((RPNode) o).getRPScore();

			if (ts < os) {
				return -1;
			} else if (os < ts) {
				return 1;
			}
		}
		*/
		ts = relaxedPlanScore.getScore(t);
		os = relaxedPlanScore.getScore(o);
		if (ts < os) {
			return -1;
		} else if (os < ts) {
			return 1;
		}
		
		//
		// Check using the action cost
		ts = t.getAction().cost();
		os = o.getAction().cost();
		if (ts > os) {
			return 1;
		} else if (ts < os) {
			return -1;
		}

		//
		return 0;
		// if (t.hashCode() > o.hashCode()) return 1;
		// return -1;
		// if (t.hashCode() > o.hashCode()) return 1;
		// else if (t.hashCode() == o.hashCode() && t.equals(o)) return 0;
		// else return -1;
	}
}
