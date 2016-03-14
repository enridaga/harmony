package harmony.dsl.expression;

import harmony.core.api.fact.Fact;
import harmony.core.api.property.Property;
import harmony.core.api.thing.Thing;
import harmony.core.impl.fact.BasicFact;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class FactExpression implements Expression<Fact> {

	private PropertyExpression propertyExpr;
	private String[] variables;

	/**
	 * A fact expression uses variables, does not declare them.
	 */
	@Override
	public Declarations declaresVariables() {
		return Declarations.emptyDeclarations();
	}

	public void setProperty(PropertyExpression propertyExpr) {
		this.propertyExpr = propertyExpr;
	}
	
	public PropertyExpression getProperty(){
		return propertyExpr;
	}

	public void setVariables(String... strings) {
		this.variables = strings;
	}
	
	public String[] getVariables() {
		return this.variables;
	}

	@Override
	public Fact eval(Scope scope) throws UnboundVariableException,
			IncorrectBindingException {
		List<Thing> things = new ArrayList<Thing>();
		for (String var : variables) {
			if (scope.containsKey(var)) {
				things.add(scope.get(var));
			} else {
				throw new UnboundVariableException("Variable not bound: " + var);
			}
		}
		return new BasicFact(propertyExpr.eval(scope),
				things.toArray(new Thing[things.size()]));
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ExpressionToStringStyle.STYLE);
	}
}
