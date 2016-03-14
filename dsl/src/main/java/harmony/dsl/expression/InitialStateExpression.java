package harmony.dsl.expression;

import harmony.core.api.fact.Fact;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class InitialStateExpression implements Expression<Set<Fact>> {

	Set<Expression<Fact>> factsExpressions;

	public InitialStateExpression() {
		factsExpressions = new HashSet<Expression<Fact>>();
	}

	@Override
	public Declarations declaresVariables() {
		return Declarations.emptyDeclarations();
	}

	public void append(Expression<Fact> fact) {
		factsExpressions.add(fact);
	}

	@Override
	public Set<Fact> eval(Scope scope) throws UnboundVariableException,
			IncorrectBindingException {
		Set<Fact> facts = new HashSet<Fact>();
		for (Expression<Fact> expr : factsExpressions) {
			facts.add(expr.eval(scope));
		}
		return facts;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ExpressionToStringStyle.STYLE);
	}
}
