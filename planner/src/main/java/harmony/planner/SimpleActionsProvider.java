package harmony.planner;

import harmony.core.api.condition.Condition;
import harmony.core.api.operator.Action;
import harmony.core.api.operator.Operator;
import harmony.core.api.operator.OperatorException;
import harmony.core.api.state.State;
import harmony.core.api.thing.Thing;
import harmony.core.impl.assessment.Evaluator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SimpleActionsProvider implements ActionsProvider {

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
				Evaluator eval = new Evaluator(state);
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
					Evaluator eval = new Evaluator(state);
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
}
