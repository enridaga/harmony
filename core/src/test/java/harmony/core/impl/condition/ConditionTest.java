package harmony.core.impl.condition;

import harmony.core.api.condition.Condition;
import harmony.core.impl.condition.And;
import harmony.core.impl.condition.AssertFact;
import harmony.core.impl.condition.Bool;
import harmony.core.impl.condition.Equality;
import harmony.core.impl.condition.Not;
import harmony.core.impl.condition.Or;
import harmony.core.impl.condition.When;
import harmony.core.impl.fact.BasicFact;
import harmony.core.impl.property.BasicProperty;
import harmony.core.impl.thing.Something;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class ConditionTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testCompareTo() {

		Condition c3 = new AssertFact(new BasicFact(new BasicProperty("aProperty"),
				new Something("a"), new Something("a")));
		Condition c0 = new Bool(true);
		Condition c1 = new Not(new Bool(false));
		Condition c2 = new Equality(new Something("boh"), new Something("ariboh"));
		Condition c4 = new And().append(c0);
		Condition c5 = new When(c0, c1, c2);
		Condition c6 = new And().append(c3).append(c0);
		
		And a = new And();
		a.append(c3);
		a.append(c1);
		a.append(c5);
		a.append(c0);
		a.append(c2);
		a.append(c6);
		a.append(c4);
		
		List<Condition> cl = a.asList();
		
		Assert.assertTrue(cl.indexOf(c0) == 0);
		Assert.assertTrue(cl.indexOf(c1) == 1);
		Assert.assertTrue(cl.indexOf(c2) == 2);
		Assert.assertTrue(cl.indexOf(c3) == 3);
		Assert.assertTrue(cl.indexOf(c4) == 4);
		Assert.assertTrue(cl.indexOf(c5) == 5);
		Assert.assertTrue(cl.indexOf(c6) == 6);

		Or o = new Or();
		o.append(c4);
		o.append(c3);
		o.append(c0);
		o.append(c2);
		o.append(c1);
		o.append(c5);
		o.append(c6);
		
		List<Condition> co = o.asList();

		Assert.assertTrue(co.indexOf(c0) == 0);
		Assert.assertTrue(co.indexOf(c1) == 1);
		Assert.assertTrue(co.indexOf(c2) == 2);
		Assert.assertTrue(co.indexOf(c3) == 3);
		Assert.assertTrue(co.indexOf(c4) == 4);
		Assert.assertTrue(co.indexOf(c5) == 5);
		Assert.assertTrue(co.indexOf(c6) == 6);
	}
}
