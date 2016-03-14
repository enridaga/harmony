package harmony.planner.impl;

import harmony.core.api.fact.Fact;
import harmony.core.api.fact.FactRegistry;
import harmony.core.api.property.Property;
import harmony.core.impl.fact.BasicFact;
import harmony.core.impl.fact.FactRegistryImpl;
import harmony.core.impl.property.BasicProperty;
import harmony.lod.model.api.dataset.SourceDataset;
import harmony.lod.model.api.slice.Frame;
import harmony.lod.model.api.slice.Slice;
import harmony.lod.model.api.slice.StatementTemplate;
import harmony.lod.model.impl.dataset.SourceDatasetImpl;
import harmony.lod.model.impl.slice.FrameImpl;
import harmony.lod.model.impl.slice.SliceException;
import harmony.lod.model.impl.slice.StatementTemplateImpl;
import harmony.lod.model.impl.symbol.IRIImpl;
import harmony.lod.model.impl.symbol.LangImpl;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FactRegistryImplTest {

	Logger log = LoggerFactory.getLogger(this.getClass());

	private FactRegistry registry = null;

	@Before
	public void before() {
		registry = new FactRegistryImpl();
	}

	@Test
	public void put() {
		log.info("put()");
		@SuppressWarnings("unchecked")
		Property p1 = new BasicProperty("p1", SourceDataset.class, Slice.class);
		SourceDataset sourceDataset = new SourceDatasetImpl("src");
		Slice slice = new StatementTemplateImpl();
		Fact fact = new BasicFact(p1, sourceDataset, slice);

		boolean added = registry.put(fact);
		Assert.assertTrue(added);

		Fact fact2 = new BasicFact(p1, sourceDataset, slice);
		boolean addedAgain = registry.put(fact2);
		Assert.assertFalse(addedAgain);
	}

	@Test
	public void getFacts() {
		log.info("getFacts(Property property)");
		@SuppressWarnings("unchecked")
		Property p1 = new BasicProperty("p1", SourceDataset.class, Slice.class);
		SourceDataset sourceDataset = new SourceDatasetImpl("src");

		Slice slice = new StatementTemplateImpl(null, new IRIImpl(
				"http://www.example.org/predicate"), new LangImpl("en"));
		Slice slice1 = new StatementTemplateImpl(null, new IRIImpl(
				"http://www.example.org/predicate1"));
		Slice slice2 = new StatementTemplateImpl(null, new IRIImpl(
				"http://www.example.org/predicate2"));

		Fact fact = new BasicFact(p1, sourceDataset, slice);
		Fact fact1 = new BasicFact(p1, sourceDataset, slice1);
		Fact fact2 = new BasicFact(p1, sourceDataset, slice2);

		registry.put(fact);
		registry.put(fact1);
		registry.put(fact2);

		List<Fact> facts = registry.getFacts(p1);

		Assert.assertTrue(facts.size() == 3);
		Assert.assertTrue(facts.get(0).equals(fact));
		Assert.assertTrue(facts.get(1).equals(fact1));
		Assert.assertTrue(facts.get(2).equals(fact2));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getFacts2() throws SliceException {
		log.info("getFacts(Thing model)");

		Property p1 = new BasicProperty("p1", SourceDataset.class, Slice.class);
		Property p2 = new BasicProperty("p2", SourceDataset.class, Slice.class);
		Property p3 = new BasicProperty("p3", SourceDataset.class, Frame.class);
		SourceDataset sourceDataset = new SourceDatasetImpl("src");

		Slice slice = new StatementTemplateImpl();
		Frame frame = new FrameImpl((StatementTemplate) slice);

		Fact fact = new BasicFact(p1, sourceDataset, slice);
		Fact fact1 = new BasicFact(p2, sourceDataset, slice);
		Fact fact2 = new BasicFact(p3, sourceDataset, frame);

		registry.put(fact);
		registry.put(fact1);
		registry.put(fact2);

		List<Fact> facts = registry.getFacts(slice);

		Assert.assertTrue(facts.size() == 2);
		Assert.assertTrue(facts.get(0).getThing(1).equals(slice));
		Assert.assertTrue(facts.get(1).getThing(1).equals(slice));
		Assert.assertFalse(facts.get(0).getThing(0).equals(slice));
		Assert.assertFalse(facts.get(1).getThing(0).equals(slice));

	}

	@SuppressWarnings("unchecked")
	@Test
	public void remove() throws SliceException {
		log.info("getFacts(Thing model)");

		Property p1 = new BasicProperty("p1", SourceDataset.class, Slice.class);
		Property p2 = new BasicProperty("p2", SourceDataset.class, Slice.class);
		Property p3 = new BasicProperty("p3", SourceDataset.class, Frame.class);
		SourceDataset sourceDataset = new SourceDatasetImpl("src");

		Slice slice = new StatementTemplateImpl();

		Frame frame = new FrameImpl((StatementTemplate) slice);
		
		Fact fact = new BasicFact(p1, sourceDataset, slice);
		Fact fact1 = new BasicFact(p2, sourceDataset, slice);
		Fact fact2 = new BasicFact(p3, sourceDataset, frame);

		registry.put(fact);
		registry.put(fact1);
		registry.put(fact2);

		Assert.assertTrue(registry.size() == 3);

		registry.remove(fact2);
		Assert.assertFalse(registry.contains(fact2));
		Assert.assertTrue(registry.size() == 2);

		registry.remove(fact1);
		Assert.assertFalse(registry.contains(fact1));
		Assert.assertTrue(registry.size() == 1);

		Assert.assertTrue(registry.contains(fact));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void loadAndSearchFacts(){
		long start = System.currentTimeMillis();
		log.info("loadAndSearchFacts()");
		for(int x = 0; x<10001; x++){
			Property p = new BasicProperty("p" + x, StatementTemplate.class);
			StatementTemplate s = new StatementTemplateImpl(null, new IRIImpl( "i" + x ));
			registry.put(new BasicFact(p,s));
		}
		log.info("loaded in: " + (System.currentTimeMillis() - start) + "ms");
		start = System.currentTimeMillis();
		Property p9999 = new BasicProperty("p9999", StatementTemplate.class);
		StatementTemplate s9999 = new StatementTemplateImpl(null, new IRIImpl( "i9999" ));
		Assert.assertTrue(registry.contains(new BasicFact(p9999, s9999)));
		Property p10000 = new BasicProperty("p10000", StatementTemplate.class);
		StatementTemplate s10000 = new StatementTemplateImpl(null, new IRIImpl( "i10000" ));
		Assert.assertTrue(registry.contains(new BasicFact(p10000, s10000)));
		Property p10001 = new BasicProperty("p10001", StatementTemplate.class);
		StatementTemplate s10001 = new StatementTemplateImpl(null, new IRIImpl( "i10001" ));
		Assert.assertFalse(registry.contains(new BasicFact(p10001, s10001)));
		log.info("searched in: " + (System.currentTimeMillis() - start) + "ms");
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void contains(){
		FactRegistryImpl registry2 = new FactRegistryImpl();
		for(int x = 0; x<10; x++){
			Property p = new BasicProperty("p" + x, StatementTemplate.class);
			StatementTemplate s = new StatementTemplateImpl(null, new IRIImpl( "i" + x ));
			registry.put(new BasicFact(p,s));
			registry2.put(new BasicFact(p,s));
		}

		Assert.assertTrue(registry.contains(registry2.asList()));
		Assert.assertTrue(registry.contains(registry2));
		Assert.assertTrue(registry.contains(registry2.toArray()));
	}
}
