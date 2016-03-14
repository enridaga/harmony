package harmony.dsl.expression;

import harmony.core.api.condition.Condition;
import harmony.core.impl.condition.Or;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class OrConditionExpression implements Expression<Or> {

	private List<Expression<? extends Condition>> conditions;

	public OrConditionExpression() {
		conditions = new ArrayList<Expression<? extends Condition>>();
	}

	public <T extends Condition> void append(Expression<T> condition) {
		conditions.add(condition);
	}

	public List<Expression<? extends Condition>> asList() {
		return Collections.unmodifiableList(conditions);
	}

	@Override
	public Declarations declaresVariables() {
		return Declarations.emptyDeclarations();
	}

	@Override
	public Or eval(Scope scope) throws UnboundVariableException,
			IncorrectBindingException {
		Or or = new Or();
		for (Expression<? extends Condition> uc : conditions) {
			or.append(uc.eval(scope));
		}
		return or;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ExpressionToStringStyle.STYLE);
	}
}
