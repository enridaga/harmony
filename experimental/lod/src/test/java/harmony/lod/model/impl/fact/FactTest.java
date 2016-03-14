package harmony.lod.model.impl.fact;

import harmony.core.api.fact.Fact;
import harmony.core.api.property.Property;
import harmony.core.impl.fact.BasicFact;
import harmony.core.impl.property.BasicProperty;
import harmony.lod.model.api.dataset.SourceDataset;
import harmony.lod.model.api.slice.Slice;
import harmony.lod.model.impl.dataset.SourceDatasetImpl;
import harmony.lod.model.impl.slice.StatementTemplateImpl;
import harmony.lod.model.impl.symbol.IRIImpl;
import junit.framework.Assert;

import org.junit.Test;

public class FactTest {


	@Test
	public void equals() {
		@SuppressWarnings("unchecked")
		Property p1 = new BasicProperty("p1", SourceDataset.class, Slice.class);
		SourceDataset sourceDataset = new SourceDatasetImpl("src");
		Slice slice = new StatementTemplateImpl(null, new IRIImpl(
				"http://www.example.org/predicate"));
		Fact fact = new BasicFact(p1, sourceDataset, slice);
		Fact fact2 = new BasicFact(p1, sourceDataset, slice);

		Assert.assertTrue(fact.equals(fact2));
	}

	
}
