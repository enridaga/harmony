package harmony.dsl.expression;

import harmony.core.api.thing.Thing;
import harmony.core.impl.condition.Type;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TypeConditionExpression implements Expression<Type> {

	private String variable;
	private Class<? extends Thing> cls;

	public void set(String variable, String type) throws ClassNotFoundException {
		this.variable = variable;
		Class<?> aCls = getClass().getClassLoader().loadClass(type);
		try {
			cls = aCls.asSubclass(Thing.class);
		} catch (ClassCastException e) {
			throw new ClassNotFoundException(
					"Given type is not a subclass of Thing.", e);
		}
	}

	@Override
	public Declarations declaresVariables() {
		return Declarations.emptyDeclarations();
	}

	@Override
	public Type eval(Scope scope) throws UnboundVariableException,
			IncorrectBindingException {
		Thing m = scope.get(variable);
		return new Type(m, cls);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ExpressionToStringStyle.STYLE);
	}
}
