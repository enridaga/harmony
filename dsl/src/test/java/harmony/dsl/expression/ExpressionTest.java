package harmony.dsl.expression;

import harmony.core.api.renderer.Renderer;
import harmony.core.api.thing.Thing;
import harmony.core.impl.condition.And;
import harmony.core.impl.condition.AssertFact;
import harmony.core.impl.condition.Or;
import harmony.core.impl.renderer.RendererImpl;
import harmony.core.impl.thing.Something;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExpressionTest {

	Logger log = LoggerFactory.getLogger(getClass());

	private PropertyExpression p1;
	private PropertyExpression p2;
	private PropertyExpression p3;
	private Thing a1;
	private Thing a2;
	private Thing b1;
	private Thing b2;
	private Thing b3;

	@SuppressWarnings("unchecked")
	@Before
	public void before() {
		p1 = new PropertyExpression("p1", TypeA.class);
		p2 = new PropertyExpression("p2", TypeA.class, TypeB.class);
		p3 = new PropertyExpression("p3", TypeA.class, TypeB.class, Thing.class);

		a1 = new TypeA("1");
		a2 = new TypeA("2");
		b1 = new TypeB("1");
		b2 = new TypeB("2");
		b3 = new TypeB("3");
	}

	@Test
	public void testConstructor1() {
		log.info("testConstructor1()");
		try {
			new AssertFactConditionExpression().set(p1, "a");
		} catch (AssertionExpressionException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		Assert.assertTrue(true);
	}

	@Test
	public void testConstructor2() {
		log.info("testConstructor2()");
		try {
			new AssertFactConditionExpression().set(p1, "a", "b");
		} catch (AssertionExpressionException e) {
			log.info("Expected exception: ", e);
			Assert.assertTrue(true);
			return;
		}
		Assert.assertTrue(false);
	}

	@Test
	public void testConstructor3() {
		log.info("testConstructor3()");
		try {
			new AssertFactConditionExpression().set(p1);
		} catch (AssertionExpressionException e) {
			Assert.assertTrue(true);
			return;
		}
		Assert.assertTrue(false);
	}

	@Test
	public void testArgTypesVarSize2() throws AssertionExpressionException {
		log.info("testArgTypesVarSize()");
		AssertFactConditionExpression ass1 = new AssertFactConditionExpression();
		ass1.set(p1, "x");
		Assert.assertEquals(0, ass1.declaresVariables().size());
		AssertFactConditionExpression ass2 = new AssertFactConditionExpression();
		ass2.set(p2, "x", "y");
		Assert.assertEquals(0, ass2.declaresVariables().size());
		AssertFactConditionExpression ass3 = new AssertFactConditionExpression();
		ass3.set(p3, "x", "y", "z");
		Assert.assertEquals(0, ass3.declaresVariables().size());

	}

	@Test
	public void testBindings() throws AssertionExpressionException,
			UnboundVariableException, IncorrectBindingException {
		log.info("testBindings()");

		AssertFactConditionExpression ass1 = new AssertFactConditionExpression();
		ass1.set(p1, "x");
		Scope bindings = new Scope();
		bindings.put("x", a1);
		bindings.put("y", a2);
		bindings.put("z", b3);

		AssertFact condition = ass1.eval(bindings);
		log.info(" {} ", condition);
	}

	@Test
	public void testBindings2() throws AssertionExpressionException,
			UnboundVariableException, IncorrectBindingException {
		log.info("testBindings2()");

		AndConditionExpression and = new AndConditionExpression();
		AssertFactConditionExpression ass1 = new AssertFactConditionExpression();
		ass1.set(p1, "x");
		AssertFactConditionExpression ass2 = new AssertFactConditionExpression();
		ass2.set(p2, "x", "z");
		and.append(ass1);
		and.append(ass2);

		Scope bindings = new Scope();
		bindings.put("x", a1);
		bindings.put("y", a2);
		bindings.put("z", b3);

		And condition = and.eval(bindings);

		Renderer rendy = new RendererImpl();
		String asString = rendy.append(condition).toString();
		log.info("Condition: \n {}", asString);
		Assert.assertEquals("(?(And (Assert (p1 A_1)) (Assert (p2 A_1 B_3))))",
				asString);
	}

	@Test
	public void testBindings3() throws AssertionExpressionException,
			UnboundVariableException, IncorrectBindingException {
		log.info("testBindings3()");

		AndConditionExpression and = new AndConditionExpression();
		AssertFactConditionExpression ass1 = new AssertFactConditionExpression();
		ass1.set(p1, "x");
		AssertFactConditionExpression ass2 = new AssertFactConditionExpression();
		ass2.set(p2, "x", "z");
		and.append(ass1);
		and.append(ass2);

		AndConditionExpression and2 = new AndConditionExpression();
		AssertFactConditionExpression ass3 = new AssertFactConditionExpression();
		ass3.set(p1, "y");
		AssertFactConditionExpression ass4 = new AssertFactConditionExpression();
		ass4.set(p2, "y", "z");
		and2.append(ass3);
		and2.append(ass4);

		OrConditionExpression or = new OrConditionExpression();
		or.append(and);
		NotConditionExpression not = new NotConditionExpression();
		not.setCondition(and2);
		or.append(not);

		Scope bindings = new Scope();
		bindings.put("x", a1);
		bindings.put("y", a2);
		bindings.put("z", b3);

		Or condition = or.eval(bindings);

		Renderer rendy = new RendererImpl();

		String asString = rendy.append(condition).toString();
		log.info("Condition: \n {}", asString);
		Assert.assertEquals(
				"(?(Or (And (Assert (p1 A_1)) (Assert (p2 A_1 B_3))) (Not (And (Assert (p1 A_2)) (Assert (p2 A_2 B_3))))))",
				asString);
	}

	@Test
	public void testBindings4() throws AssertionExpressionException,
			UnboundVariableException, IncorrectBindingException,
			ClassNotFoundException {
		log.info("testBindings4()");

		AndConditionExpression and = new AndConditionExpression();
		AssertFactConditionExpression ass1 = new AssertFactConditionExpression();
		ass1.set(p1, "x");
		AssertFactConditionExpression ass2 = new AssertFactConditionExpression();
		ass2.set(p2, "x", "z");
		and.append(ass1);
		and.append(ass2);

		AndConditionExpression and2 = new AndConditionExpression();
		EqualityConditionExpression ass3 = new EqualityConditionExpression();
		ass3.setup("x", "x2");
		TypeConditionExpression ass4 = new TypeConditionExpression();
		ass4.set("x", TypeA.class.getName());
		and2.append(ass3);
		and2.append(ass4);

		OrConditionExpression or = new OrConditionExpression();
		or.append(and);
		NotConditionExpression not = new NotConditionExpression();
		not.setCondition(and2);
		or.append(not);

		Scope bindings = new Scope();
		bindings.put("x", a1);
		bindings.put("y", a2);
		bindings.put("z", b3);
		bindings.put("x2", a1);

		Or condition = or.eval(bindings);

		Renderer rendy = new RendererImpl();

		String asString = rendy.append(condition).toString();
		log.info("Condition: \n {}", asString);
		Assert.assertEquals(
				"(?(Or (And (Assert (p1 A_1)) (Assert (p2 A_1 B_3))) (Not (And (Type A_1 harmony.dsl.expression.ExpressionTest$TypeA) (Eq A_1 A_1)))))",
				asString);
	}

	@Test
	public void testBindings5() throws AssertionExpressionException,
			UnboundVariableException, IncorrectBindingException,
			ClassNotFoundException {
		log.info("testBindings5()");

		AndConditionExpression and = new AndConditionExpression();
		AssertFactConditionExpression ass1 = new AssertFactConditionExpression();
		ass1.set(p3, "x", "z", "j");

		WhenConditionExpression ass2 = new WhenConditionExpression();
		ass2.setWhen(new AssertFactConditionExpression().set(p1, "x"));
		ass2.setThen(new AssertFactConditionExpression().set(p2, "x", "z"));
		WhenConditionExpression ass3 = new WhenConditionExpression();
		ass3.setWhen(new AssertFactConditionExpression().set(p1, "y"));
		ass3.setThen(new AssertFactConditionExpression().set(p2, "y", "q"));
		ass3.setOtherwise(new AssertFactConditionExpression().set(p2, "y", "j"));

		and.append(ass1);
		and.append(ass2);
		and.append(ass3);

		Scope bindings = new Scope();
		bindings.put("x", a1);
		bindings.put("y", a2);
		bindings.put("z", b1);
		bindings.put("q", b2);
		bindings.put("j", b3);

		And condition = and.eval(bindings);

		Renderer rendy = new RendererImpl();

		String asString = rendy.append(condition).toString();
		log.info("Condition: \n {}", asString);
		Assert.assertEquals(
				"(?(And (Assert (p3 A_1 B_1 B_3)) (When (Assert (p1 A_1)) (Assert (p2 A_1 B_1))) (When (Assert (p1 A_2)) (Assert (p2 A_2 B_2)) (Assert (p2 A_2 B_3)))))",
				asString);
	}

	class TypeA extends Something {

		public TypeA(String signature) {
			super("A_" + signature);
		}
	}

	class TypeB extends Something {

		public TypeB(String signature) {
			super("B_" + signature);
		}
	}

}
