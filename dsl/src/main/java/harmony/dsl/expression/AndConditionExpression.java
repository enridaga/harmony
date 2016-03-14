package harmony.dsl.expression;

import harmony.core.api.condition.Condition;
import harmony.core.impl.condition.And;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class AndConditionExpression implements Expression<And> {

	private List<Expression<? extends Condition>> conditions;

	public AndConditionExpression() {
		conditions = new ArrayList<Expression<? extends Condition>>();
	}

	public <T extends Condition> AndConditionExpression append(Expression<T> condition) {
		conditions.add(condition);
		return this;
	}

	public List<Expression<? extends Condition>> asList() {
		return Collections.unmodifiableList(conditions);
	}

	@Override
	public Declarations declaresVariables() {
		return Declarations.emptyDeclarations();
	}

	@Override
	public And eval(Scope scope) throws UnboundVariableException,
			IncorrectBindingException {
		And and = new And();
		for (Expression<? extends Condition> uc : conditions) {
			and.append(uc.eval(scope));
		}
		return and;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ExpressionToStringStyle.STYLE);
	}
}
