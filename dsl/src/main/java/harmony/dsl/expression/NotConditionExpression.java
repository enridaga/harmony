package harmony.dsl.expression;

import harmony.core.api.condition.Condition;
import harmony.core.impl.condition.Not;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class NotConditionExpression implements Expression<Not> {

	private Expression<? extends Condition> negated;

	public <T extends Condition> void setCondition(Expression<T> negated) {
		this.negated = negated;
	}

	@Override
	public Declarations declaresVariables() {
		return Declarations.emptyDeclarations();
	}

	@Override
	public Not eval(Scope scope) throws UnboundVariableException,
			IncorrectBindingException {
		return new Not(negated.eval(scope));
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ExpressionToStringStyle.STYLE);
	}
}
