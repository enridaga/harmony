package harmony.planner.graphplan;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import harmony.core.api.condition.Condition;
import harmony.core.api.operator.Action;
import harmony.core.api.operator.Operator;
import harmony.core.api.operator.OperatorException;
import harmony.core.api.state.State;
import harmony.core.api.thing.Thing;
import harmony.core.impl.assessment.Evaluator;
import harmony.core.impl.condition.Not;
import harmony.planner.ActionsProvider;

public class GraphplanActionsProvider implements ActionsProvider {
	
	@Override
	public Set<Action> buildActions(Collection<Operator> operators, State state) {

		Set<Action> actions = new HashSet<Action>();
		for (Operator o : operators) {
			actions.addAll(buildActions(o, state));
		}
		return actions;
	}
	
	protected Set<Action> buildActions(Operator o, State state) {
		Set<Action> actions = new HashSet<Action>();
		// If the operator does not need any input
		if (o.getParametersTypes().size() == 0) {
			try {
				Action act = o.build(new Thing[0]);
				Condition precog = act.precondition();
				Evaluator eval = new RelaxedEvaluator(state);
				if (precog.accept(eval)) {
					actions.add(act);
				}
				act = o.build();
			} catch (OperatorException e) {
				e.printStackTrace();
			}
		} else {
			Iterator<List<Thing>> iter = state.getParametersRegistry()
					.iterator(o);
			while (iter.hasNext()) {
				try {
					List<Thing> parameters = iter.next();
					Action act = o.build(parameters
							.toArray(new Thing[parameters.size()]));
					Condition precog = act.precondition();
					Evaluator eval = new RelaxedEvaluator(state);
					if (precog.accept(eval)) {
						actions.add(act);
					}
				} catch (OperatorException e) {
					e.printStackTrace();
				}
			}
		}
		return actions;
	}
	
	// Ignore negative preconditions
	protected class RelaxedEvaluator extends Evaluator{

		public RelaxedEvaluator(State state) {
			super(state);
		}
		
		@Override
		public boolean visit(Not cond) {
			return true;
		}
	}
}
