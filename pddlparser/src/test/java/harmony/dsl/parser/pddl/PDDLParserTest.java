package harmony.dsl.parser.pddl;

import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import harmony.core.api.condition.Condition;
import harmony.core.api.domain.Domain;
import harmony.core.api.operator.Action;
import harmony.core.api.operator.GroundAction;
import harmony.core.api.operator.Operator;
import harmony.core.api.plan.Plan;
import harmony.core.api.problem.Problem;
import harmony.core.api.property.Property;
import harmony.core.impl.renderer.RendererImpl;
import harmony.dsl.expression.DomainExpression;
import harmony.dsl.expression.Expression;
import harmony.dsl.expression.FactExpression;
import harmony.dsl.expression.IncorrectBindingException;
import harmony.dsl.expression.OperatorExpression;
import harmony.dsl.expression.ProblemExpression;
import harmony.dsl.expression.PropertyExpression;
import harmony.dsl.expression.Scope;
import harmony.dsl.expression.UnboundVariableException;
import harmony.pddlparser.DomainParseException;
import harmony.pddlparser.PDDLDomainParser;
import harmony.pddlparser.PDDLParserFactory;
import harmony.pddlparser.PDDLProblemParser;
import harmony.pddlparser.ProblemParseException;
import harmony.planner.NoSolutionException;
import harmony.planner.PlannerInputBuilder;
import harmony.planner.Searcher;
import harmony.planner.bestfirst.AstarHeuristic;
import harmony.planner.bestfirst.BestFirstPlanner;
import harmony.pddlparser.ParseException;

public class PDDLParserTest {

	private final Logger log = LoggerFactory.getLogger(getClass());

	final String BLOCKSWORLD_DOMAIN = "blocksworld.domain.pddl";
	final String BLOCKSWORLD_PROBLEM1 = "blocksworld.problem.1.pddl";
	final String BLOCKS_DOMAIN = "blocks.domain.pddl";
	final String LOGISTICS_DOMAIN = "logistics.domain.pddl";
	final String TYPES_DOMAIN = "test-types.domain.pddl";

	@Rule
	public TestName testName = new TestName();

	private InputStream in(String resource) {
		InputStream stream = getClass().getClassLoader().getResourceAsStream(
				resource);
		return stream;
	}

	@Test
	public void testPDDLDomainParserInstance() {
		log.info(testName.getMethodName());
		PDDLDomainParser parser = PDDLParserFactory.getDomainParser(System.in);
		Assert.assertTrue(parser != null);
	}

	@Test
	public void testPDDLProblemParserInstance() {
		log.info(testName.getMethodName());
		PDDLDomainParser parser = PDDLParserFactory.getDomainParser(System.in);
		Assert.assertTrue(parser != null);
	}

	@Test
	public void testTypes() throws ParseException, DomainParseException {
		log.info(testName.getMethodName());

		PDDLDomainParser parser = PDDLParserFactory
				.getDomainParser(in(TYPES_DOMAIN));
		Assert.assertTrue(parser != null);

		DomainExpression domain = parser.getDomain();
		Assert.assertTrue(domain != null
				&& domain.getClass().equals(DomainExpression.class));
		for (Entry<String, Set<String>> te : domain.getTypes().getMap()
				.entrySet()) {
			log.info("{} declared types: ", te.getKey());
			for (String t : te.getValue()) {
				log.info("- {} ", t);
			}
		}
	}

	@Test
	public void testBlocksworldDomain() throws ParseException,
			DomainParseException {
		log.info(testName.getMethodName());

		PDDLDomainParser parser = PDDLParserFactory
				.getDomainParser(in(BLOCKSWORLD_DOMAIN));

		Assert.assertTrue(parser != null);

		DomainExpression domain = parser.getDomain();
		Assert.assertTrue(domain != null
				&& domain.getClass().equals(DomainExpression.class));
		Assert.assertTrue(domain.getDomainName().equals("blocksworld"));
		Assert.assertTrue(domain.getRequirements().size() == 1);
		Assert.assertTrue(domain.getRequirements().get(0).equals(":strips"));
		Assert.assertTrue(domain.getTypes().size() == 1);
		Assert.assertTrue(domain.getTypes().contains("block"));
		Assert.assertTrue(domain.getTypes().getMap().get("block").isEmpty());
		Assert.assertTrue(domain.getProperties().size() == 5);
		Assert.assertTrue(domain.getOperators().size() == 4);
		
		show(domain);
	}

	@Test
	public void testBlocksDomain() throws ParseException, DomainParseException {
		log.info(testName.getMethodName());

		PDDLDomainParser parser = PDDLParserFactory
				.getDomainParser(in(BLOCKS_DOMAIN));

		Assert.assertTrue(parser != null);
		DomainExpression domain = parser.getDomain();

		Assert.assertTrue(domain.getDomainName().equals("blocks"));

		Assert.assertTrue(domain.getRequirements().size() == 1);
		Assert.assertTrue(domain.getRequirements().get(0).equals(":strips"));
		Assert.assertTrue(domain.hasTypes() == false);
		Assert.assertTrue(domain.getProperties().size() == 5);
		Assert.assertTrue(domain.getOperators().size() == 4);

		show(domain);
	}

	@Test
	public void testLogisticsDomain() throws ParseException,
			DomainParseException {
		log.info(testName.getMethodName());

		PDDLDomainParser parser = PDDLParserFactory
				.getDomainParser(in(LOGISTICS_DOMAIN));

		Assert.assertTrue(parser != null);
		DomainExpression domain = parser.getDomain();
		Assert.assertTrue(domain.getDomainName().equals("logistics"));

		Assert.assertTrue(domain.getRequirements().size() == 1);
		Assert.assertTrue(domain.getRequirements().get(0).equals(":strips"));
		Assert.assertTrue(domain.hasTypes() == false);
		Assert.assertTrue(domain.getProperties().size() == 9);
		Assert.assertTrue(domain.getOperators().size() == 6);

		show(domain);
	}

	@Test
	public void testBlocksworldProblem1() throws ParseException,
			DomainParseException, ProblemParseException, UnboundVariableException, IncorrectBindingException {
		log.info(testName.getMethodName());


		PDDLDomainParser parser = PDDLParserFactory
				.getDomainParser(in(BLOCKSWORLD_DOMAIN));

		Assert.assertTrue(parser != null);

		DomainExpression domainExpr = parser.getDomain();

		PDDLProblemParser pparser = PDDLParserFactory
				.getProblemParser(in(BLOCKSWORLD_PROBLEM1));

		Assert.assertTrue(pparser != null);
		ProblemExpression problemExpr = pparser.getProblem(domainExpr);
		Assert.assertTrue(problemExpr.getProblemName().equals(domainExpr.getDomainName()));
		Domain domain = domainExpr.eval(new Scope());
		Problem problem = problemExpr.eval(new Scope());
		Searcher planner = new BestFirstPlanner(new PlannerInputBuilder(domain, problem).build(), new AstarHeuristic());

		Assert.assertTrue(domainExpr.getRequirements().size() == 1);
		Assert.assertTrue(domainExpr.getRequirements().get(0).equals(":strips"));
		Assert.assertTrue(domainExpr.hasTypes() == true);
		Assert.assertTrue(domainExpr.getProperties().size() == 5);
		Assert.assertTrue(domainExpr.getOperators().size() == 4);

		show(domainExpr);
		show(problemExpr);

		try {
			Plan plan = planner.search();
			show(plan);
		} catch (NoSolutionException e) {
			log.info("No solution :(");
		}

	}

	private void show(Plan plan){
		log.info("Solution:");
		for(GroundAction a: plan.getActions()){
			log.info(" > {}", new RendererImpl().append((Action) a).toString());
		}
	}
	private void show(ProblemExpression problem) {
		log.info("Problem: {}", problem.getProblemName());
//		for(String d : problem.declaresVariables().getOrderedKeys()){
//			log.info(" Declares {}: {}", d,  problem.declaresVariables().get(d) );
//		}
		for(FactExpression f : problem.getInitialState()){
			log.info(" - {} {}", f.getProperty().getName(), f.getVariables() );
		}
		Expression<Condition> c = problem.getGoal();
		log.info("Goal: {}", c);
	}
	
	private void show(DomainExpression domain) {
		log.info("Domain: {}", domain.getDomainName());
		log.info("Properties:");
		for (Expression<Property> p : domain.getProperties()) {
			log.info("{}", ((PropertyExpression) p).getName());
			log.info(" declares variables: ");
			for (String key : p.declaresVariables().getOrderedKeys()) {
				Class<?> t = p.declaresVariables().get(key);
				log.info("   {} ({})", key, t.getCanonicalName());
			}
		}

		log.info("Operators:");
		for (Expression<Operator> o : domain.getOperators()) {
			log.info("{}", ((OperatorExpression) o).getName());
			log.info(" declares variables: ");
			for (String key : o.declaresVariables().getOrderedKeys()) {
				Class<?> t = o.declaresVariables().get(key);
				// Assert.assertTrue(t.getCanonicalName().equals("inmemory.blocksworld.Block"));
				log.info("   {} ({})", key, t.getCanonicalName());
			}
		}
	}
}
