package harmony.pddlparser;

import harmony.dsl.expression.DomainExpression;

public interface PDDLDomainParser {
	
	public DomainExpression getDomain() throws ParseException, DomainParseException;
	
}
