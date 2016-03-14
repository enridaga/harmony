package harmony.dsl.expression;

import harmony.core.api.effect.Effect;
import harmony.core.impl.effect.NotEffect;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class NotEffectExpression implements Expression<NotEffect> {

	private Expression<Effect> effectExpression;

	public void setEffect(Expression<Effect> effectExpression) {
		this.effectExpression = effectExpression;
	}

	public Expression<Effect> getEffect() {
		return effectExpression;
	}

	@Override
	public Declarations declaresVariables() {
		return Declarations.emptyDeclarations();
	}

	@Override
	public NotEffect eval(Scope scope) throws UnboundVariableException,
			IncorrectBindingException {
		return new NotEffect(getEffect().eval(scope));
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ExpressionToStringStyle.STYLE);
	}
}
