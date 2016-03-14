package harmony.lod.operator;

import harmony.core.api.fact.Fact;
import harmony.core.api.goal.Goal;
import harmony.core.api.operator.Action;
import harmony.core.api.operator.Operator;
import harmony.core.api.operator.OperatorException;
import harmony.core.api.plan.Plan;
import harmony.core.api.property.Property;
import harmony.core.api.renderer.Renderer;
import harmony.core.api.thing.Thing;
import harmony.core.impl.fact.BasicFact;
import harmony.core.impl.goal.GoalImpl;
import harmony.core.impl.renderer.RendererImpl;
import harmony.core.impl.state.InitialState;
import harmony.lod.model.api.dataset.OutputDataset;
import harmony.lod.model.api.dataset.SourceDataset;
import harmony.lod.model.api.slice.Frame;
import harmony.lod.model.api.slice.StatementTemplate;
import harmony.lod.model.api.symbol.IRI;
import harmony.lod.model.api.symbol.Literal;
import harmony.lod.model.impl.dataset.OutputDatasetImpl;
import harmony.lod.model.impl.dataset.SourceDatasetImpl;
import harmony.lod.model.impl.slice.FrameException;
import harmony.lod.model.impl.slice.FrameImpl;
import harmony.lod.model.impl.slice.StatementTemplateImpl;
import harmony.lod.model.impl.symbol.DatatypeImpl;
import harmony.lod.model.impl.symbol.IRIImpl;
import harmony.lod.model.impl.symbol.LiteralImpl;
import harmony.lod.property.Extracted;
import harmony.lod.property.HasDefaultRange;
import harmony.lod.property.HasDomain;
import harmony.lod.property.HasSlice;
import harmony.lod.property.Linked;
import harmony.lod.property.NeededSlice;
import harmony.lod.property.Renewed;
import harmony.planner.NoSolutionException;
import harmony.planner.PlannerInput;
import harmony.planner.bestfirst.BestFirstPlanner;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TypifyTest {
	SourceDataset src = null;
	OutputDataset out = null;
	StatementTemplate st = null;
	IRI property = null;
	IRI type = null;
	IRI type2 = null;
	PlannerInput input = null;
	Renderer rendy = new RendererImpl();
	Literal defaultValue = null;
	Frame frame = null;

	Logger log = LoggerFactory.getLogger(getClass());

	@Before
	public void setup() throws FrameException {

		src = new SourceDatasetImpl("src");
		out = new OutputDatasetImpl("out");
		property = new IRIImpl("property");
		type = new IRIImpl("type");
		type2 = new IRIImpl("type2");
		st = new StatementTemplateImpl(null, property);
		IRI rdfType = new IRIImpl(
				"http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
		IRI xsdInt = new IRIImpl(
				"http://www.w3.org/2001/XMLSchema#integer");
		defaultValue = new LiteralImpl("0", new DatatypeImpl(xsdInt));
		frame = new FrameImpl(st,
				new StatementTemplateImpl(null, rdfType, type));

		input = new PlannerInput() {

			public InitialState getInitialState() {
				Fact[] initial = new Fact[] {
						new BasicFact(new HasSlice(), src, st),
						new BasicFact(new HasDefaultRange(), property,
								defaultValue),
						new BasicFact(new HasDomain(), property, type) 
						};
				return new InitialState(initial, new Thing[]{});
			}

			public Operator[] getOperators() {
				return new Operator[] { new Extract(), new Link(), new Renew(),
						new Typify(), new Append(), new Filter() };
			}

			public Property[] getProperty() {
				return new Property[] { new HasSlice(), new Extracted(),
						new Linked(), new Renewed(), new HasDomain(),
						new HasDefaultRange(), new NeededSlice() };
			}

			public Goal getGoal() {
				Fact[] goal = new Fact[] {
				// new BasicFact(new Extracted(), st),
				new BasicFact(new HasSlice(), out, frame)
				//new BasicFact(new HasDomain(), property, type) 
				};
				return new GoalImpl(goal);
			}

			@Override
			public Thing[] getObjects() {
				return new Thing[] { st, src, out, property, type, defaultValue };
			}
		};
	}

	@Test
	public void test() throws OperatorException {
		
		BestFirstPlanner p = new BestFirstPlanner(input);
		Plan pl;
		try {
			pl = p.search();
			for (Action a : pl.getActions()) {
				log.info(rendy.append(a).toString());
			}
		} catch (NoSolutionException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		Assert.assertTrue(true);
	}
}
