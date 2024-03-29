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
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public abstract class AbstractPddlIT {
    protected Logger log = LoggerFactory.getLogger(getClass());

    @Before
    public void before() {
    }

    @After
    public void after() {
        log.info("{} completed.", testName.getMethodName());
    }

    @Rule
    public TestName testName = new TestName();

    protected void runTest(int plannerId, String domainPath, String problemPath)
            throws Exception {
        System.out.println(domainPath);
        System.out.println(problemPath);
        log.info("{} is running...", testName.getMethodName());
        InputStream dstream = getClass().getClassLoader().getResourceAsStream(
                domainPath);
        InputStream pstream = getClass().getClassLoader().getResourceAsStream(
                problemPath);
        System.out.println(dstream);
        System.out.println(pstream);
        PDDLDomainParser dparser = PDDLParserFactory.getDomainParser(dstream);
        PDDLProblemParser pparser = PDDLParserFactory.getProblemParser(pstream);

        try {
            DomainExpression domainExpr = dparser.getDomain();
            ProblemExpression problemExpr = pparser.getProblem(domainExpr);
            Domain domain = domainExpr.eval(new Scope());

            Problem problem = problemExpr.eval(new Scope());

            PlannerInput input = new PlannerInputBuilder(domain, problem)
                    .build();
            Searcher planner;
            if (plannerId == 1) {
                planner = new BestFirstPlanner(input);
            } else if (plannerId == 2) {
                planner = new Graphplan(input);
            } else {
                throw new Exception("No planner");
            }
            Plan p = planner.search();
            log.info("Done! Plan size is {}", p.size());
            for (GroundAction a : p.getActions()) {
                log.info(" > {}", a.getAction());
            }

        } catch (Exception e) {
            if (e instanceof NoSolutionException) {
                log.error("No solution!!!");
                SearchReport r = ((NoSolutionException) e).getSearchReport();
                if (r != null && r instanceof BestFirstSearchReport) {
                    log.error("Open: {}", ((BestFirstSearchReport) r)
                            .openNodes().size());
                    log.error("Closed: {}", ((BestFirstSearchReport) r)
                            .closedNodes().size());
                    log.error("Root: {}",
                            ((BestFirstSearchReport) r).getRootNode());
                }
            } else {
                throw e;
            }
        }
    }
}
