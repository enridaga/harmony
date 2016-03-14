package harmony.planner.test;

import harmony.core.api.fact.Fact;
import harmony.core.api.goal.Goal;
import harmony.core.api.thing.Thing;
import harmony.core.impl.goal.GoalImpl;
import harmony.core.impl.state.InitialState;
import harmony.planner.PlannerInput;

import java.util.HashSet;
import java.util.Set;

abstract class PlannerInputTest implements PlannerInput {

	protected Fact[] initf;
	protected Fact[] goalf;
	private Goal goal;
	private InitialState init;
	protected Thing[] objects = null;

	public PlannerInputTest(Fact[] init, Fact[] goalf, Thing[] objects) {
		this.initf = init;
		this.goalf = goalf;
		this.objects = objects;
		init();
	}

	public PlannerInputTest(Fact[] init, Fact[] goalf) {
		this.initf = init;
		this.goalf = goalf;

		Set<Thing> objects = new HashSet<Thing>();
		for (Fact f : initf) {
			objects.addAll(f.getThings());
		}
		for (Fact f : goalf) {
			objects.addAll(f.getThings());
		}
		this.objects = objects.toArray(new Thing[objects.size()]);
		init();
	}

	protected void init() {
		setInitialState(new InitialState(initf, getObjects()));
		setGoal(new GoalImpl(goalf));
	}

	public void setInitialState(InitialState state) {
		this.init = state;
	}

	@Override
	public InitialState getInitialState() {
		return init;
	}

	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	@Override
	public Goal getGoal() {
		return goal;
	}

	@Override
	public Thing[] getObjects() {
		return objects;
	}

}
