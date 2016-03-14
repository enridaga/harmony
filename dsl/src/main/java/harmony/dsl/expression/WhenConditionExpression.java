package harmony.dsl.expression;

import harmony.core.api.condition.Condition;
import harmony.core.impl.condition.When;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class WhenConditionExpression implements Expression<When> {

	private Expression<? extends Condition> when;
	private Expression<? extends Condition> then;
	private Expression<? extends Condition> otherwise;

	public WhenConditionExpression() {

	}

	public <T extends Condition> void setWhen(Expression<T> when) {
		this.when = when;
	}

	public Expression<? extends Condition> getWhen() {
		return when;
	}

	public <T extends Condition> void setThen(Expression<T> then) {
		this.then = then;
	}

	public Expression<? extends Condition> getThen() {
		return then;
	}

	public <T extends Condition> void setOtherwise(Expression<T> otherwise) {
		this.otherwise = otherwise;
	}

	public Expression<? extends Condition> getOtherwise() {
		return otherwise;
	}

	@Override
	public Declarations declaresVariables() {
		return Declarations.emptyDeclarations();
	}

	@Override
	public When eval(Scope scope) throws UnboundVariableException,
			IncorrectBindingException {
		When groundWhen;
		if (otherwise == null) {
			groundWhen = new When(when.eval(scope), then.eval(scope));
		} else {
			groundWhen = new When(when.eval(scope), then.eval(scope),
					otherwise.eval(scope));
		}

		return groundWhen;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ExpressionToStringStyle.STYLE);
	}
}
