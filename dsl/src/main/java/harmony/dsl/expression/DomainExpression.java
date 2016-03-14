package harmony.dsl.expression;

import harmony.core.api.domain.Domain;
import harmony.core.api.operator.Operator;
import harmony.core.api.property.Property;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class DomainExpression implements Expression<Domain> {

	private String domainName = null;

	private List<Expression<Operator>> operators = new ArrayList<Expression<Operator>>();

	private List<Expression<Property>> properties = new ArrayList<Expression<Property>>();

	private List<String> requirements = new ArrayList<String>();

	private Types types = null;

	@Override
	public Declarations declaresVariables() {
		return Declarations.emptyDeclarations();
	}

	@Override
	public Domain eval(Scope scope) throws UnboundVariableException,
			IncorrectBindingException {
		final List<Property> propertyList = new ArrayList<Property>();
		final List<Operator> operatorList = new ArrayList<Operator>();

		for (Expression<Property> p : properties) {
			propertyList.add(p.eval(scope));
		}

		for (Expression<Operator> p : operators) {
			operatorList.add(p.eval(scope));
		}

		return new Domain() {

			@Override
			public Operator[] getOperators() {
				return operatorList.toArray(new Operator[operatorList.size()]);
			}

			@Override
			public Property[] getProperty() {
				return propertyList.toArray(new Property[propertyList.size()]);
			}
		};
	}

	public String getDomainName() {
		return domainName;
	}

	public List<Expression<Operator>> getOperators() {
		return operators;
	}

	public List<Expression<Property>> getProperties() {
		return properties;
	}

	public List<String> getRequirements() {
		return requirements;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public void setOperators(List<Expression<Operator>> operators) {
		this.operators = operators;
	}

	public void setProperties(List<Expression<Property>> properties) {
		this.properties = properties;
	}

	public void setRequirements(List<String> requirements) {
		this.requirements = requirements;
	}

	public void addProperty(Expression<Property> property) {
		properties.add(property);
	}

	public void addOperator(Expression<Operator> operator) {
		operators.add(operator);
	}

	public void addRequirement(String requirement) {
		requirements.add(requirement);
	}

	public Types getTypes() {
		return types;
	}

	public void setTypes(Types types) {
		this.types = types;
	}

	public boolean hasTypes() {
		return types != null;
	}

	public PropertyExpression getProperty(String name){
		for(Expression<Property> e : properties){
			if(((PropertyExpression) e).getName().equals(name)){
				return (PropertyExpression)e;
			}
		}
		// FIXME Do it better
		return null;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ExpressionToStringStyle.STYLE);
	}
}
