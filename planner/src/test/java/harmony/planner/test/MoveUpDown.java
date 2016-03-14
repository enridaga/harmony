package harmony.planner.test;

import harmony.core.api.condition.Condition;
import harmony.core.api.effect.Effect;
import harmony.core.api.fact.Fact;
import harmony.core.api.goal.Goal;
import harmony.core.api.operator.Operator;
import harmony.core.api.operator.OperatorException;
import harmony.core.api.property.Property;
import harmony.core.api.thing.Thing;
import harmony.core.impl.condition.And;
import harmony.core.impl.condition.AssertFact;
import harmony.core.impl.condition.Not;
import harmony.core.impl.effect.BasicEffect;
import harmony.core.impl.fact.BasicFact;
import harmony.core.impl.goal.GoalImpl;
import harmony.core.impl.operator.AbstractOperator;
import harmony.core.impl.property.BasicProperty;
import harmony.core.impl.state.InitialState;
import harmony.core.impl.thing.Something;
import harmony.planner.PlannerInput;

public class MoveUpDown implements PlannerInput {

	public MoveUpDown() {
	}

	public MoveUpDown(Fact[] init, Fact[] goal) {
		setInitialState(init);
		setGoal(goal);
	}

	public void setGoal(Fact[] goalf) {
		goal = new GoalImpl(goalf);
	}

	@SuppressWarnings("unchecked")
	public static final Property on = new BasicProperty("On", Something.class,
			Something.class);
	@SuppressWarnings("unchecked")
	public static final Property under = new BasicProperty("Under",
			Something.class, Something.class);

	public static final Thing cat = new Something("Cat");
	public static final Thing dog = new Something("Dog");
	public static final Thing mouse = new Something("Mouse");
	public static final Thing horse = new Something("Horse");
	public static final Thing barbie = new Something("Barbie");
	public static final Thing piggy = new Something("Piggy");
	public static final Thing table = new Something("Table");

	@SuppressWarnings("unchecked")
	public static final Operator moveOn = new AbstractOperator("moveOn",
			Thing.class, Thing.class) {

		public Condition getPrecondition(final Thing... things)
				throws OperatorException {
			return new And().append(
					new AssertFact(new BasicFact(under, things[0], things[1])))
					.append(new Not(new AssertFact(new BasicFact(on, things[0],
							things[1]))));
		}

		public Effect getEffect(final Thing... things) throws OperatorException {
			BasicEffect e = new BasicEffect();
			e.toAdd(new BasicFact(on, things[0], things[1]));
			e.toRemove(new BasicFact(under, things[0], things[1]));
			return e;
		}
	};
	@SuppressWarnings("unchecked")
	public static final Operator moveUnder = new AbstractOperator("moveUnder",
			Thing.class, Thing.class) {

		public Condition getPrecondition(final Thing... things)
				throws OperatorException {
			return new And().append(
					new AssertFact(new BasicFact(on, things[0], things[1])))
					.append(new Not(new AssertFact(new BasicFact(under,
							things[0], things[1]))));
		}

		public Effect getEffect(final Thing... things) throws OperatorException {
			BasicEffect e = new BasicEffect();
			e.toAdd(new BasicFact(under, things[0], things[1]));
			e.toRemove(new BasicFact(on, things[0], things[1]));
			return e;
		}
	};

	private InitialState init;
	private Goal goal;

	public void setInitialState(Fact... facts) {
		init = new InitialState(facts, getObjects());
	}

	public InitialState getInitialState() {
		return init;
	}

	public Goal getGoal() {
		return goal;
	}

	public Property[] getProperty() {
		final Property[] pp = { on, under };
		return pp;
	}

	public Operator[] getOperators() {
		final Operator[] oo = { moveOn, moveUnder };
		return oo;
	}

	@Override
	public Thing[] getObjects() {
		return new Thing[] { dog, horse, piggy, mouse, barbie };
	}

	public static Fact on(Thing th0, Thing th1) {
		return new BasicFact(MoveUpDown.on, th0, th1);
	}

	public static Fact under(Thing th0, Thing th1) {
		return new BasicFact(MoveUpDown.under, th0, th1);
	}

}
