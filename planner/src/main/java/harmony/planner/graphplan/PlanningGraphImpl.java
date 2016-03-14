package harmony.planner.graphplan;

import harmony.core.api.goal.Goal;
import harmony.core.api.operator.Operator;
import harmony.core.api.state.State;
import harmony.core.impl.assessment.Evaluator;
import harmony.planner.PlannerInput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class PlanningGraphImpl implements PlanningGraph {
	private List<Time> layers;
	private PlannerInput input;

	public PlanningGraphImpl(PlannerInput input) {
		this(input, 0);
	}

	public PlanningGraphImpl(PlannerInput input, int prepareNSteps) {
		this.input = input;
		layers = new ArrayList<Time>();
		layers.add(new TimeImpl(this));
		int c = 0;
		while (!(goalFound() || noSolution())) {
			c++;
			if (c > prepareNSteps)
				break;
			Time t = getLast();
			t.next();
		}
	}

	@Override
	public Set<Operator> getOperators() {
		return Collections.unmodifiableSet(new HashSet<Operator>(Arrays
				.asList(input.getOperators())));
	}

	@Override
	public State getRoot() {
		return input.getInitialState();
	}

	@Override
	public List<Time> asList() {
		return Collections.unmodifiableList(layers);
	}

	@Override
	public Time getFirst() {
		return layers.get(0);
	}

	@Override
	public Time getLast() {
		return layers.get(layers.size() - 1);
	}

	@Override
	public Time getTime(int index) {
		while (layers.size() <= index) {
			layers.add(new TimeImpl(this));
		}
		return layers.get(index);
	}

	@Override
	public int size() {
		return layers.size();
	}

	@Override
	public boolean goalFound() {
		// If last action level matches the goal
		State s = getLast().getPropositionLevel().asState();
		Goal goal = input.getGoal();
		Evaluator e = new Evaluator(s);
		return goal.asCondition().accept(e);
	}

	@Override
	public boolean noSolution() {
		Time last = getLast();
		if (last.step() == 0) {
			return false;
		}
		Time previous = getTime(last.step() - 1);
		return (last.getPropositionLevel().asSet()
				.equals(previous.getPropositionLevel().asSet()) && last
				.getActionLevel().asSet()
				.equals(previous.getActionLevel().asSet()));
	}
}
