package harmony.dsl.expression;

import harmony.core.api.property.Property;
import harmony.core.api.thing.Thing;
import harmony.core.impl.property.BasicProperty;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class PropertyExpression implements Expression<Property> {

	private String name;

	public PropertyExpression() {
	}

	public PropertyExpression(String name, Class<? extends Thing>... argTypes) {
		Declarations decl = new Declarations();
		setName(name);
		int c = 0;
		for (Class<? extends Thing> t : argTypes) {
			decl.put("x" + c, t);
		}
		setDeclarations(decl);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDeclarations(Declarations declarations) {
		this.declarations = declarations;
	}

	private Declarations declarations;

	@Override
	public Declarations declaresVariables() {
		return declarations;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Property eval(Scope scope) throws UnboundVariableException,
			IncorrectBindingException {
		return new BasicProperty(getName(), declaresVariables()
				.getOrderedValues().toArray(
						new Class[declaresVariables().size()]));
	}

	public int getArgSize() {
		return declarations.size();
	}

	public Class<? extends Thing> getArgType(int index)
			throws IndexOutOfBoundsException {
		return declarations.getOrderedValues().get(index);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ExpressionToStringStyle.STYLE);
	}
}
