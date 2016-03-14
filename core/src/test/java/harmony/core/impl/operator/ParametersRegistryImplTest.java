package harmony.core.impl.operator;

import harmony.core.api.condition.Condition;
import harmony.core.api.effect.Effect;
import harmony.core.api.operator.Operator;
import harmony.core.api.operator.OperatorException;
import harmony.core.api.thing.Thing;
import harmony.core.impl.condition.Bool;
import harmony.core.impl.effect.CompositeEffectImpl;
import harmony.core.impl.operator.AbstractOperator;
import harmony.core.impl.operator.OperatorRegistryImpl;
import harmony.core.impl.parameters.ParametersRegistryImpl;
import harmony.core.impl.thing.Something;
import harmony.core.impl.thing.ThingRegistryImpl;

import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParametersRegistryImplTest {

	Logger log = LoggerFactory.getLogger(getClass());

	ParametersRegistryImpl parametersRegistry = null;
	ThingRegistryImpl modelRegistry = null;
	OperatorRegistryImpl operatorRegistry = null;

	Something m1 = null;
	Something m2 = null;
	Something m3 = null;

	Operator o1 = null;
	Operator o2 = null;
	Operator o3 = null;

	@SuppressWarnings("unchecked")
	@Before
	public void before() {
		modelRegistry = new ThingRegistryImpl();
		operatorRegistry = new OperatorRegistryImpl();

		parametersRegistry = new ParametersRegistryImpl(modelRegistry);

		m1 = new ModelA("m1");
		m2 = new ModelB("m2");
		m3 = new ModelC("m3");

		o1 = new UselessOperator("o1", ModelA.class, ModelB.class);
		o2 = new UselessOperator("o2", ModelA.class, ModelB.class, ModelC.class);
		o3 = new UselessOperator("o3", ModelB.class);

		operatorRegistry.register(o1);
		operatorRegistry.register(o2);
		operatorRegistry.register(o3);

		modelRegistry.put(m1);
		modelRegistry.put(m2);
		modelRegistry.put(m3);

	}

	@Test
	public void test1() {
		Iterator<List<Thing>> it = parametersRegistry.iterator(o1);
		boolean exists = false;
		while (it.hasNext()) {
			List<Thing> p = it.next();
			Assert.assertTrue(p.indexOf(m1) == 0);
			Assert.assertTrue(p.indexOf(m2) == 1);
			Assert.assertTrue(p.indexOf(m3) == -1);
			exists = true;
		}
		Assert.assertTrue(exists);
	}

	@Test
	public void test2() {
		Iterator<List<Thing>> it = parametersRegistry.iterator(o2);
		boolean exists = false;
		while (it.hasNext()) {
			List<Thing> p = it.next();
			Assert.assertTrue(p.indexOf(m1) == 0);
			Assert.assertTrue(p.indexOf(m2) == 1);
			Assert.assertTrue(p.indexOf(m3) == 2);
			exists = true;
		}
		Assert.assertTrue(exists);
	}

	@Test
	public void test3() {
		Iterator<List<Thing>> it = parametersRegistry.iterator(o3);
		boolean exists = false;
		while (it.hasNext()) {
			List<Thing> p = it.next();
			Assert.assertTrue(p.indexOf(m1) == -1);
			Assert.assertTrue(p.indexOf(m2) == 0);
			Assert.assertTrue(p.indexOf(m3) == -1);
			exists = true;
		}
		Assert.assertTrue(exists);
	}

	class ModelA extends Something {

		public ModelA(String signature) {
			super(signature);
		}
	}

	class ModelB extends Something {

		public ModelB(String signature) {
			super(signature);
		}
	}

	class ModelC extends Something {

		public ModelC(String signature) {
			super(signature);
		}
	}

	class UselessOperator extends AbstractOperator {

		public UselessOperator(String name,
				Class<? extends Thing>... parameters) {
			super(name, parameters);
		}

		@Override
		public Condition getPrecondition(Thing... things)
				throws OperatorException {
			return new Bool(true);
		}

		@Override
		public Effect getEffect(Thing... things) throws OperatorException {
			return new CompositeEffectImpl();
		}
	}
}
