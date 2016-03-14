package harmony.core.impl.fact;

import harmony.core.api.fact.Fact;
import harmony.core.api.property.Property;
import harmony.core.api.thing.Thing;
import harmony.core.impl.fact.BasicFact;
import harmony.core.impl.property.BasicProperty;
import junit.framework.Assert;

import org.junit.Test;

public class BasicFactTest {

	@Test
	public void equals() {
		@SuppressWarnings("unchecked")
		Property p1 = new BasicProperty("p1", MyModel.class, MyModel2.class);
		Thing m1 = new MyModel("aModel");
		Thing m2 = new MyModel2("anotherModel");
		Fact fact = new BasicFact(p1, m1, m2);
		Fact fact2 = new BasicFact(p1, m1, m2);

		Assert.assertTrue(fact.equals(fact2));
	}

	
	@Test
	public void equalthings() {
		@SuppressWarnings("unchecked")
		Property p1 = new BasicProperty("p1", MyModel.class, MyModel2.class);
		Thing m1 = new MyModel("aModel");
		Thing m2 = new MyModel2("anotherModel");
		Fact fact = new BasicFact(p1, m1, m2);
		Fact fact2 = new BasicFact(p1, m1, m2);

		Assert.assertTrue(fact.getThings().equals(fact2.getThings()));
	}

	class MyModel implements Thing {

		private String signature = null;

		public MyModel(String signature) {
			this.signature = signature;
		}

		@Override
		public String getSignature() {
			return signature;
		}
	}

	class MyModel2 implements Thing {

		private String signature = null;

		public MyModel2(String signature) {
			this.signature = signature + "2";
		}

		@Override
		public String getSignature() {
			return signature;
		}
	}
}
