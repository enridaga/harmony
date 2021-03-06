package harmony.dsl.expression;

import harmony.core.api.condition.Condition;
import harmony.core.api.thing.Thing;
import harmony.core.impl.condition.Forall;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ForallConditionExpression extends QuantifierExpression<Forall> {

	@Override
	public ForallConditionExpression set(Declarations declarations,
			Expression<?> expression) {
		return (ForallConditionExpression) super.set(declarations, expression);
	}

	@Override
	public Forall eval(final Scope scope) throws UnboundVariableException,
			IncorrectBindingException {

		final Expression<?> expression = getExpression();
		final List<Class<? extends Thing>> orderedTypes = declaresVariables()
				.getOrderedValues();
		@SuppressWarnings("unchecked")
		Forall forall = new Forall(
				(Class<? extends Thing>[]) orderedTypes
						.toArray(new Class[orderedTypes.size()])) {

			@Override
			public Condition getCondition(Thing... things) {
				Scope newScope = new Scope(scope);
				int index = 0;
				for (String var : ForallConditionExpression.this
						.declaresVariables().getOrderedKeys()) {
					newScope.put(var, things[index]);
					index++;
				}
				try {
					return (Condition) expression.eval(newScope);
				} catch (UnboundVariableException e) {
					throw new RuntimeException(e);
				} catch (IncorrectBindingException e) {
					throw new RuntimeException(e);
				}
			}
		};
		return forall;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ExpressionToStringStyle.STYLE);
	}
}
