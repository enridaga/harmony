package harmony.cli;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import harmony.core.api.domain.Domain;
import harmony.core.api.operator.Action;
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
import harmony.planner.bestfirst.AstarHeuristic;
import harmony.planner.bestfirst.BestFirstPlanner;
import harmony.planner.bestfirst.heuristic.BestNodeHeuristic;
import harmony.planner.bestfirst.heuristic.GoalDistanceHeuristic;
import harmony.planner.bestfirst.heuristic.GreedyGoalDistanceHeuristic;
import harmony.planner.bestfirst.heuristic.HierarchicalCompositeBestNodeHeuristic;
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

		builder.withGroup("plan").withDescription("Search a plan").withDefaultCommand(Help.class)
				.withCommands(BestFirstCmd.class, GraphplanCmd.class);

		Cli<Runnable> gitParser = builder.build();

		Runnable r = gitParser.parse(args);
		try {
			r.run();
		} catch (Throwable t) {
			while (t.getCause() != null) {
				t = t.getCause();
			}
			System.err.print(t.getClass().getSimpleName());
			System.err.print(": ");
			System.err.println(t.getMessage());
		}
	}

	public abstract static class HarmonyCmd implements Runnable {
		@Option(type = OptionType.GLOBAL, name = "-v", description = "Verbose mode")
		public boolean verbose;

		@Option(type = OptionType.GROUP, required = true, name = "-d", description = "Domain file")
		public String domainFile;

		@Option(type = OptionType.GROUP, required = true, name = "-p", description = "Problem file")
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

		private Map<String, Class<? extends BestNodeHeuristic>> heuristics = new HashMap<String, Class<? extends BestNodeHeuristic>>();

		public BestFirstCmd() {
			heuristics.put("astar", AstarHeuristic.class);
			heuristics.put("gd", GoalDistanceHeuristic.class);
			heuristics.put("ggd", GreedyGoalDistanceHeuristic.class);
		}

		@Option(name = "-h", description = "Best First Heuristic(s). Possible values: astar (A*), gd (Goal distance), ggd (Greedy Goal distance")
		public String hopt;

		public void run() {
			Plan plan;
			try {
				BestNodeHeuristic h = null;
				if (null != hopt) {
					String[] heus = hopt.split(",");
					if (heus.length == 1) {
						h = heuristics.get(heus[0]).newInstance();
						if (h == null)
							throw new RuntimeException("Invalid argument for -h: " + heus[0]);
					} else {
						h = new HierarchicalCompositeBestNodeHeuristic();
						for (String hh : heus) {
							if (!heuristics.containsKey(hh)) {
								throw new RuntimeException("Invalid argument for -h: " + hh);
							}
							((HierarchicalCompositeBestNodeHeuristic) h).attach(heuristics.get(hh).newInstance());
						}
					}
				} else {
					h = heuristics.get("astar").newInstance();
				}
				plan = new BestFirstPlanner(buildPlannerInput(), h).search();
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (NoSolutionException e) {
				System.out.println("No solution.");
				System.exit(1);
				return;
			}
			Renderer r = new RendererImpl();
			for (Action a : plan.getActions()) {
				System.out.println(r.append(a).toString());
			}
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
			for (Action a : plan.getActions()) {
				System.out.println(r.append(a).toString());
			}
			System.exit(0);
		}
	}
}
