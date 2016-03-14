package harmony.dsl.expression;

import harmony.core.api.effect.Effect;
import harmony.core.api.thing.Thing;
import harmony.core.impl.effect.ForallEffect;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ForallEffectExpression extends QuantifierExpression<ForallEffect> {

	@Override
	public ForallEffect eval(final Scope scope)
			throws UnboundVariableException, IncorrectBindingException {
		final Expression<?> expression = getExpression();
		final List<Class<? extends Thing>> orderedTypes = declaresVariables()
				.getOrderedValues();
		@SuppressWarnings("unchecked")
		ForallEffect forall = new ForallEffect(
				(Class<? extends Thing>[]) orderedTypes
						.toArray(new Class[orderedTypes.size()])) {

			@Override
			public Effect getEffect(Thing... things) {
				Scope newScope = new Scope(scope);
				try {
					newScope.inherit(ForallEffectExpression.this
							.declaresVariables().bind(things));
					return (Effect) expression.eval(newScope);
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
