package harmony.blocksworld.heuristic;

import harmony.core.api.fact.Fact;
import harmony.core.api.state.State;
import harmony.core.api.thing.Thing;
import harmony.core.impl.assessment.FactsCollector;
import harmony.core.impl.state.StaticState;
import harmony.planner.bestfirst.Node;
import harmony.planner.bestfirst.heuristic.BestNodeHeuristic;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * TODO
 * This is not complete!
 * 
 * @author Enrico Daga
 *
 */
public class BlocksworldHeuristic implements BestNodeHeuristic {

	Set<Fact> goalFacts = null;
	Map<Fact, Fact> dependencies = new HashMap<Fact, Fact>();
	
	private void computeGoal(Node node){
		// Get the needed facts in the goal state
		StaticState s = new StaticState(new Fact[] {}, new Thing[]{});
		FactsCollector colly = new FactsCollector(s);
		node.getPlannerInput().getGoal().asCondition().accept(colly);
		 goalFacts = colly.getMissing();
	}
	
	private void computeDependencies(){
		for(Fact goDown : goalFacts){
			if (goDown.getProperty().getName().equals("On")){
				computeDependencies(goDown);
			}
		}
	}
	
	private void computeDependencies(Fact test){
		for(Fact goDown : goalFacts){
			if (goDown.getProperty().getName().equals("On")
					&& goDown.getThing(0).equals(test.getThing(1))) {
				 dependencies.put(test, goDown);
				 computeDependencies(goDown);
			} else if (goDown.getProperty().getName().equals("OnTable")
					&& goDown.getThing(0).equals(test.getThing(1))) {
				dependencies.put(test, goDown);
			}
		}
	}
	
	@Override
	public int compare(Node arg0, Node arg1) {
		int c0 = compute(arg0);
		int c1 = compute(arg1);
		if (c0 > c1) {
			return 1;
		} else if (c0 < c1) {
			return -1;
		}
		return 0;
	}

	private int compute(Node node) {
		
		if(goalFacts == null){
			computeGoal(node);
			computeDependencies();
		}
		
		int c = 0;
		// Prioritize nodes with positive stacks
		if(node.isRoot()){
			return 0;
		}else if(node.isGoal()){
			return 100;
		}

		if (node.getAction().operator().getName().equals("Stack")) {
			
			// take the On fact
			Fact on = null;
			for (Fact f : node.getAction().add()) {
				if (f.getProperty().getName().equals("On")) {
					on = f;
					break;
				}
			}
			boolean good = false;
			if (on != null) {
				good = traverse(node, on);
			}
			
			if(good){
				c = 1; // promote
			}else{
				c = -1; // penalize
			}
		}
		return c;
	}
	
	private boolean traverse(State state, Fact on) {
		if(on.getProperty().getName().equals("OnTable")){
			return true;
		}else if(dependencies.containsKey(on)){
			return traverse(state, dependencies.get(on));
		}else{
			return false;
		}
	}

	@Override
	public int getGoalDistance(Node node) {
		// FIXME
		// Returns the maximum as default, this heuristic must not be used
		// alone!
		return 100;
	}

}
