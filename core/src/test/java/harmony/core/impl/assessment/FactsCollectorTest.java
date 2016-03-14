package harmony.core.impl.assessment;

import harmony.core.api.condition.Condition;
import harmony.core.api.fact.Fact;
import harmony.core.api.property.Property;
import harmony.core.api.renderer.Renderer;
import harmony.core.api.thing.Thing;
import harmony.core.impl.condition.And;
import harmony.core.impl.condition.AssertFact;
import harmony.core.impl.condition.Not;
import harmony.core.impl.condition.Or;
import harmony.core.impl.condition.When;
import harmony.core.impl.fact.BasicFact;
import harmony.core.impl.property.BasicProperty;
import harmony.core.impl.renderer.RendererImpl;
import harmony.core.impl.state.StaticState;
import harmony.core.impl.thing.Something;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FactsCollectorTest {

	Logger log = LoggerFactory.getLogger(FactsCollectorTest.class);

	@SuppressWarnings("unchecked")
	Property p1 = new BasicProperty("p1", Thing.class);
	@SuppressWarnings("unchecked")
	Property p2 = new BasicProperty("p2", Thing.class);
	@SuppressWarnings("unchecked")
	Property p3 = new BasicProperty("p3", Thing.class);
	@SuppressWarnings("unchecked")
	Property p4 = new BasicProperty("p4", Thing.class);
	@SuppressWarnings("unchecked")
	Property p5 = new BasicProperty("p5", Thing.class);
	@SuppressWarnings("unchecked")
	Property p6 = new BasicProperty("p6", Thing.class);

	Thing m1 = new Something("m1");
	Thing m2 = new Something("m2");
	Thing m3 = new Something("m3");
	Thing m4 = new Something("m4");
	Thing m5 = new Something("m5");
	Thing m6 = new Something("m6");

	Fact f1 = new BasicFact(p1, m1);
	Fact f2 = new BasicFact(p2, m2);
	Fact f3 = new BasicFact(p3, m3);
	Fact f4 = new BasicFact(p4, m4);
	Fact f5 = new BasicFact(p5, m5);
	Fact f6 = new BasicFact(p6, m6);

	/*
	 * UTILITIES
	 */

	private void logReport(FactsCollector colly) {
		Renderer rendy = new RendererImpl();
		if (!colly.getShared().isEmpty()) {
			log.info("Shared:");
			for (Fact f : colly.getShared()) {
				log.info(" {}", rendy.append(f).toString());
			}
		}
		if (!colly.getIgnored().isEmpty()) {
			log.info("Ignored:");
			for (Fact f : colly.getIgnored()) {
				log.info(" {}", rendy.append(f).toString());
			}
		}
		if (!colly.getMissing().isEmpty()) {
			log.info("Missing:");
			for (Fact f : colly.getMissing()) {
				log.info(" {}", rendy.append(f).toString());
			}
		}
		if (!colly.getNegated().isEmpty()) {
			log.info("Negated:");
			for (Fact f : colly.getNegated()) {
				log.info(" {}", rendy.append(f).toString());
			}
		}
		if (!colly.getNegatedMissing().isEmpty()) {
			log.info("Negated missing:");
			for (Fact f : colly.getNegatedMissing()) {
				log.info(" {}", rendy.append(f).toString());
			}
		}
	}

	private FactsCollector build(Fact[] facts) {
		// 
		StaticState s = new StaticState(facts, new Thing[0]);
		FactsCollector colly = new FactsCollector(s);
		return colly;
	}

	private void assertTest(FactsCollector colly, int shared, int ignored,
			int missing, int negated, int negMissing) {
		logReport(colly);
		Assert.assertTrue(colly.getIgnored().size() == ignored);
		Assert.assertTrue(colly.getShared().size() == shared);
		Assert.assertTrue(colly.getMissing().size() == missing);
		Assert.assertTrue(colly.getNegated().size() == negated);
		Assert.assertTrue(colly.getNegatedMissing().size() == negMissing);
	}

	/*
	 * TESTS
	 */

	@Test
	public void test1() {
		log.info("test1()");

		Fact[] facts = { f1 };

		Condition c = new AssertFact(f1);

		FactsCollector colly = build(facts);
		boolean result = c.accept(colly);

		assertTest(colly, 1, 0, 0, 0, 0);

		Assert.assertTrue(result);
	}

	@Test
	public void test2() {
		log.info("test2()");

		Fact[] facts = { f1, f2 };

		Condition c = new AssertFact(f1);

		FactsCollector colly = build(facts);
		boolean result = c.accept(colly);

		assertTest(colly, 1, 1, 0, 0, 0);

		Assert.assertTrue(result);
	}

	@Test
	public void test3() {
		log.info("test3()");

		Fact[] facts = { f1, f2 };

		And c = new And();
		c.append(new AssertFact(f1));
		c.append(new AssertFact(f2));

		FactsCollector colly = build(facts);
		boolean result = c.accept(colly);

		assertTest(colly, 2, 0, 0, 0, 0);

		Assert.assertTrue(result);
	}

	@Test
	public void test4() {
		log.info("test4()");

		Fact[] facts = { f1, f2 };

		And c = new And();
		c.append(new AssertFact(f1));
		c.append(new AssertFact(f3));

		FactsCollector colly = build(facts);
		boolean invalid = c.accept(colly);

		// A fact is missing
		assertTest(colly, 1, 1, 1, 0, 0);
		Assert.assertFalse(invalid);
	}

	@Test
	public void test5() {
		log.info("test5");

		Fact[] facts = { f1, f2, f4 };

		And c = new And();
		c.append(new AssertFact(f1));
		c.append(new AssertFact(f3));
		c.append(new Not(new AssertFact(f4)));

		FactsCollector colly = build(facts);
		boolean result = c.accept(colly);

		assertTest(colly, 1, 1, 1, 1, 0);

		Assert.assertFalse(result);

	}

	@Test
	public void test6() {
		log.info("test6");

		Fact[] facts = { f1, f2, f4 };

		And c = new And();
		c.append(new AssertFact(f1));
		c.append(new AssertFact(f3));
		c.append(new Not(new AssertFact(f4)));

		FactsCollector colly = build(facts);
		boolean result = c.accept(colly);

		assertTest(colly, 1, 1, 1, 1, 0);

		Assert.assertFalse(result);
	}

	@Test
	public void test7() {
		log.info("test7");
		Fact[] facts = { f1, f2, f4 };
		FactsCollector colly = build(facts);

		And c = new And();
		c.append(new AssertFact(f1));
		c.append(new Or().append(new AssertFact(f3)).append(new AssertFact(f4)));

		boolean result = c.accept(colly);

		assertTest(colly, 2, 1, 0, 0, 0);

		Assert.assertTrue(result);
	}

	@Test
	public void test8() {
		log.info("test8");
		Fact[] facts = { f1, f2, f3, f4 };
		FactsCollector colly = build(facts);

		Or c = new Or();
		c.append(new AssertFact(f1));
		c.append(new Or().append(new AssertFact(f3)).append(new AssertFact(f4)));

		boolean result = c.accept(colly);

		assertTest(colly, 3, 1, 0, 0, 0);

		Assert.assertTrue(result);
	}

	@Test
	public void test9() {
		log.info("test9");
		Fact[] facts = { f1 };
		FactsCollector colly = build(facts);

		Or c = new Or();
		c.append(new Not(new AssertFact(f3)));
		c.append(new Not(new AssertFact(f4)));
		c.append(new Or().append(new AssertFact(f1)).append(new AssertFact(f2)));

		boolean result = c.accept(colly);

		assertTest(colly, 1, 0, 0, 0, 2);

		Assert.assertTrue(result);
	}

	@Test
	public void test10() {
		log.info("test10");
		Fact[] facts = { f1, f2, f3, f4 };
		FactsCollector colly = build(facts);

		And c = new And();
		c.append(new Not(new AssertFact(f5)));
		c.append(new Not(new AssertFact(f6)));
		c.append(new AssertFact(f1));
		c.append(new AssertFact(f2));
		c.append(new AssertFact(f3));
		c.append(new AssertFact(f4));

		boolean result = c.accept(colly);

		assertTest(colly, 4, 0, 0, 0, 2);

		Assert.assertTrue(result);
	}

	@Test
	public void test11() {
		log.info("test11");
		Fact[] facts = { f1, f2 };
		FactsCollector colly = build(facts);

		And c = new And();
		c.append(new Not(new AssertFact(f5)));
		c.append(new Not(new AssertFact(f6)));
		c.append(new Not(new AssertFact(f1)));
		c.append(new AssertFact(f2));
		c.append(new AssertFact(f3));
		c.append(new AssertFact(f4));

		boolean result = c.accept(colly);

		assertTest(colly, 1, 0, 2, 1, 2);

		Assert.assertFalse(result);
	}

	@Test
	public void test12() {
		log.info("test12");
		Fact[] facts = { f1, f2, f3 };
		FactsCollector colly = build(facts);

		And c = new And();
		c.append(new Not(new AssertFact(f5)));
		Or o = new Or().append(new Not(new AssertFact(f6)));
		o.append(new Not(new AssertFact(f1)));
		c.append(o);
		c.append(new AssertFact(f2));
		c.append(new AssertFact(f4));

		boolean result = c.accept(colly);
		// since there is an OR condition, negation of f1 is ignored
		assertTest(colly, 1, 2, 1, 0, 2);

		Assert.assertFalse(result);
	}

	@Test
	public void test13() {
		log.info("test13");
		Fact[] facts = { f1, f2, f3, f4, f5 };
		FactsCollector colly = build(facts);

		And c = new And();
		c.append(new Not(new AssertFact(f5)));
		Or o = new Or().append(new Not(new AssertFact(f6)));
		o.append(new Not(new AssertFact(f1)));
		c.append(o);
		c.append(new AssertFact(f2));
		c.append(new AssertFact(f4));

		boolean result = c.accept(colly);
		// since there is an OR condition, negation of f1 is ignored
		assertTest(colly, 2, 2, 0, 1, 1);

		Assert.assertFalse(result);
	}

	@Test
	public void test14() {
		
		log.info("test14");
		Fact[] facts = { f1, f2, f3 };
		FactsCollector colly = build(facts);

		And c = new And();
		c.append(new When(new AssertFact(f1), new Not(new AssertFact(f6))));
		c.append(new AssertFact(f2));
		
		boolean result = c.accept(colly);
		
		assertTest(colly, 2, 1, 0, 0, 1);

		Assert.assertTrue(result);
	}

}
