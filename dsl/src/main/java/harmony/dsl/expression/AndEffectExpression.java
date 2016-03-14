package harmony.dsl.expression;

import harmony.core.api.effect.CompositeEffect;
import harmony.core.api.effect.Effect;
import harmony.core.impl.effect.CompositeEffectImpl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class AndEffectExpression implements Expression<Effect> {

	List<Expression<? extends Effect>> effectList;
	
	public AndEffectExpression() {
		effectList = new ArrayList<Expression<? extends Effect>>();
	}
	
	public <T extends Effect> AndEffectExpression append(Expression<T> effectExpression) {
		effectList.add(effectExpression);
		return this;
	}
	
	@Override
	public Declarations declaresVariables() {
		return Declarations.emptyDeclarations();
	}

	@Override
	public CompositeEffect eval(Scope scope) throws UnboundVariableException,
			IncorrectBindingException {
		CompositeEffect eff = new CompositeEffectImpl();
		for(Expression<? extends Effect> ue : effectList){
			eff.append(ue.eval(scope));
		}
		return eff;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ExpressionToStringStyle.STYLE);
	}
}
