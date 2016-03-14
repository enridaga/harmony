package harmony.dsl.expression;

import harmony.core.api.condition.Condition;
import harmony.core.api.effect.Effect;
import harmony.core.api.operator.Operator;
import harmony.core.api.operator.OperatorException;
import harmony.core.api.thing.Thing;
import harmony.core.impl.operator.AbstractOperator;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class OperatorExpression implements Expression<Operator> {

	private Declarations declarations;
	private Expression<? extends Condition> precondition;
	private Expression<? extends Effect> effect;
	private String name;
	private int cost;

	public OperatorExpression(String name, Declarations vars,
			Expression<Condition> precondition, Expression<Effect> effect) {
		declarations = vars;
		this.name = name;
		this.effect = effect;
		this.precondition = precondition;
	}

	public int getCost() {
		return this.cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Expression<? extends Condition> getPreconditionExpression() {
		return precondition;
	}

	public void setPrecondition(Expression<? extends Condition> expression) {
		this.precondition = expression;
	}

	public Expression<? extends Effect> getEffectExpression() {
		return effect;
	}

	@Override
	public Declarations declaresVariables() {
		return declarations;
	}

	public void setDeclarations(Declarations declarations) {
		this.declarations = declarations;
	}

	@Override
	public Operator eval(Scope scope) throws UnboundVariableException,
			IncorrectBindingException {

		return new AbstractOperator() {

			@Override
			public List<Class<? extends Thing>> getParametersTypes() {
				return OperatorExpression.this.declaresVariables()
						.getOrderedValues();
			}

			@Override
			public int getNumberOfParameters() {
				return OperatorExpression.this.declaresVariables()
						.getOrderedValues().size();
			}

			@Override
			public Condition getPrecondition(Thing... things)
					throws OperatorException {
				Condition condition = null;
				try {
					Scope scope = OperatorExpression.this
							.declaresVariables().bind(things);
					condition = getPreconditionExpression().eval(scope);
				} catch (IncorrectBindingException e) {
					throw new OperatorException(this, e);
				} catch (UnboundVariableException e) {
					throw new OperatorException(this, e);
				}
				return condition;
			}

			@Override
			public String getName() {
				return OperatorExpression.this.getName();
			}

			@Override
			public Effect getEffect(Thing... things) throws OperatorException {
				Effect effect = null;
				try {
					Scope scope = OperatorExpression.this
							.declaresVariables().bind(things);
					effect = getEffectExpression().eval(scope);
				} catch (IncorrectBindingException e) {
					throw new OperatorException(this, e);
				} catch (UnboundVariableException e) {
					throw new OperatorException(this, e);
				}
				return effect;
			}

			@Override
			public int cost() {
				return OperatorExpression.this.getCost();
			}
		};
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ExpressionToStringStyle.STYLE);
	}
}
