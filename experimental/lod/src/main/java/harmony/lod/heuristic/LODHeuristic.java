package harmony.lod.heuristic;

import harmony.core.api.operator.Action;
import harmony.lod.model.api.dataset.OutputDataset;
import harmony.lod.property.Extracted;
import harmony.planner.bestfirst.Node;
import harmony.planner.bestfirst.heuristic.BestNodeHeuristic;

/**
 * Prioritize nodes with the highest ampount of extracted slices.
 * Append a dataset to the output if and only if all the slices in the temp
 * dataset must be in the output. To be used in conjunction with another, more
 * robust, heuristic, for example astar
 * 
 * @author Enrico Daga
 * 
 */
public class LODHeuristic implements BestNodeHeuristic {

	@Override
	public int compare(Node o1, Node o2) {
		int n1 = compute(o1);
		int n2 = compute(o2);
		int ret = 0;
		if(n1 == n2){
			ret = 0;
		}else if(n1 > n2){
			ret = 1;
		}else if(n1 < n2){
			ret = -1;
		}
		return ret;
	}

	private int compute(Node node) {
		
		// First, prioritize nodes with the bigger amount of extracted slices
		int score = node.getFactRegistry().getFacts(new Extracted()).size();
		
		if(!node.isRoot()){
			Action a = node.getAction();
			// This is the most important (10)
			if (a.operator().getName().equals("Append")) {
				// Get the needed slices in the goal state
				if(a.parameters()[1] instanceof OutputDataset){
					score += 10;
				}
			}
			// Then, if you can do Merge, do it (restrict the number of slices all around)
			if (a.operator().getName().equals("Merge")) {
				score += 5;
			}
			// Then, if you can do Filter, do it (restrict the number of slices all around)
			if (a.operator().getName().equals("Filter")) {
				score += 4;
			}
			// Then, if you can do Typify, do it (restrict the number of slices all around)
			if (a.operator().getName().equals("Typify")) {
				score += 3;
			}
			// Then, if you can do Append, do it (restrict the number of slices all around)
			if (a.operator().getName().equals("Append")) {
				score += 1;
			}
		}
		return score;
	}

	@Override
	public int getGoalDistance(Node node) {
		// Returns the maximum as default, this heuristic must not be used
		// alone!
		return 100;
	}

}
