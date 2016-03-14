package harmony.lod.property;

import harmony.core.impl.assessment.Evaluator;
import harmony.core.impl.assessment.ScoredEvaluator;
import harmony.core.impl.condition.AssertFact;
import harmony.core.impl.fact.BasicFact;
import harmony.core.impl.state.StaticState;
import harmony.lod.model.api.dataset.TempDataset;
import harmony.lod.model.api.slice.Frame;
import harmony.lod.model.api.slice.StatementTemplate;
import harmony.lod.model.api.symbol.IRI;
import harmony.lod.model.impl.dataset.TempDatasetImpl;
import harmony.lod.model.impl.slice.FrameException;
import harmony.lod.model.impl.slice.FrameImpl;
import harmony.lod.model.impl.slice.StatementTemplateImpl;
import harmony.lod.model.impl.symbol.IRIImpl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HasTypeTest {

	StaticState state;
	TempDataset dataset;
	StatementTemplate typeSt;
	StatementTemplate aSt;
	Frame frame;

	IRI rdf_type;
	IRI aPredicate;
	IRI clas;

	@Before
	public void setup() throws FrameException {
		aPredicate = new IRIImpl("aPredicate");
		rdf_type = new IRIImpl(
				"http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
		clas = new IRIImpl("aClass");

		dataset = new TempDatasetImpl();
		typeSt = new StatementTemplateImpl(null, rdf_type, clas);
		aSt = new StatementTemplateImpl(null, aPredicate);
		frame = new FrameImpl(aSt, typeSt);

		state = new StaticState();
		state.add(new BasicFact(new HasSlice(), dataset, typeSt));
	}

	@Test
	public void testFrame() {
		Evaluator e = new Evaluator(state);
		AssertFact af = new AssertFact(new BasicFact(new HasType(), frame, clas));
		Assert.assertTrue(af.accept(e));
	}
	
	@Test
	public void testStatementTemplate() {
		Evaluator e = new Evaluator(state);
		AssertFact af = new AssertFact(new BasicFact(new HasType(), typeSt, clas));
		Assert.assertTrue(af.accept(e));
	}

	@Test
	public void testScoredFrame() {
		ScoredEvaluator e = new ScoredEvaluator(state, 100);
		AssertFact af = new AssertFact(new BasicFact(new HasType(), frame, clas));
		Assert.assertTrue(af.accept(e));
		Assert.assertTrue(e.getScore() == 100f);
	}
	
	@Test
	public void testScoredStatementTemplate() {
		ScoredEvaluator e = new ScoredEvaluator(state, 100);
		AssertFact af = new AssertFact(new BasicFact(new HasType(), typeSt, clas));
		Assert.assertTrue(af.accept(e));
		Assert.assertTrue(e.getScore() == 100f);
	}
}
