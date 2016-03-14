package harmony.planner.impl;

import harmony.core.api.property.Property;
import harmony.core.api.property.PropertyRegistry;
import harmony.core.impl.property.BasicProperty;
import harmony.core.impl.property.NoargsProperty;
import harmony.core.impl.property.PropertyRegistryImpl;
import harmony.lod.model.api.dataset.SourceDataset;
import harmony.lod.model.api.slice.StatementTemplate;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyRegistryImplTest {

	Logger log = LoggerFactory.getLogger(this.getClass());

	PropertyRegistry propertyRegistry = null;

	@Before
	public void before() {
		propertyRegistry = new PropertyRegistryImpl();
	}

	@Test
	public void register() {
		log.info("register()");
		Property p1 = new NoargsProperty("example");
		propertyRegistry.register(p1);
		Assert.assertTrue(propertyRegistry.isRegistered(p1.getName()));
	}

	@Test
	public void unregister() {
		log.info("unregister()");
		Property p1 = new NoargsProperty("example");
		propertyRegistry.register(p1);
		propertyRegistry.unregister(p1.getName());
		Assert.assertFalse(propertyRegistry.isRegistered(p1.getName()));
	}

	@Test
	public void size() {
		log.info("size()");

		Assert.assertTrue(propertyRegistry.size() == 0);

		@SuppressWarnings("unchecked")
		Property p1 = new BasicProperty("p1", SourceDataset.class,
				StatementTemplate.class);
		propertyRegistry.register(p1);

		Assert.assertTrue(propertyRegistry.size() == 1);

		propertyRegistry.register(p1);
		Assert.assertTrue(propertyRegistry.size() == 1);

		@SuppressWarnings("unchecked")
		Property p2 = new BasicProperty("p2", SourceDataset.class,
				StatementTemplate.class);
		propertyRegistry.register(p2);
		Assert.assertTrue(propertyRegistry.size() == 2);

		propertyRegistry.unregister(p1.getName());
		Assert.assertTrue(propertyRegistry.size() == 1);

		propertyRegistry.unregister(p2.getName());
		Assert.assertTrue(propertyRegistry.size() == 0);
	}
}
