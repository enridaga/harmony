package harmony.lod.operator;

import harmony.core.api.fact.Fact;
import harmony.core.api.goal.Goal;
import harmony.core.api.operator.Action;
import harmony.core.api.operator.Operator;
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
import harmony.lod.model.api.slice.Slice;
import harmony.lod.model.impl.dataset.OutputDatasetImpl;
import harmony.lod.model.impl.dataset.SourceDatasetImpl;
import harmony.lod.model.impl.slice.StatementTemplateImpl;
import harmony.lod.model.impl.symbol.IRIImpl;
import harmony.lod.property.Extracted;
import harmony.lod.property.HasSlice;
import harmony.lod.property.Linked;
import harmony.lod.property.Renewed;
import harmony.planner.NoSolutionException;
import harmony.planner.PlannerInput;
import harmony.planner.bestfirst.BestFirstPlanner;
import harmony.planner.bestfirst.BestFirstSearchReport;
import harmony.planner.bestfirst.Node;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppendTest {
	SourceDataset src = null;
	OutputDataset out = null;
	Slice slice = null;
	PlannerInput input = null;
	Renderer rendy = new RendererImpl();

	Logger log = LoggerFactory.getLogger(getClass());

	@Before
	public void setup() {

		src = new SourceDatasetImpl("src");
		slice = new StatementTemplateImpl(null, new IRIImpl("type"));
		out = new OutputDatasetImpl("my-output");
		final SourceDataset fsrc = src;
		final OutputDataset fout = out;
		final Slice fslice = slice;

		input = new PlannerInput() {

			public InitialState getInitialState() {
				Fact[] initial = new Fact[] { new BasicFact(new HasSlice(),
						fsrc, fslice) };
				return new InitialState(initial, new Thing[]{});
			}

			public Operator[] getOperators() {
				return new Operator[] { new Extract(), new Append() , new Link(), new Renew()};
			}

			public Property[] getProperty() {
				return new Property[] { new HasSlice(), new Extracted(), new Linked(), new Renewed() };
			}

			public Goal getGoal() {
				Fact[] goal = new Fact[] { new BasicFact(new HasSlice(), fout,
						fslice) };
				return new GoalImpl(goal);
			}

			@Override
			public Thing[] getObjects() {
				return new Thing[] { fout, fslice, fsrc };
			}
		};
	}

	@Test
	public void test() {
		BestFirstPlanner p = new BestFirstPlanner(input);
		Plan pl;
		try {
			pl = p.search();
			for (Action a : pl.getActions()) {
				log.info(rendy.append(a).toString());
			}
		} catch (NoSolutionException e) {
			Renderer r = new RendererImpl();
			
			log.error("No solution!");
			log.error("Closed: ");
			BestFirstSearchReport sr = (BestFirstSearchReport) e.getSearchReport();
			for (Node n : sr.closedNodes()) {
				log.error(" : {}", r.append(n).toString());
			}
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		Assert.assertTrue(true);
	}

}
