package harmony.dsl.expression;

import harmony.core.api.thing.Thing;
import harmony.core.impl.effect.BasicEffect;
import harmony.core.impl.fact.BasicFact;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class AddFactEffectExpression extends AssertionExpression implements
		Expression<BasicEffect> {

	@Override
	public BasicEffect eval(Scope scope) throws UnboundVariableException,
			IncorrectBindingException {
		Thing[] things = new Thing[property.getArgSize()];
		for (String var : variables.getOrderedKeys()) {
			if (!scope.containsKey(var)) {
				throw new UnboundVariableException("Variable not bound: " + var);
			} else {
				Thing m = scope.get(var);
				int position = positions.get(var);
				Class<? extends Thing> cls = variables.get(var);
				if (cls.isInstance(m)) {
					things[position] = m;
				} else {
					throw new IncorrectBindingException("Object for variable "
							+ var + " must be an instance of "
							+ cls.getCanonicalName() + ", was: "
							+ m.getClass().getCanonicalName());
				}
			}
		}
		BasicEffect be = new BasicEffect();
		be.toAdd(new BasicFact(property.eval(scope), things));
		return be;
	}

	@Override
	public Declarations declaresVariables() {
		return Declarations.unmodifiableDeclarations(variables);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ExpressionToStringStyle.STYLE);
	}
}
