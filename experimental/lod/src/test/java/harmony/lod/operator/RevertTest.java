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
import harmony.lod.model.api.dataset.TempDataset;
import harmony.lod.model.api.slice.Path;
import harmony.lod.model.api.slice.StatementTemplate;
import harmony.lod.model.api.symbol.IRI;
import harmony.lod.model.impl.dataset.TempDatasetImpl;
import harmony.lod.model.impl.slice.PathException;
import harmony.lod.model.impl.slice.PathImpl;
import harmony.lod.model.impl.slice.StatementTemplateImpl;
import harmony.lod.model.impl.symbol.IRIImpl;
import harmony.lod.property.Extracted;
import harmony.lod.property.HasSlice;
import harmony.lod.property.IsRevertable;
import harmony.lod.property.IsShortable;
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

public class RevertTest {
	TempDataset tmp = null;
	Path path = null;
	IRI predicate = null;
	IRI reverted = null;
	PlannerInput input = null;
	Renderer rendy = new RendererImpl();

	Logger log = LoggerFactory.getLogger(getClass());

	@Before
	public void setup() throws PathException {
		tmp = new TempDatasetImpl();
		StatementTemplateImpl father = new StatementTemplateImpl(null,
				new IRIImpl("hasFather"));
		StatementTemplateImpl brother = new StatementTemplateImpl(null,
				new IRIImpl("hasBrother"));
		path = new PathImpl(father, brother);
		predicate = new IRIImpl("hasUncle");
		reverted = new IRIImpl("isUncleOf");

		final StatementTemplate isUncleOf = new StatementTemplateImpl(null,
				new IRIImpl("isUncleOf"));

		input = new PlannerInput() {
			@Override
			public Thing[] getObjects() {
				return new Thing[] { tmp, path, predicate, reverted };
			}

			@Override
			public InitialState getInitialState() {
				Fact[] initial = new Fact[] { new BasicFact(new HasSlice(), tmp, path),
						new BasicFact(new Extracted(), path),
						new BasicFact(new IsShortable(), path, predicate),
						new BasicFact(new IsRevertable(), predicate, reverted), };
				return new InitialState(initial, new Thing[]{});
			}

			@Override
			public Goal getGoal() {
				Fact[] facts = new Fact[] { new BasicFact(new HasSlice(), tmp,
						isUncleOf) };
				return new GoalImpl(facts);
			}

			@Override
			public Property[] getProperty() {
				return new Property[] { new HasSlice(), new IsShortable(),
						new IsRevertable(), new Linked(), new Renewed() };
			}

			@Override
			public Operator[] getOperators() {
				return new Operator[] { new Shortcut(), new Revert(),
						new Link(), new Renew() };
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
			e.printStackTrace();
			
			log.error("Closed");
			for (Node d : ((BestFirstSearchReport) e.getSearchReport()).closedNodes()) {
				rendy.append(d);
			}
			log.error(rendy.toString());

			log.error("Open");
			for (Node d : ((BestFirstSearchReport) e.getSearchReport()).openNodes()) {
				rendy.append(d);
			}
			log.error(rendy.toString());
			Assert.assertTrue(false);
		}
		Assert.assertTrue(true);
	}
}
