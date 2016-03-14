package harmony.dsl.expression;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TypesTest {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Rule
	public TestName testName = new TestName();

	@Test
	public void test() throws ClassNotFoundException {
		log.info(testName.getMethodName());
		Types types = new Types("test.types." + testName.getMethodName());
		types.addType("Pippo");
		Assert.assertNotNull(types.getTypeClass("Pippo"));
	}
}
