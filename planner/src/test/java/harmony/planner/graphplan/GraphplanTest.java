package harmony.planner.graphplan;

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
import static harmony.planner.test.RocketDomain.Flowers1;
import static harmony.planner.test.RocketDomain.London;
import static harmony.planner.test.RocketDomain.Madrid;
import static harmony.planner.test.RocketDomain.Meat0;
import static harmony.planner.test.RocketDomain.Paris;
import static harmony.planner.test.RocketDomain.R0;
import static harmony.planner.test.RocketDomain.R1;
import static harmony.planner.test.RocketDomain.R2;
import static harmony.planner.test.RocketDomain.R3;
import static harmony.planner.test.RocketDomain.R4;
import static harmony.planner.test.RocketDomain.Rome;
import static harmony.planner.test.RocketDomain.at;
import static harmony.planner.test.RocketDomain.hasFuel;
import harmony.core.api.fact.Fact;
import harmony.core.api.operator.GroundAction;
import harmony.core.api.plan.Plan;
import harmony.core.api.thing.Thing;
import harmony.core.impl.fact.BasicFact;
import harmony.core.impl.property.BasicProperty;
import harmony.core.impl.thing.Something;
import harmony.planner.NoSolutionException;
import harmony.planner.PlannerInput;
import harmony.planner.test.BlocksworldDomain;
import harmony.planner.test.MoveUpDown;
import harmony.planner.test.RocketDomain;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphplanTest {
	final Logger log = LoggerFactory.getLogger(GraphplanTest.class);

	@Rule
	public TestName name = new TestName();

	@SuppressWarnings("unchecked")
	@Test
	public void testFactSets(){
		Fact f1 = new BasicFact(new BasicProperty("p1", Thing.class), new Something("a"));
		Fact f2 = new BasicFact(new BasicProperty("p2", Thing.class), new Something("b"));
		Fact f3 = new BasicFact(new BasicProperty("p3", Thing.class), new Something("c"));
		
		Fact c1 = new BasicFact(new BasicProperty("p1", Thing.class), new Something("a"));
		Fact c2 = new BasicFact(new BasicProperty("p2", Thing.class), new Something("b"));
		Fact c3 = new BasicFact(new BasicProperty("p3", Thing.class), new Something("c"));

		Set<Fact> setF = new HashSet<Fact>();
		setF.add(f1);
		setF.add(f2);
		setF.add(f3);

		Set<Fact> setC = new HashSet<Fact>();
		setC.add(c1);
		setC.add(c2);
		setC.add(c3);
		
		Assert.assertTrue(f1.equals(c1));
		
		Assert.assertTrue(setF.equals(setC));
		
		Set<Set<Fact>> gs = new HashSet<Set<Fact>>();
		gs.add(setF);
		Assert.assertTrue(gs.contains(setC));
		Assert.assertTrue(gs.contains(setF));
		
	}
	
	@Test
	public void rocket0() {
		Fact[] rinit = { RocketDomain.at(Fish0, Rome),
				RocketDomain.at(Meat0, Rome), RocketDomain.hasFuel(R0),
				RocketDomain.at(R0, Rome) };
		Fact[] rgoal = { RocketDomain.at(Fish0, London),
				RocketDomain.at(Meat0, London) };

		search(new RocketDomain(rinit, rgoal));
	}

	@Test
	public void moveupdown1() throws NoSolutionException {
		final Fact[] init = { on(cat, table), on(dog, table),
				on(barbie, table), on(horse, table), on(piggy, table),
				on(mouse, table), };

		final Fact[] goalf = { under(cat, table), under(dog, table),
				under(barbie, table), under(horse, table), under(piggy, table),
				under(mouse, table) };

		search(new MoveUpDown(init, goalf));
	}

	@Test
	public void rocket1() throws NoSolutionException {
		final Fact[] init = { at(R0, Rome), at(Fish0, Rome), at(Meat0, Rome),
				hasFuel(R0) };

		final Fact[] goalf = { at(Fish0, London), at(Meat0, London) };

		search(new RocketDomain(init, goalf));
	}

	@Test
	public void rocket2() throws NoSolutionException {
		final Fact[] init = { at(R0, Rome), at(Fish0, Rome), at(Meat0, Paris),
				hasFuel(R0), at(R1, Paris), hasFuel(R1) };

		final Fact[] goalf = { at(Fish0, London), at(Meat0, London) };

		search(new RocketDomain(init, goalf));
	}

	@Test
	public void rocket3() throws NoSolutionException {
		final Fact[] init = { at(R0, Rome), at(Fish0, Rome), at(Meat0, Paris),
				hasFuel(R0), at(R1, Paris), hasFuel(R1), at(R2, Rome),
				hasFuel(R2) };

		final Fact[] goalf = { at(Fish0, London), at(Meat0, London) };

		search(new RocketDomain(init, goalf));
	}

	@Test
	public void rocket4() throws NoSolutionException {
		final Fact[] init = { at(R0, Rome), at(Fish0, Rome), at(Meat0, Paris),
				hasFuel(R0), at(R1, Paris), hasFuel(R1), at(R2, Rome),
				hasFuel(R2), at(Flowers0, Amsterdam), at(R3, Amsterdam),
				hasFuel(R3) };

		final Fact[] goalf = { at(Fish0, London), at(Meat0, London),
				at(Flowers0, Paris) };

		search(new RocketDomain(init, goalf));
	}

	@Test
	public void rocket5() throws NoSolutionException {
		final Fact[] init = { 
				at(R0, Rome), 
				at(Fish0, Rome), 
				at(Meat0, Paris),
				hasFuel(R0),
				at(R1, Paris), 
				hasFuel(R1), 
				at(R2, Rome),
				hasFuel(R2), 
				at(Flowers0, Amsterdam), 
				at(R3, Amsterdam),
				hasFuel(R3),
				at(R4, Amsterdam),
				hasFuel(R4),
				at(Flowers1, Amsterdam)
				
		};

		final Fact[] goalf = { at(Fish0, London), at(Meat0, London),
				at(Flowers0, Paris), at(Flowers1, Madrid) };

		search(new RocketDomain(init, goalf));
	}
	

	@Test
	public void blocksworld1() throws NoSolutionException {
		Fact[] init = BlocksworldDomain.stacksToFacts("A,B,C");
		Fact[] goal = BlocksworldDomain.stacksToFacts("C,B,A");
		search(new BlocksworldDomain(init, goal));
	}
//
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

	//@Test
	public void blocksworld6() {
		Fact[] init = BlocksworldDomain
				.stacksToFacts("I,H,G", "F,E,D", "C,B,A");
		Fact[] goal = BlocksworldDomain
				.stacksToFacts("G,H,I", "D,E,F", "A,B,C");
		search(new BlocksworldDomain(init, goal));
	}

	//@Test
	public void blocksworld7() {
		Fact[] init = BlocksworldDomain.stacksToFacts("J,K,L,M", "E,F,G,H,I",
				"A,B,C,D");
		Fact[] goal = BlocksworldDomain.stacksToFacts("A,B,C,D,G");
		search(new BlocksworldDomain(init, goal));
	}

	//@Test
	public void blocksworld8() {
		Fact[] init = BlocksworldDomain.stacksToFacts("J,K,L,M", "E,F,G,H,I",
				"A,B,C,D");
		Fact[] goal = BlocksworldDomain.stacksToFacts("A,B,C,D,E");
		search(new BlocksworldDomain(init, goal));
	}

	//@Test
	public void blocksworld9() {
		Fact[] init = BlocksworldDomain.stacksToFacts("J,K,L,M", "E,F", "A,B");
		Fact[] goal = BlocksworldDomain.stacksToFacts("J,E,A", "M,L,K", "F,B");
		search(new BlocksworldDomain(init, goal));
	}
	
	//@Test
	public void blocksworld10() throws NoSolutionException {
		Fact[] init = BlocksworldDomain.stacksToFacts("J,K,L,M", "E,F,G,H,I",
				"A,B,C,D");
		Fact[] goal = BlocksworldDomain.stacksToFacts("A,B,C,D,E,F");
		search(new BlocksworldDomain(init, goal));
	}
	
	//@Test
	public void blocksworld11() {
		Fact[] init = BlocksworldDomain.stacksToFacts("J,K,L,M", "E,F,G,H,I", "A,B,C,D");
		Fact[] goal = BlocksworldDomain.stacksToFacts("J,E,A", "K,L,M", "F,G,H,I", "B,C,D");
		search(new BlocksworldDomain(init, goal));
	}

	public void search(PlannerInput input) {
		log.info("Test {} is running ...", name.getMethodName());
		log.info("Planning input:");
		log.info(" Initial state:");
		for (Fact f : input.getInitialState().getFacts()) {
			log.info("  {}", f);
		}
		log.info(" Goal:");
		log.info("  {}", input.getGoal().asCondition());

		int precompute = 100;
		Graphplan gp = new Graphplan(input, precompute);
		PlanningGraph pg = gp.getGraph();
		if(pg.goalFound()){
			precompute = pg.getLast().step();
		}
		log.info("Goal found with {} computed steps: {}", precompute, pg.goalFound());
		log.info("No solution discovered in {} precomputed steps? {}", precompute, pg.noSolution());

		try {
			Plan plan = gp.search();
			log.info("Plan size: {}", plan.size());
			for (GroundAction ga : plan.getActions()) {
				log.info("  {}", ga.getAction());

			}
			Assert.assertTrue(true);
		} catch (NoSolutionException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		log.info("... {} finished", name.getMethodName());
		
		Assert.assertTrue(pg.goalFound());
		Assert.assertFalse(pg.noSolution());
	}
}
