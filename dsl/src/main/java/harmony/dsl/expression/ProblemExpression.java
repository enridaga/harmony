package harmony.dsl.expression;

import harmony.core.api.condition.Condition;
import harmony.core.api.fact.Fact;
import harmony.core.api.goal.Goal;
import harmony.core.api.problem.Problem;
import harmony.core.api.thing.Thing;
import harmony.core.impl.goal.GoalImpl;
import harmony.core.impl.state.InitialState;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ProblemExpression implements Expression<Problem> {

	private Declarations thingsDecl = null;
	private List<FactExpression> initialState = new ArrayList<FactExpression>();
	private Expression<Condition> goal = null;

	private String problemName = null;
	
	public String getProblemName() {
		return problemName;
	}

	public void setProblemName(String problemName) {
		this.problemName = problemName;
	}

	public void setObjects(Declarations decl) {
		thingsDecl = decl;
	}

	public void addToInitialState(FactExpression expr) {
		initialState.add(expr);
	}

	public void setGoal(Expression<Condition> goal) {
		this.goal = goal;
	}

	@Override
	public Declarations declaresVariables() {
		return thingsDecl;
	}

	@Override
	public Problem eval(Scope scope) throws UnboundVariableException,
			IncorrectBindingException {
		final List<Thing> th = new ArrayList<Thing>();
		for (String thingId : thingsDecl.getOrderedKeys()) {
			try {
				Thing t = thingsDecl.get(thingId).getConstructor(String.class)
						.newInstance(thingId);
				scope.put(thingId, t);
				th.add(t);
			} catch (IllegalArgumentException e) {
				throw new IncorrectBindingException(e);
			} catch (SecurityException e) {
				throw new IncorrectBindingException(e);
			} catch (InstantiationException e) {
				throw new IncorrectBindingException(e);
			} catch (IllegalAccessException e) {
				throw new IncorrectBindingException(e);
			} catch (InvocationTargetException e) {
				throw new IncorrectBindingException(e);
			} catch (NoSuchMethodException e) {
				throw new IncorrectBindingException(e);
			}
		}

		final Thing[] things = th.toArray(new Thing[th.size()]);

		// Add variables to scope
		List<Fact> fa = new ArrayList<Fact>();
		for (FactExpression fe : initialState) {
			fa.add(fe.eval(scope));
		}

		final Fact[] facts = fa.toArray(new Fact[fa.size()]);

		final Condition goalc = goal.eval(scope);
		
		return new Problem() {
			private Thing[] ths = things;
			private InitialState is = new InitialState(facts, things);
			private Goal g = new GoalImpl(goalc);

			@Override
			public Thing[] getObjects() {
				return ths;
			}

			@Override
			public InitialState getInitialState() {
				return is;
			}

			@Override
			public Goal getGoal() {
				return g;
			}
		};
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ExpressionToStringStyle.STYLE);
	}
}
