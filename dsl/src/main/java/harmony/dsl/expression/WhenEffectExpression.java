package harmony.dsl.expression;

import harmony.core.api.condition.Condition;
import harmony.core.api.effect.Effect;
import harmony.core.impl.effect.WhenEffect;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class WhenEffectExpression implements Expression<WhenEffect> {

	private Expression<Condition> conditionExpression;
	private Expression<Effect> ungroundThen;
	private Expression<Effect> ungroundOtherwise;

	public void setWhen(Expression<Condition> conditionExpression) {
		this.conditionExpression = conditionExpression;
	}

	public Expression<Condition> getWhen() {
		return this.conditionExpression;
	}

	public void setThen(Expression<Effect> ungroundThen) {
		this.ungroundThen = ungroundThen;
	}

	public void setOtherwise(Expression<Effect> ungroundOtherwise) {
		this.ungroundOtherwise = ungroundOtherwise;
	}

	public Expression<Effect> getThen() {
		return ungroundThen;
	}

	public Expression<Effect> getOtherwise() {
		return ungroundOtherwise;
	}

	@Override
	public Declarations declaresVariables() {
		return Declarations.emptyDeclarations();
	}

	@Override
	public WhenEffect eval(Scope scope) throws UnboundVariableException,
			IncorrectBindingException {
		WhenEffect we;
		if (this.getOtherwise() != null) {
			we = new WhenEffect(getWhen().eval(scope), getThen().eval(scope), getOtherwise().eval(scope));
		}else{
			we = new WhenEffect(getWhen().eval(scope), getThen().eval(scope));
		}
		return we;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ExpressionToStringStyle.STYLE);
	}
}
