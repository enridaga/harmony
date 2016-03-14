package harmony.core.impl.operator;

import harmony.core.api.condition.Condition;
import harmony.core.api.effect.Effect;
import harmony.core.api.effect.GroundEffect;
import harmony.core.api.fact.Fact;
import harmony.core.api.operator.Action;
import harmony.core.api.operator.GroundAction;
import harmony.core.api.operator.Operator;
import harmony.core.api.operator.OperatorException;
import harmony.core.api.state.State;
import harmony.core.api.thing.Thing;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractOperator implements Operator {

	private String name = null;
	private List<Class<? extends Thing>> parameters = null;

	protected boolean validate(Thing... things) {
		List<Class<? extends Thing>> parameters = getParametersTypes();
		List<Thing> input = Arrays.asList(things);
		if (parameters.size() == things.length) {
			for (int x = 0; x < parameters.size(); x++) {
				Class<? extends Thing> cl = parameters.get(x);
				if (cl.isInstance(input.get(x))) {
					// go ahead
				} else {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This constructor is defined only to allow a more free definitions of name
	 * and parameters in anonymous instances
	 */
	public AbstractOperator() {

	}

	private int cost = 0;

	public void setCost(int cost) {
		this.cost = cost;
	}

	public AbstractOperator(String name, Class<? extends Thing>... parameters) {
		this.name = name;
		this.parameters = Arrays.asList(parameters);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<Class<? extends Thing>> getParametersTypes() {
		return parameters;
	}

	@Override
	public int getNumberOfParameters() {
		return parameters.size();
	}

	public int cost() {
		return cost;
	}

	@Override
	public Action build(final Thing... parameters) throws OperatorException {
		final Condition precog = getPrecondition(parameters);
		final Effect effect = getEffect(parameters);
		final Operator operator = this;
		return new Action() {
			@Override
			public int cost() {
				return operator.cost();
			}

			@Override
			public Condition precondition() {
				return precog;
			}

			@Override
			public Thing[] parameters() {
				return parameters;
			}

			@Override
			public Operator operator() {
				return operator;
			}

			@Override
			public Effect effect() {
				return effect;
			}

			public String toString(){
				StringBuilder b = new StringBuilder();
				b.append("(");
				b.append(getName());
				for(Thing m : parameters()){
					b.append(" ");
					b.append(m.getSignature());
				}
				b.append(")");
				return b.toString();
			}
			
			@Override
			public GroundAction asGroundAction(final State state) {
				final Action action = this;
				final GroundEffect gf = effect().asGroundEffect(state);
				return new GroundAction() {
					
					public Action getAction(){
						return action;
					}
					
					@Override
					public int cost() {
						return action.cost();
					}

					@Override
					public State onState() {
						return state;
					}

					@Override
					public Fact[] remove() {
						return gf.remove();
					}

					@Override
					public Fact[] add() {
						return gf.add();
					}

					@Override
					public Thing[] create() {
						return gf.create();
					}

					@Override
					public Thing[] destroy() {
						return gf.destroy();
					}

					@Override
					public Condition precondition() {
						return action.precondition();
					}

					@Override
					public Thing[] parameters() {
						return action.parameters();
					}

					@Override
					public Operator operator() {
						return action.operator();
					}

					@Override
					public Effect effect() {
						return action.effect();
					}

					@Override
					public GroundAction asGroundAction(State state) {
						return this;
					}
				};
			}
		};
	}
}
