package harmony.cli;

import java.io.File;
import java.io.FileInputStream;

import harmony.core.api.domain.Domain;
import harmony.core.api.effect.GroundEffect;
import harmony.core.api.plan.Plan;
import harmony.core.api.problem.Problem;
import harmony.core.api.renderer.Renderer;
import harmony.core.impl.renderer.RendererImpl;
import harmony.dsl.expression.DomainExpression;
import harmony.dsl.expression.ProblemExpression;
import harmony.dsl.expression.Scope;
import harmony.pddlparser.PDDLDomainParser;
import harmony.pddlparser.PDDLParserFactory;
import harmony.pddlparser.PDDLProblemParser;
import harmony.planner.NoSolutionException;
import harmony.planner.PlannerInput;
import harmony.planner.PlannerInputBuilder;
import harmony.planner.bestfirst.BestFirstPlanner;
import harmony.planner.graphplan.Graphplan;
import io.airlift.airline.Cli;
import io.airlift.airline.Cli.CliBuilder;
import io.airlift.airline.Command;
import io.airlift.airline.Help;
import io.airlift.airline.Option;
import io.airlift.airline.OptionType;

public class HarmonyCli {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		CliBuilder<Runnable> builder = Cli.<Runnable> builder("harmony").withDescription("a planner")
				.withDefaultCommand(Help.class).withCommands(Help.class);

		builder.withGroup("plan").withDescription("Search a plan")
				.withDefaultCommand(Help.class)
				.withCommands(BestFirstCmd.class, GraphplanCmd.class);

		Cli<Runnable> gitParser = builder.build();

		Runnable r = gitParser.parse(args);
		//System.out.println(r);
		r.run();
	}

	public abstract static class HarmonyCmd implements Runnable {
		@Option(type = OptionType.GLOBAL, name = "-v", description = "Verbose mode")
		public boolean verbose;

		@Option(type = OptionType.GROUP, name = "-d", description = "Domain file")
		public String domainFile;

		@Option(type = OptionType.GROUP, name = "-p", description = "Problem file")
		public String problemFile;

		protected PlannerInput buildPlannerInput() {
			try {
				PDDLDomainParser parser = PDDLParserFactory.getDomainParser(new FileInputStream(new File(domainFile)));
				DomainExpression domainExpr = parser.getDomain();
				PDDLProblemParser pparser = PDDLParserFactory
						.getProblemParser(new FileInputStream(new File(problemFile)));
				ProblemExpression problemExpr = pparser.getProblem(domainExpr);
				Domain domain = domainExpr.eval(new Scope());
				Problem problem = problemExpr.eval(new Scope());
				return new PlannerInputBuilder(domain, problem).build();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Command(name = "bestfirst", description = "Best First Search Algorithm")
	public static class BestFirstCmd extends HarmonyCmd {

		@Option(name = "-h", description = "Heuristics")
		public String heuristics;

		public void run() {
			Plan plan;
			try {
				plan = new BestFirstPlanner(buildPlannerInput()).search();
			} catch (NoSolutionException e) {
				System.out.println("No solution.");
				System.exit(1);
				return;
			}
			Renderer r = new RendererImpl();
			for (GroundEffect gf : plan.getActions()) {
				r.append(gf);
			}
			System.out.println(r.toString());
			System.exit(0);
		}
	}

	@Command(name = "graphplan", description = "Graphplan algorithm")
	public static class GraphplanCmd extends HarmonyCmd {
		@Override
		public void run() {
			Plan plan;
			try {
				plan = new Graphplan(buildPlannerInput()).search();
			} catch (NoSolutionException e) {
				System.out.println("No solution.");
				System.exit(1);
				return;
			}
			Renderer r = new RendererImpl();
			for (GroundEffect gf : plan.getActions()) {
				r.append(gf);
			}
			System.out.println(r.toString());
			System.exit(0);
		}
	}
}
