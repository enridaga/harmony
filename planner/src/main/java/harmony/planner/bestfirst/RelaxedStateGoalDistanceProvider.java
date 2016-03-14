package harmony.planner.bestfirst;

import harmony.core.api.fact.Fact;
import harmony.core.api.thing.Thing;
import harmony.core.impl.assessment.ScoredEvaluator;
import harmony.core.impl.state.StaticState;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RelaxedStateGoalDistanceProvider implements ScoreProvider {

	Map<Node, StaticState> relaxedStates = new HashMap<Node, StaticState>();
	Map<StaticState, Float> scores = new HashMap<StaticState, Float>();

	@Override
	public int compute(Node node) {

		if (!relaxedStates.containsKey(node)) {
			relaxedStates.put(node, buildRelaxedState(node));
		}

		StaticState state = relaxedStates.get(node);
		if(!scores.containsKey(state)){
			ScoredEvaluator e = new ScoredEvaluator(state, 100f);
			node.getPlannerInput().getGoal().asCondition().accept(e);
			float score = e.getScore();
			scores.put(state, score);
		}
		
		int score = scores.get(state).intValue();
		return 100-score;
	}

	public StaticState buildRelaxedState(Node node) {
		if (node.isRoot()) {
			StaticState state = new StaticState(node.getFactRegistry().asList()
					.toArray(new Fact[node.getFactRegistry().asList().size()]),
					node.getThingRegistry().asSet()
					.toArray(new Thing[node.getThingRegistry().asSet().size()]));
			relaxedStates.put(node, state);
			return state;
		} else {
			Set<Fact> rp = new HashSet<Fact>();
			StaticState parent = relaxedStates.get(node.getParent());
			if(parent == null){
				parent = buildRelaxedState(node.getParent());
			}
			rp.addAll(parent.getFacts());
			rp.addAll(Arrays.asList(node.getAction().add()));

			StaticState gstate = new StaticState(
					rp.toArray(new Fact[rp.size()]), new Thing[0]); // XXX
			relaxedStates.put(node, gstate);
			return gstate;
		}
	}
}
