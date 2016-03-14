package harmony.dsl.expression;

import junit.framework.Assert;
import harmony.dsl.thingloader.ThingTypeCreator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThingTypeCreatorTest {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Rule
	public TestName testName = new TestName();

	@Test
	public void test() {
		log.info(testName.getMethodName());
		ThingTypeCreator jtc = new ThingTypeCreator(
				ThingTypeCreator.KIND_INTERFACE, "name.subname.Typename");
		Assert.assertTrue(jtc.getCanonicalName()
				.equals("name.subname.Typename"));
		Assert.assertTrue(jtc.getName().equals("Typename"));
		Assert.assertTrue(jtc.getPackage().equals("name.subname"));
	}
}
