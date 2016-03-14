package harmony.planner.bestfirst;

import harmony.core.impl.assessment.ScoredEvaluator;

public class GoalDistanceProvider implements ScoreProvider {

//	private Map<Node, Float> cache = new HashMap<Node, Float>();

	@Override
	public int compute(Node node) {

		/*
		 * int factsInGoal = 0; Fact[] goal = node.getPlannerInput().getGoal();
		 * for (Fact f : goal) { if (node.getFactRegistry().contains(f)) {
		 * factsInGoal++; } } int gFactSize = goal.length; int distance =
		 * gFactSize - factsInGoal; int factor = distance * 100 / gFactSize;
		 * return factor;
		 */
		/*
		 * if(!cache.containsKey(node)){ cache.put(node, new
		 * Float(computeScore(node))); } return 100 -
		 * cache.get(node).intValue();
		 */
		return 100 - new Float(computeScore(node)).intValue();
	}

	protected float computeScore(Node node) {
		ScoredEvaluator e = new ScoredEvaluator(node, 100);
		node.getPlannerInput().getGoal().asCondition().accept(e);
		return e.getScore();
	}
}
