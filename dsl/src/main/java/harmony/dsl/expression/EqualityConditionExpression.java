package harmony.dsl.expression;

import harmony.core.api.thing.Thing;
import harmony.core.impl.condition.Equality;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class EqualityConditionExpression implements Expression<Equality> {

	private String left;
	private String right;

	private Declarations declarations;
	
	public void setup(String left, String right){
		this.left = left;
		this.right = right;
		this.declarations = new Declarations();

		this.declarations.put(left, Thing.class);
		this.declarations.put(right, Thing.class);		
	}

	@Override
	public Declarations declaresVariables() {
		return Declarations.unmodifiableDeclarations(declarations);
	}
	
	@Override
	public Equality eval(Scope scope) throws UnboundVariableException,
			IncorrectBindingException {
		if (!scope.containsKey(left)) {
			throw new UnboundVariableException("Missing binding for variable "
					+ left);
		}
		if (!scope.containsKey(right)) {
			throw new UnboundVariableException("Missing binding for variable "
					+ right);
		}
		return new Equality(scope.get(left), scope.get(right));
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ExpressionToStringStyle.STYLE);
	}
}
