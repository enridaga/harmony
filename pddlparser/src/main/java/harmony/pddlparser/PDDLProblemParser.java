package harmony.pddlparser;

import harmony.dsl.expression.DomainExpression;
import harmony.dsl.expression.ProblemExpression;

public interface PDDLProblemParser {

	public ProblemExpression getProblem(DomainExpression domainExpr)
			throws ParseException, ProblemParseException;

}
