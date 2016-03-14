package harmony.dsl.expression;

import harmony.core.api.property.Property;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class PredicatesExpression implements Expression<Set<Property>> {

	private List<Expression<Property>> pExpress;

	public PredicatesExpression() {
		pExpress = new ArrayList<Expression<Property>>();
	}

	@Override
	public Declarations declaresVariables() {
		return Declarations.emptyDeclarations();
	}

	public void addPropertyExpression(Expression<Property> pExpr) {
		this.pExpress.add(pExpr);
	}

	@Override
	public Set<Property> eval(Scope scope) throws UnboundVariableException,
			IncorrectBindingException {
		Set<Property> pp = new HashSet<Property>();
		for (Expression<Property> pe : pExpress) {
			pp.add(pe.eval(scope));
		}
		return pp;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ExpressionToStringStyle.STYLE);
	}
}
