package harmony.integration;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class IntegrationTests {

	protected Logger log = LoggerFactory.getLogger(getClass());

	@Rule
	public TestName testName = new TestName();

	@Before
	public void after() {
		log.info("{} is running...", testName.getMethodName());
	}

	@After
	public void before() {
		log.info("{} completed.", testName.getMethodName());
	}
}
