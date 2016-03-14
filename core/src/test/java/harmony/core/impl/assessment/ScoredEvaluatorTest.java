package harmony.core.impl.assessment;

import harmony.core.api.fact.Fact;
import harmony.core.api.goal.Goal;
import harmony.core.api.property.Property;
import harmony.core.api.state.State;
import harmony.core.api.thing.Thing;
import harmony.core.impl.assessment.ScoredEvaluator;
import harmony.core.impl.condition.And;
import harmony.core.impl.condition.AssertFact;
import harmony.core.impl.condition.Not;
import harmony.core.impl.condition.Or;
import harmony.core.impl.fact.BasicFact;
import harmony.core.impl.goal.GoalImpl;
import harmony.core.impl.property.BasicProperty;
import harmony.core.impl.state.StaticState;
import harmony.core.impl.thing.Something;

import org.junit.Assert;
import org.junit.Test;

public class ScoredEvaluatorTest {

	@Test
	public void testANDFull() {
		// Properties
		@SuppressWarnings("unchecked")
		Property friendOf = new BasicProperty("Friend", Thing.class,
				Thing.class);

		// things
		Thing enrico = new Something("enrico");
		Thing alessandro = new Something("alessandro");
		Thing andrea = new Something("andrea");
		Thing carlo = new Something("carlo");
		Fact[] facts = new Fact[] {
				new BasicFact(friendOf, enrico, alessandro),
				new BasicFact(friendOf, enrico, andrea),
				new BasicFact(friendOf, enrico, carlo),
				new BasicFact(friendOf, carlo, alessandro),
				new BasicFact(friendOf, carlo, enrico),
				new BasicFact(friendOf, carlo, andrea) };
		Thing[] things = {enrico, alessandro, andrea, carlo};
		State state = new StaticState(facts, things);

		And andC = new And();
		for (Fact f : facts)
			andC.append(new AssertFact(f));

		ScoredEvaluator e = new ScoredEvaluator(state, 100f);

		Assert.assertTrue(andC.accept(e));
		Assert.assertTrue(e.getScore() == 100f);
	}

	@Test
	public void testANDPartial() {
		// Properties
		@SuppressWarnings("unchecked")
		Property friendOf = new BasicProperty("Friend", Thing.class,
				Thing.class);

		// things
		Thing enrico = new Something("enrico");
		Thing alessandro = new Something("alessandro");
		Thing andrea = new Something("andrea");
		Thing carlo = new Something("carlo");
		Fact[] facts = new Fact[] {
				new BasicFact(friendOf, enrico, alessandro),
				new BasicFact(friendOf, enrico, andrea),
				new BasicFact(friendOf, enrico, carlo), };
		Thing[] things = {enrico, alessandro, andrea, carlo};
		State state = new StaticState(facts, things);

		And andC = new And();
		for (Fact f : facts)
			andC.append(new AssertFact(f));

		Fact[] untrue = new Fact[] {
				new BasicFact(friendOf, carlo, alessandro),
				new BasicFact(friendOf, carlo, enrico),
				new BasicFact(friendOf, carlo, andrea) };
		for (Fact f : untrue)
			andC.append(new AssertFact(f));

		ScoredEvaluator e = new ScoredEvaluator(state, 100f);

		Assert.assertFalse(andC.accept(e));
		Assert.assertTrue(e.getScore() == 50f);
	}

	@Test
	public void testANDPartial2() {
		// Properties
		@SuppressWarnings("unchecked")
		Property friendOf = new BasicProperty("Friend", Thing.class,
				Thing.class);

		// things
		Thing enrico = new Something("enrico");
		Thing alessandro = new Something("alessandro");
		Thing andrea = new Something("andrea");
		Thing carlo = new Something("carlo");
		Fact[] facts = new Fact[] {
				new BasicFact(friendOf, enrico, alessandro),
				new BasicFact(friendOf, enrico, andrea),
				new BasicFact(friendOf, enrico, carlo), };
		Thing[] things = {enrico, alessandro, andrea, carlo};
		State state = new StaticState(facts, things);

		And andC = new And();
		for (Fact f : facts)
			andC.append(new AssertFact(f));

		Fact[] untrue = new Fact[] {
				new BasicFact(friendOf, carlo, alessandro),
				new BasicFact(friendOf, carlo, enrico),
				new BasicFact(friendOf, carlo, andrea),
				new BasicFact(friendOf, alessandro, carlo),
				new BasicFact(friendOf, alessandro, enrico),
				new BasicFact(friendOf, alessandro, andrea),
				new BasicFact(friendOf, andrea, carlo)

		};
		for (Fact f : untrue)
			andC.append(new AssertFact(f));

		ScoredEvaluator e = new ScoredEvaluator(state, 100f);

		Assert.assertFalse(andC.accept(e));
		Assert.assertTrue(e.getScore() == 30f);
	}

	@Test
	public void testANDORPartial() {
		// Properties
		@SuppressWarnings("unchecked")
		Property friendOf = new BasicProperty("Friend", Thing.class,
				Thing.class);

		// things
		Thing enrico = new Something("enrico");
		Thing alessandro = new Something("alessandro");
		Thing andrea = new Something("andrea");
		Thing carlo = new Something("carlo");
		Fact[] facts = new Fact[] {
				new BasicFact(friendOf, enrico, alessandro),
				new BasicFact(friendOf, enrico, andrea),
				new BasicFact(friendOf, enrico, carlo), };
		Thing[] things = {enrico, alessandro, andrea, carlo};
		State state = new StaticState(facts, things);

		And andC = new And();
		for (Fact f : facts)
			andC.append(new AssertFact(f));

		Or orC = new Or();
		Fact[] untrue = new Fact[] {
				new BasicFact(friendOf, carlo, alessandro),
				new BasicFact(friendOf, carlo, enrico),
				new BasicFact(friendOf, carlo, andrea),
				new BasicFact(friendOf, alessandro, carlo),
				new BasicFact(friendOf, alessandro, enrico),
				new BasicFact(friendOf, alessandro, andrea),
				new BasicFact(friendOf, andrea, carlo)

		};
		for (Fact f : untrue) {
			orC.append(new AssertFact(f));
		}
		And container = new And();
		container.append(andC).append(orC);

		ScoredEvaluator e = new ScoredEvaluator(state, 100f);

		Assert.assertFalse(container.accept(e));
		Assert.assertTrue(e.getScore() == 50f);
	}

	@Test
	public void testANDNOTPartial() {
		// Properties
		@SuppressWarnings("unchecked")
		Property friendOf = new BasicProperty("Friend", Thing.class,
				Thing.class);

		// things
		Thing enrico = new Something("enrico");
		Thing alessandro = new Something("alessandro");
		Thing andrea = new Something("andrea");
		Thing carlo = new Something("carlo");
		Fact[] facts = new Fact[] {
				new BasicFact(friendOf, enrico, alessandro),
				new BasicFact(friendOf, enrico, andrea),
				new BasicFact(friendOf, enrico, carlo), };
		Thing[] things = {enrico, alessandro, andrea, carlo};
		State state = new StaticState(facts, things);

		And andC = new And();
		for (Fact f : facts)
			andC.append(new AssertFact(f));

		And and2C = new And();
		Fact[] untrue = new Fact[] {
				new BasicFact(friendOf, carlo, alessandro),
				new BasicFact(friendOf, carlo, enrico),
				new BasicFact(friendOf, carlo, andrea),
				new BasicFact(friendOf, alessandro, carlo),
				new BasicFact(friendOf, alessandro, enrico),
				new BasicFact(friendOf, alessandro, andrea),
				new BasicFact(friendOf, enrico, carlo),

		};
		for (Fact f : untrue) {
			and2C.append(new Not(new AssertFact(f)));
		}
		And container = new And();
		container.append(andC).append(and2C);

		ScoredEvaluator e = new ScoredEvaluator(state, 100f);

		Assert.assertFalse(container.accept(e));
		Assert.assertTrue(e.getScore() == 92.85715f);
	}

	@Test
	public void testPartial() {
		// Properties
		@SuppressWarnings("unchecked")
		Property friendOf = new BasicProperty("Friend", Thing.class,
				Thing.class);

		// things
		Thing enrico = new Something("enrico");
		Thing alessandro = new Something("alessandro");
		Thing andrea = new Something("andrea");
		Thing carlo = new Something("carlo");
		Fact[] facts = new Fact[] {
				new BasicFact(friendOf, enrico, alessandro),
				new BasicFact(friendOf, enrico, andrea),
				new BasicFact(friendOf, enrico, carlo), };
		Thing[] things = {enrico, alessandro, andrea, carlo};
		State state = new StaticState(facts, things);

		And andC = new And();
		andC.append(new Not(new AssertFact(new BasicFact(friendOf, enrico,
				carlo))));
		andC.append(new AssertFact(new BasicFact(friendOf, enrico, alessandro)));
		andC.append(new AssertFact(new BasicFact(friendOf, enrico, andrea)));

		And container = new And();
		container.append(andC);

		ScoredEvaluator e = new ScoredEvaluator(state, 100f);

		Assert.assertFalse(container.accept(e));
		Assert.assertTrue(e.getScore() == 66.666664f);
	}

	@Test
	public void compareWithFactsCount() {
		// Properties
		@SuppressWarnings("unchecked")
		Property friendOf = new BasicProperty("Friend", Thing.class,
				Thing.class);

		// things
		Thing enrico = new Something("enrico");
		Thing alessandro = new Something("alessandro");
		Thing andrea = new Something("andrea");
		Thing carlo = new Something("carlo");
		Fact[] facts = new Fact[] {
				new BasicFact(friendOf, enrico, alessandro),
				new BasicFact(friendOf, enrico, andrea),
				new BasicFact(friendOf, enrico, carlo), };
		Thing[] things = {enrico, alessandro, andrea, carlo};
		State state = new StaticState(facts, things);

		Fact[] gfacts = new Fact[] {
				new BasicFact(friendOf, enrico, alessandro),
				new BasicFact(friendOf, enrico, andrea),
				new BasicFact(friendOf, enrico, carlo), };

		int factsInGoal = 0;
		for (Fact f : gfacts) {
			if (state.getFactRegistry().contains(f)) {
				factsInGoal++;
			}
		}
		int gFactSize = gfacts.length;
		int distance = gFactSize - factsInGoal;
		int factor = distance * 100 / gFactSize;
		
		Assert.assertTrue(factor == 0);
		
		ScoredEvaluator se = new ScoredEvaluator(state, 100);
		Goal g = new GoalImpl(gfacts);
		g.asCondition().accept(se);
		factor = 100 - new Float(se.getScore()).intValue();
		Assert.assertTrue(factor == 0);
		
		
	}
}
