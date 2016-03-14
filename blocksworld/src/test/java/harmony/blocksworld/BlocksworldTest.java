package harmony.blocksworld;

import harmony.blocksworld.Blocksworld;
import harmony.blocksworld.BlocksworldProblem;
import harmony.blocksworld.heuristic.BlocksworldHeuristic;
import harmony.core.api.fact.Fact;
import harmony.core.api.goal.Goal;
import harmony.core.api.operator.Action;
import harmony.core.api.plan.Plan;
import harmony.core.api.renderer.Renderer;
import harmony.core.impl.renderer.RendererImpl;
import harmony.planner.ActionsProvider;
import harmony.planner.NoSolutionException;
import harmony.planner.PlannerInput;
import harmony.planner.PlannerInputBuilder;
import harmony.planner.SimpleActionsProvider;
import harmony.planner.bestfirst.AstarHeuristic;
import harmony.planner.bestfirst.BestFirstPlanner;
import harmony.planner.bestfirst.BestFirstSearchReport;
import harmony.planner.bestfirst.Node;
import harmony.planner.bestfirst.heuristic.BestNodeHeuristic;
import harmony.planner.bestfirst.heuristic.GreedyGoalDistanceHeuristic;
import harmony.planner.bestfirst.heuristic.HierarchicalCompositeBestNodeHeuristic;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlocksworldTest {

	Logger log = LoggerFactory.getLogger(getClass());
	Blocksworld domain = null;
	BlocksworldProblem problem = null;

	long start = 0;
	long end = 0;
	private Renderer renderer;

	@Before
	public void setup() {
		domain = new Blocksworld();
		problem = new BlocksworldProblem();
		renderer = new RendererImpl();
		start = 0;
		end = 0;
	}

	@Test
	public void test2() {
		log.info("test2: A");
		problem.setInitialState("A");
		problem.setGoal("A");
		Assert.assertTrue(blocky().size() == 0);
	}

	@Test
	public void test1a() {
		log.info("test1a (astar): A,B,C => B,A,C");
		problem.setInitialState("A,B,C");
		problem.setGoal("B,A,C");
		Assert.assertTrue(astar().size() != 0);
	}

	@Test
	public void test1b() {
		log.info("test1b (blocky): A,B,C => B,A,C");
		problem.setInitialState("A,B,C");
		problem.setGoal("B,A,C");
		Assert.assertTrue(blocky().size() != 0);
	}

	@Test
	public void test3() {
		log.info("test3 (astar): C A B => A,B,C");
		problem.setInitialState("C", "B", "A");
		problem.setGoal("A,B,C");
		Assert.assertTrue(astar().size() != 0);
	}

	@Test
	public void test4() {
		log.info("test4 (greedy): C,B,A => A,B,C");
		problem.setInitialState("C,B,A");
		problem.setGoal("A,B,C");
		Assert.assertTrue(greedy().size() != 0);
	}

	@Test
	public void test5() {
		log.info("test5 (astar): I,H,G,F,E,D,C,B,A => A,B,C,D,E,F,G,H,I");
		problem.setInitialState("I,H,G,F,E,D,C,B,A");
		problem.setGoal("A,B,C,D,E,F,G,H,I");
		Assert.assertTrue(astar().size() != 0);
	}

	@Test
	public void test6() {
		log.info("test6 (astar): " + "I,H,G F,E,D C,B,A => G,H,I D,E,F A,B,C");
		problem.setInitialState("I,H,G", "F,E,D", "C,B,A");
		problem.setGoal("G,H,I", "D,E,F", "A,B,C");
		Assert.assertTrue(astar().size() != 0);
	}

	@Test
	public void test7() {
		log.info("test7 (astar): J,K,L,M E,F,G,H,I A,B,C,D => A,B,C,D,G");
		problem.setInitialState("J,K,L,M", "E,F,G,H,I", "A,B,C,D");
		problem.setGoal("A,B,C,D,G");
		Assert.assertTrue(astar().size() != 0);
	}

	@Test
	public void test8() {
		log.info("test8 (astar): J,K,L,M E,F,G,H,I A,B,C,D => A,B,C,D,E");
		problem.setInitialState("J,K,L,M", "E,F,G,H,I", "A,B,C,D");
		problem.setGoal("A,B,C,D,E");
		Assert.assertTrue(astar().size() != 0);
	}

	@Test
	public void test10() {
		log.info("test10 (astar): J,K,L,M E,F A,B");
		problem.setInitialState("J,K,L,M", "E,F", "A,B");
		problem.setGoal("J,E,A", "M,L,K", "F,B");
		Assert.assertTrue(astar().size() != 0);
	}

	// @Test
	// public void test9() {
	// log.info("Unsolvable!");
	// problem.setInitialState("J,K,L,M", "E,F,G,H,I", "A,B,C,D");
	// problem.setGoal("J,E,A", "K,L,M", "F,G,H,I", "B,C,D");
	// Assert.assertTrue(astarLimited().size() == 0);
	// }

	private Plan greedy() {
		return plan(new GreedyGoalDistanceHeuristic());
	}

	private Plan blocky() {
		HierarchicalCompositeBestNodeHeuristic h = new HierarchicalCompositeBestNodeHeuristic();
		h.attach(new BlocksworldHeuristic());
		h.attach(new AstarHeuristic());
		return plan(h);
	}

	private Plan astar() {
		return plan(new AstarHeuristic());
	}

	@SuppressWarnings("unused")
	private Plan astarLimited() {
		return plan(new AstarHeuristic(), input(), new SimpleActionsProvider(),
				500);
	}

	private Plan plan(BestNodeHeuristic heuristic) {
		return plan(heuristic, input(), new SimpleActionsProvider(), -1);
	}

	private PlannerInput input() {
		return new PlannerInputBuilder(domain, problem).build();
	}

	private Plan plan(BestNodeHeuristic heuristic, PlannerInput input,
			ActionsProvider provider, int maxClosedNodes) {
		log.info("-----------------");

		BestFirstPlanner planner = new BestFirstPlanner(input, heuristic,
				provider);
		planner.setMaxClosedNodes(maxClosedNodes);
		Plan plan = null;
		try {
			log.info("Initial state:");
			renderFacts(input.getInitialState().getFacts());
			log.info("Goal:");
			renderGoal(input.getGoal());
			start = System.currentTimeMillis();
			plan = planner.search();
			end = System.currentTimeMillis();
			log.info("Plan size is: {}", plan.getActions().size());
			renderPlan(plan);
			log.info("Executed in: {}ms", end - start);
		} catch (NoSolutionException e) {
			log.error("No solution!");

			log.error("Closed nodes:");
			for (Node n : ((BestFirstSearchReport) e.getSearchReport())
					.closedNodes()) {
				log.error(renderer.append(n).toString());
			}
			Assert.assertTrue(false);
		}
		log.info("Checked  nodes: {}. Unchecked nodes: {}.",
				((BestFirstSearchReport) planner.getLastSearchReport())
						.openNodes().size(), ((BestFirstSearchReport) planner
						.getLastSearchReport()).closedNodes().size());
		Assert.assertTrue(true);
		return plan;
	}

	private void renderFacts(List<Fact> facts) {
		for (Fact f : facts) {
			log.info(renderer.append(f).toString());
		}
	}

	private void renderPlan(Plan plan) {
		for (Action e : plan.getActions()) {
			log.info("{}", renderer.append(e).toString());
		}
	}

	private void renderGoal(Goal goal) {
		log.info(renderer.append(goal).toString());
	}
}
