package harmony.dsl.expression;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class AssertionExpression {

	protected PropertyExpression property;
	protected Declarations variables;
	protected Map<String, Integer> positions;
	
	private AssertionExpression setVariables(String...vars ) throws AssertionExpressionException{
		
		if(property == null){
			throw new AssertionExpressionException("Cannot set variables with an undefined Property");
		}
		
		this.positions = new HashMap<String, Integer>();
		Declarations varmap = new Declarations();

		for (int x = 0; x < vars.length; x++) {
			try {
				positions.put(vars[x], x);
				varmap.put(vars[x], property.getArgType(x));

			} catch (IndexOutOfBoundsException e) {
				throw new AssertionExpressionException("Cannot find argument "
						+ x);
			}
		}

		this.variables = varmap;
		if (property.getArgSize() != variables.size()) {
			throw new AssertionExpressionException(
					"Number of variables must match the Property definition");
		}
		return this;
	}
	
	public AssertionExpression set(PropertyExpression property, String...vars) throws AssertionExpressionException{
		this.property = property;
		this.setVariables(vars);
		return this;
	}
	
	public PropertyExpression getProperty(){
		return property;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ExpressionToStringStyle.STYLE);
	}
}
