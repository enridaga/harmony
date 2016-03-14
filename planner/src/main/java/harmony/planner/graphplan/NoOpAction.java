package harmony.planner.graphplan;

import harmony.core.api.condition.Condition;
import harmony.core.api.effect.Effect;
import harmony.core.api.fact.Fact;
import harmony.core.api.operator.Action;
import harmony.core.api.operator.GroundAction;
import harmony.core.api.operator.Operator;
import harmony.core.api.operator.OperatorException;
import harmony.core.api.state.State;
import harmony.core.api.thing.Thing;
import harmony.core.impl.condition.AssertFact;
import harmony.core.impl.effect.BasicEffect;
import harmony.core.impl.operator.AbstractOperator;

public class NoOpAction  extends AbstractOperator implements Action {
	private Fact fact;

	@SuppressWarnings("unchecked")
	public NoOpAction(Fact f) {
		super("NoOp");
		this.fact = f;
	}

	@Override
	public Condition getPrecondition(Thing... things)
			throws OperatorException {
		return new AssertFact(fact);
	}

	@Override
	public Effect getEffect(Thing... things) throws OperatorException {
		return new BasicEffect(fact);
	}

	@Override
	public Operator operator() {
		return this;
	}

	@Override
	public Thing[] parameters() {
		return new Thing[0];
	}

	@Override
	public Condition precondition() {
		try {
			return getPrecondition(new Thing[] {});
		} catch (OperatorException e) {
			e.printStackTrace();
			// should never happen!
			return null;
		}
	}

	@Override
	public Effect effect() {
		try {
			return getEffect(new Thing[0]);
		} catch (OperatorException e) {
			// should never happen!
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public GroundAction asGroundAction(State state) {
		return new NoOpGroundAction(this, state);
	}

	@Override
	public int cost() {
		return 0;
	}

	public Fact getFact() {
		return fact;
	}

	public String toString() {
		return "(*NoOp " + fact + ")";
	}

	class NoOpGroundAction implements GroundAction {
		private NoOpAction action;
		private State state;

		public NoOpGroundAction(NoOpAction action, State state) {
			this.action = action;
			this.state = state;
		}

		@Override
		public GroundAction asGroundAction(State state) {
			return this;
		}

		@Override
		public Fact[] add() {
			return new Fact[] { action.getFact() };
		}

		@Override
		public Fact[] remove() {
			return new Fact[] {};
		}

		@Override
		public Thing[] create() {
			return new Thing[] {};
		}

		@Override
		public Thing[] destroy() {
			return new Thing[] {};
		}

		@Override
		public State onState() {
			return state;
		}

		@Override
		public Action getAction() {
			return action;
		}

		@Override
		public Operator operator() {
			return action.operator();
		}

		@Override
		public Thing[] parameters() {
			return action.parameters();
		}

		@Override
		public Condition precondition() {
			return action.precondition();
		}

		@Override
		public Effect effect() {
			return action.effect();
		}

		@Override
		public int cost() {
			return action.cost();
		}

		public String toString() {
			return action.toString();
		}
	}
}