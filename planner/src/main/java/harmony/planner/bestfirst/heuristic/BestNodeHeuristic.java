package harmony.planner.bestfirst.heuristic;


import harmony.planner.bestfirst.Node;

import java.util.Comparator;

public interface BestNodeHeuristic extends Comparator<Node> {

	public int getGoalDistance(Node node);
}
