package harmony.dsl.expression;

import harmony.core.api.thing.Thing;
import harmony.core.impl.thing.Something;

import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExpressionToStringTest {

	Logger log = LoggerFactory.getLogger(getClass());

	@Rule
	public TestName testName = new TestName();

	@BeforeClass
	public static void beforeClass() {
		//ExpressionToStringStyle.STYLE = ToStringStyle.SHORT_PREFIX_STYLE;
	}

	@AfterClass
	public static void afterClass() {
		ExpressionToStringStyle.STYLE = ToStringStyle.MULTI_LINE_STYLE;

	}

	@After
	public void after() {
		log.info("{} completed.", testName.getMethodName());
	}

	@Test
	public void Declarations() {
		start();
		Declarations decl = new Declarations();
		empty(decl);
		decl.put("name1", Thing.class);
		populated(decl);
		test(decl, "Declarations", "name1",
				"interface harmony.core.api.thing.Thing");
	}

	@Test
	public void PropertyExpression() {
		start();
		PropertyExpression expr = new PropertyExpression();
		empty(expr);
		expr.setName("OnTable");
		Declarations decl = new Declarations();
		decl.put("A", Thing.class);
		expr.setDeclarations(decl);
		populated(expr);
		test(expr, " OnTable", "Declarations","[A]",
				"[interface harmony.core.api.thing.Thing");
	}

	@Test
	public void Scope() {
		start();
		Scope scope = new Scope();
		empty(scope);
		scope.put("?x", new Something("X"));
		scope.put("?y", new Something("Y"));
		scope.put("?z", new Something("Z"));
		scope.put("?q", new Something("Q"));
		populated(scope);
		test(scope, "X", "Y", "Z", "Q");
	}

	@Test
	public void FactExpression() {
		start();
		FactExpression expr = new FactExpression();
		empty(expr);
		@SuppressWarnings("unchecked")
		PropertyExpression pexpr = new PropertyExpression("OnTable",
				Thing.class);
		expr.setProperty(pexpr);
		expr.setVariables("A", "B");
		populated(expr);
		test(expr, "A", "B", "OnTable");
	}

	@Test
	public void AddFactEffectExpression() {
		start();
		AddFactEffectExpression expr = new AddFactEffectExpression();
		empty(expr);
		PropertyExpression property = new PropertyExpression();
		property.setName("On");
		Declarations decl = new Declarations();
		decl.put("?x", Thing.class);
		decl.put("?y", Thing.class);
		property.setDeclarations(decl);
		try {
			expr.set(property, "A", "B");
		} catch (AssertionExpressionException e) {
			log.error("Error while setting the assertion. ", e);
			Assert.assertTrue(false);
		}
		populated(expr);
	}

	@Test
	public void AssertFactConditionExpression() {
		start();
		AssertFactConditionExpression expr = new AssertFactConditionExpression();
		empty(expr);
		try {
			expr.set(OnPropertyExpression(), "Block1", "Block2");
			populated(expr);
		} catch (AssertionExpressionException e) {
			log.error("Failed", e);
			Assert.assertTrue(false);
		}
		test(expr, "On", "Declarations", "AssertFactCondition");
	}

	@Test
	public void AndConditionExpression() {
		start();
		AndConditionExpression expr = new AndConditionExpression();
		AssertFactConditionExpression cond1 = new AssertFactConditionExpression();
		AssertFactConditionExpression cond2 = new AssertFactConditionExpression();
		AssertFactConditionExpression cond3 = new AssertFactConditionExpression();
		try {
			cond1.set(OnPropertyExpression(), "X", "Y");
			cond2.set(OnPropertyExpression(), "Y", "Z");
			cond3.set(OnPropertyExpression(), "Z", "Q");
		} catch (AssertionExpressionException e) {
			log.error("Failed", e);
			Assert.assertTrue(false);
		}
		expr.append(cond1).append(cond2).append(cond3);
		populated(expr);

	}

	private PropertyExpression OnPropertyExpression() {
		PropertyExpression property = new PropertyExpression();
		property.setName("On");
		Declarations decl = new Declarations();
		decl.put("?x", Thing.class);
		decl.put("?y", Thing.class);
		property.setDeclarations(decl);
		return property;
	}

	private void start() {
		log.info("Testing {}", testName.getMethodName());
	}

	private void empty(Object expr) {
		log.info("Empty: {}", expr.toString());
	}

	private void populated(Object expr) {
		log.info("Populated: {}", expr.toString());
	}

	private void test(Object expr, String... strings) {
		for (String s : strings) {
			boolean val = expr.toString().contains(s);
			if (!val) {
				log.error("{} does not contains {}", expr, s);
			}
			Assert.assertTrue(val);
		}
	}
}
