package harmony.planner.bestfirst;

import static harmony.planner.test.MoveUpDown.barbie;
import static harmony.planner.test.MoveUpDown.cat;
import static harmony.planner.test.MoveUpDown.dog;
import static harmony.planner.test.MoveUpDown.horse;
import static harmony.planner.test.MoveUpDown.mouse;
import static harmony.planner.test.MoveUpDown.on;
import static harmony.planner.test.MoveUpDown.piggy;
import static harmony.planner.test.MoveUpDown.table;
import static harmony.planner.test.MoveUpDown.under;
import static harmony.planner.test.RocketDomain.Amsterdam;
import static harmony.planner.test.RocketDomain.Fish0;
import static harmony.planner.test.RocketDomain.Flowers0;
import static harmony.planner.test.RocketDomain.London;
import static harmony.planner.test.RocketDomain.Meat0;
import static harmony.planner.test.RocketDomain.Paris;
import static harmony.planner.test.RocketDomain.R0;
import static harmony.planner.test.RocketDomain.R1;
import static harmony.planner.test.RocketDomain.R2;
import static harmony.planner.test.RocketDomain.R3;
import static harmony.planner.test.RocketDomain.Rome;
import static harmony.planner.test.RocketDomain.at;
import static harmony.planner.test.RocketDomain.hasFuel;
import harmony.core.api.fact.Fact;
import harmony.core.api.operator.GroundAction;
import harmony.core.api.plan.Plan;
import harmony.planner.NoSolutionException;
import harmony.planner.PlannerInput;
import harmony.planner.Searcher;
import harmony.planner.test.BlocksworldDomain;
import harmony.planner.test.MoveUpDown;
import harmony.planner.test.RocketDomain;

import java.util.List;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BestFirstPlannerTest {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Rule
	public TestName name = new TestName();

	@Test
	public void moveupdown1() throws NoSolutionException {
		Fact[] init = { on(cat, table), on(dog, table), on(barbie, table),
				on(horse, table), on(piggy, table), on(mouse, table), };

		Fact[] goalf = { under(cat, table), under(dog, table),
				under(barbie, table), under(horse, table), under(piggy, table),
				under(mouse, table) };

		search(new MoveUpDown(init, goalf));
	}

	@Test
	public void rocket1() throws NoSolutionException {
		Fact[] init = { at(R0, Rome), at(Fish0, Rome), at(Meat0, Rome),
				hasFuel(R0) };

		Fact[] goalf = { at(Fish0, London), at(Meat0, London) };

		search(new RocketDomain(init, goalf));
	}

	@Test
	public void rocket2() throws NoSolutionException {
		Fact[] init = { at(R0, Rome), at(Fish0, Rome), at(Meat0, Paris),
				hasFuel(R0), at(R1, Paris), hasFuel(R1) };

		Fact[] goalf = { at(Fish0, London), at(Meat0, London) };

		search(new RocketDomain(init, goalf));
	}

	@Test
	public void rocket3() throws NoSolutionException {
		Fact[] init = { at(R0, Rome), at(Fish0, Rome), at(Meat0, Paris),
				hasFuel(R0), at(R1, Paris), hasFuel(R1), at(R2, Rome),
				hasFuel(R2) };

		Fact[] goalf = { at(Fish0, London), at(Meat0, London) };

		search(new RocketDomain(init, goalf));
	}

	@Test
	public void rocket4() throws NoSolutionException {
		Fact[] init = { at(R0, Rome), at(Fish0, Rome), at(Meat0, Paris),
				hasFuel(R0), at(R1, Paris), hasFuel(R1), at(R2, Rome),
				hasFuel(R2), at(Flowers0, Amsterdam), at(R3, Amsterdam),
				hasFuel(R3) };

		Fact[] goalf = { at(Fish0, London), at(Meat0, London),
				at(Flowers0, Paris) };

		search(new RocketDomain(init, goalf));
	}

	@Test
	public void blocksworld1() throws NoSolutionException {
		Fact[] init = BlocksworldDomain.stacksToFacts("A,B,C");
		Fact[] goal = BlocksworldDomain.stacksToFacts("C,B,A");
		search(new BlocksworldDomain(init, goal));
	}

	@Test
	public void blocksworld2() throws NoSolutionException {
		Fact[] init = BlocksworldDomain.stacksToFacts("C,B,A");
		Fact[] goal = BlocksworldDomain.stacksToFacts("A", "B", "C");
		search(new BlocksworldDomain(init, goal));
	}
//
	@Test
	public void blocksworld3() {
		Fact[] init = BlocksworldDomain.stacksToFacts("C", "B", "A");
		Fact[] goal = BlocksworldDomain.stacksToFacts("A,B,C");
		search(new BlocksworldDomain(init, goal));
	}
	
	@Test
	public void blocksworld4() {
		Fact[] init = BlocksworldDomain.stacksToFacts("C,B,A");
		Fact[] goal = BlocksworldDomain.stacksToFacts("A,B,C");
		search(new BlocksworldDomain(init, goal));
	}

	@Test
	public void blocksworld5() {
		Fact[] init = BlocksworldDomain.stacksToFacts("I,H,G,F,E,D,C,B,A");
		Fact[] goal = BlocksworldDomain.stacksToFacts("A,B,C,D,E,F,G,H,I");
		search(new BlocksworldDomain(init, goal));
	}

	@Test
	public void blocksworld6() {
		Fact[] init = BlocksworldDomain
				.stacksToFacts("I,H,G", "F,E,D", "C,B,A");
		Fact[] goal = BlocksworldDomain
				.stacksToFacts("G,H,I", "D,E,F", "A,B,C");
		search(new BlocksworldDomain(init, goal));
	}

	@Test
	public void blocksworld7() {
		Fact[] init = BlocksworldDomain.stacksToFacts("J,K,L,M", "E,F,G,H,I",
				"A,B,C,D");
		Fact[] goal = BlocksworldDomain.stacksToFacts("A,B,C,D,G");
		search(new BlocksworldDomain(init, goal));
	}

	@Test
	public void blocksworld8() {
		Fact[] init = BlocksworldDomain.stacksToFacts("J,K,L,M", "E,F,G,H,I",
				"A,B,C,D");
		Fact[] goal = BlocksworldDomain.stacksToFacts("A,B,C,D,E");
		search(new BlocksworldDomain(init, goal));
	}

	@Test
	public void blocksworld9() {
		Fact[] init = BlocksworldDomain.stacksToFacts("J,K,L,M", "E,F", "A,B");
		Fact[] goal = BlocksworldDomain.stacksToFacts("J,E,A", "M,L,K", "F,B");
		search(new BlocksworldDomain(init, goal));
	}

	private void search(PlannerInput input) {
		
		log.info("Test {} is running ...", name.getMethodName());
		log.info("Planning input:");
		log.info(" Initial state:");
		for (Fact f : input.getInitialState().getFacts()) {
			log.info("  {}", f);
		}
		log.info(" Goal:");
		log.info("  {}", input.getGoal().asCondition());

		
		Searcher searcher = new BestFirstPlanner(input);
		List<GroundAction> list;
		try {
			Plan plan = searcher.search();
			list = plan.getActions();
			printActions(list);
		} catch (NoSolutionException e) {
			log.error("No solution!");
			log.error("Open nodes: {}", ((BestFirstSearchReport) e
					.getSearchReport()).openNodes().size());
			log.error("Closed nodes: {}", ((BestFirstSearchReport) e
					.getSearchReport()).closedNodes().size());
			Assert.assertTrue(false);
		}
	}

	private void printActions(List<GroundAction> list) {
		log.info("Action list size is {}", list.size());
		for (GroundAction a : list) {
			log.info(":: {}", a.getAction());
		}
	}
}
