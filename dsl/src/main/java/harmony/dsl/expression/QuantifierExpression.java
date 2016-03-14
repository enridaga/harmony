package harmony.dsl.expression;

import org.apache.commons.lang3.builder.ToStringBuilder;


public abstract class QuantifierExpression<T extends Object>
		implements Expression<T> {

	private Declarations declarations = null;
	private Expression<?> expression;

	public QuantifierExpression<T> set(Declarations map,
			Expression<?> expression) {
		this.declarations = map;
		this.expression = expression;
		return this;
	}

	@Override
	public Declarations declaresVariables() {
		return Declarations.unmodifiableDeclarations(declarations);
	}

	public Expression<?> getExpression() {
		return expression;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ExpressionToStringStyle.STYLE);
	}
}
