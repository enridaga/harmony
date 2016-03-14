package harmony.core.impl.assessment;

import harmony.core.api.fact.Fact;
import harmony.core.api.property.Property;
import harmony.core.api.state.State;
import harmony.core.api.thing.Thing;
import harmony.core.impl.assessment.Evaluator;
import harmony.core.impl.condition.And;
import harmony.core.impl.condition.AssertFact;
import harmony.core.impl.fact.BasicFact;
import harmony.core.impl.property.BasicProperty;
import harmony.core.impl.state.StaticState;
import harmony.core.impl.thing.Something;

import org.junit.Assert;
import org.junit.Test;

public class EvaluatorTest {

	@Test
	public void testAND() {
		// Properties
		@SuppressWarnings("unchecked")
		Property friendOf = new BasicProperty("Friend", Thing.class, Thing.class);

		// things
		Thing enrico = new Something("enrico");
		Thing alessandro = new Something("alessandro");
		Thing andrea = new Something("andrea");
		Thing carlo = new Something("carlo");
		Fact[] facts = new Fact[] { 
				new BasicFact(friendOf, enrico, alessandro),
				new BasicFact(friendOf, enrico, andrea),
				new BasicFact(friendOf, enrico, carlo),
				new BasicFact(friendOf, carlo, alessandro),
				new BasicFact(friendOf, carlo, enrico),
				new BasicFact(friendOf, carlo, andrea)
			};
		Thing[] things = {enrico, alessandro, andrea, carlo}; 
		State state = new StaticState(facts, things);
		
		And andC = new And();
		for(Fact f : facts){
			andC.append(new AssertFact(f));
		}
		Evaluator e = new Evaluator(state);
		
		Assert.assertTrue(andC.accept(e));
	}
}
