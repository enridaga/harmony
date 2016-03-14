package harmony.core.impl.property;

import harmony.core.api.property.Property;
import harmony.core.api.thing.Thing;
import harmony.core.impl.property.BasicProperty;

import org.junit.Assert;
import org.junit.Test;

public class BasicPropertyTest {

	@SuppressWarnings("unchecked")
	@Test
	public void equals() {
		Property p1 = new BasicProperty("property", MyModel.class);
		Property p2 = new BasicProperty("property", MyModel.class);
		Assert.assertTrue(p1.equals(p2));
		Assert.assertTrue(p1.getArgTypes().equals(p2.getArgTypes()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void equalArgs() {
		Property p1 = new BasicProperty("property", MyModel.class);
		Property p2 = new BasicProperty("property", MyModel.class);
		Assert.assertTrue(p1.getArgTypes().equals(p2.getArgTypes()));
	}

	class MyModel implements Thing {

		private String signature = null;

		public MyModel(String signature) {

		}

		@Override
		public String getSignature() {
			return signature;
		}
	}
}
