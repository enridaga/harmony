package harmony.integration.pddl;

import harmony.core.api.domain.Domain;
import harmony.core.api.operator.GroundAction;
import harmony.core.api.plan.Plan;
import harmony.core.api.problem.Problem;
import harmony.dsl.expression.DomainExpression;
import harmony.dsl.expression.ProblemExpression;
import harmony.dsl.expression.Scope;
import harmony.pddlparser.PDDLDomainParser;
import harmony.pddlparser.PDDLParserFactory;
import harmony.pddlparser.PDDLProblemParser;
import harmony.planner.NoSolutionException;
import harmony.planner.PlannerInput;
import harmony.planner.PlannerInputBuilder;
import harmony.planner.SearchReport;
import harmony.planner.Searcher;
import harmony.planner.bestfirst.BestFirstPlanner;
import harmony.planner.bestfirst.BestFirstSearchReport;
import harmony.planner.graphplan.Graphplan;

import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PddlIntegrationITCase extends AbstractPddlIT {

	protected Logger log = LoggerFactory.getLogger(getClass());

	@Before
	public void before() {
	}

	@After
	public void after() {
		log.info("{} completed.", testName.getMethodName());
	}

	@Test
	public void testBF_Blocks_10_0() throws Exception {
		runBlocksworld("10-0");
	}

	@Test
	public void testBF_Blocks_4_0() throws Exception {
		runBlocksworld("4-0");
	}

	@Test
	public void testBF_Blocks_4_1() throws Exception {
		runBlocksworld("4-1");
	}

	@Test
	public void testBF_Blocks_4_2() throws Exception {
		runBlocksworld("4-2");
	}

	@Test
	public void testBF_Blocks_5_0() throws Exception {
		runBlocksworld("5-0");
	}

	@Test
	public void testBF_Blocks_5_1() throws Exception {
		runBlocksworld("5-1");
	}

	@Test
	public void testBF_Blocks_5_2() throws Exception {
		runBlocksworld("5-2");
	}

	@Test
	public void testGP_Blocks_4_0() throws Exception {
		runBlocksworld("4-0", 2);
	}

	@Test
	public void testGP_Blocks_4_1() throws Exception {
		runBlocksworld("4-1", 2);
	}

	@Test
	public void testGP_Blocks_4_2() throws Exception {
		runBlocksworld("4-2",2);
	}

	@Test
	public void testGP_Blocks_5_0() throws Exception {
		runBlocksworld("5-0",2);
	}

	@Test
	public void testGP_Blocks_5_1() throws Exception {
		runBlocksworld("5-1",2);
	}

	@Test
	public void testGP_Blocks_5_2() throws Exception {
		runBlocksworld("5-2",2);
	}

	//@Test
	public void testGP_Blocks_10_0() throws Exception {
		runBlocksworld("10-0",2);
	}

	private void runBlocksworld(String postfix) throws Exception {
		runBlocksworld(postfix, 1);
	}

	private void runBlocksworld(String postfix, int planner) throws Exception {
		String domainPath = "pddl" + System.getProperty("file.separator")
				+ "blocks" + System.getProperty("file.separator")
				+ "domain.pddl";
		String problemPath = "pddl" + System.getProperty("file.separator")
				+ "blocks" + System.getProperty("file.separator")
				+ "probBLOCKS-" + postfix + ".pddl";
		runTest(planner, domainPath, problemPath);
	}

}
