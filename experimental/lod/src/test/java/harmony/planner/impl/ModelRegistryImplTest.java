package harmony.planner.impl;

import harmony.core.api.thing.Thing;
import harmony.core.api.thing.ThingRegistry;
import harmony.core.impl.thing.ThingRegistryImpl;
import harmony.lod.model.api.dataset.Dataset;
import harmony.lod.model.api.dataset.SourceDataset;
import harmony.lod.model.api.dataset.TempDataset;
import harmony.lod.model.api.slice.Slice;
import harmony.lod.model.impl.dataset.SourceDatasetImpl;
import harmony.lod.model.impl.dataset.TempDatasetImpl;
import harmony.lod.model.impl.slice.StatementTemplateImpl;
import harmony.lod.model.impl.symbol.IRIImpl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelRegistryImplTest {

	Logger log = LoggerFactory.getLogger(this.getClass());

	ThingRegistry thingRegistry = null;

	long started = 0;

	int sourceDataset = 10;

	int tempDataset = 200;

	int slice = 5000;

	@Before
	public void before() {
		log.info("Prepare...");
		started = System.currentTimeMillis();
		thingRegistry = new ThingRegistryImpl();
		log.info("Loading {} of SourceDataset", sourceDataset);
		for (int x = 0; x < sourceDataset; x++) {
			thingRegistry.put(new SourceDatasetImpl("src" + x));
		}
		log.info("Loading {} of TempDataset", tempDataset);
		for (int x = 0; x < tempDataset; x++) {
			thingRegistry.put(new TempDatasetImpl());
		}
		log.info("Loading {} of StatementTemplate", slice);
		for (int x = 0; x < slice; x++) {
			thingRegistry.put(new StatementTemplateImpl(null, new IRIImpl("p"
					+ x)));
		}
		log.info("Starting...");
	}

	@After
	public void after() {
		log.info("... done in {} ms",
				((System.currentTimeMillis() - started) / 1000));
		started = 0;
	}

	@Test
	public void put() {
		log.info("put()");
		Thing o = new TempDatasetImpl();
		boolean added = thingRegistry.put(o);
		Assert.assertTrue(added);
		boolean notAdded = thingRegistry.put(o);
		Assert.assertFalse(notAdded);
	}

	@Test
	public void get() {
		log.info("get()");
		Thing o = new TempDatasetImpl();
		thingRegistry.put(o);
		boolean contained = thingRegistry.get(TempDataset.class).contains(o);
		Assert.assertTrue(contained);
	}

	@Test
	public void size() {
		log.info("size()");
		Assert.assertTrue(slice + tempDataset + sourceDataset == thingRegistry
				.size());
		log.info("size: " + thingRegistry.size());
	}

	@Test
	public void sizeOf() {
		Assert.assertTrue(sourceDataset == thingRegistry
				.sizeOf(SourceDataset.class));
		log.info("size of SourceDataset: " + sourceDataset);

		Assert.assertTrue(tempDataset == thingRegistry
				.sizeOf(TempDataset.class));
		log.info("size of TempDataset: " + tempDataset);

		Assert.assertTrue(tempDataset + sourceDataset == thingRegistry
				.sizeOf(Dataset.class));
		log.info("size of Dataset: " + (tempDataset + sourceDataset));

		Assert.assertTrue(slice == thingRegistry.sizeOf(Slice.class));
		log.info("size of Slice: " + slice);
	}

	@Test
	public void contains() {
		log.info("contains()");
		Thing o = new Thing() {

			public String getSignature() {
				return "dummy";
			}
		};
		Class<?> c = o.getClass();
		log.info("Put object of type " + c.getName());
		thingRegistry.put(o);
		boolean containedObject = thingRegistry.contains(o);
		boolean containedClass = thingRegistry.contains(c);
		Assert.assertTrue(containedObject);
		Assert.assertTrue(containedClass);
	}
}
