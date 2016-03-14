package harmony.usecases;

import harmony.core.api.fact.Fact;
import harmony.core.api.goal.Goal;
import harmony.core.api.operator.Operator;
import harmony.core.api.property.Property;
import harmony.core.api.thing.Thing;
import harmony.core.impl.condition.And;
import harmony.core.impl.condition.AssertFact;
import harmony.core.impl.fact.BasicFact;
import harmony.core.impl.goal.GoalImpl;
import harmony.core.impl.state.InitialState;
import harmony.lod.factory.LOD;
import harmony.lod.model.api.dataset.OutputDataset;
import harmony.lod.model.api.dataset.SourceDataset;
import harmony.lod.model.api.slice.Frame;
import harmony.lod.model.api.slice.Path;
import harmony.lod.model.api.slice.StatementTemplate;
import harmony.lod.model.api.symbol.IRI;
import harmony.lod.model.impl.slice.FrameException;
import harmony.lod.model.impl.slice.PathException;
import harmony.lod.model.impl.symbol.IRIImpl;
import harmony.lod.operator.Append;
import harmony.lod.operator.Extract;
import harmony.lod.operator.Filter;
import harmony.lod.operator.Link;
import harmony.lod.operator.Merge;
import harmony.lod.operator.Renew;
import harmony.lod.operator.Revert;
import harmony.lod.operator.Shortcut;
import harmony.lod.operator.Size;
import harmony.lod.operator.Typify;
import harmony.lod.property.Extracted;
import harmony.lod.property.HasDefaultRange;
import harmony.lod.property.HasDomain;
import harmony.lod.property.HasSlice;
import harmony.lod.property.HasType;
import harmony.lod.property.IsRevertable;
import harmony.lod.property.IsShortable;
import harmony.lod.property.IsSizable;
import harmony.lod.property.Linked;
import harmony.lod.property.NeededSlice;
import harmony.lod.property.Renewed;
import harmony.planner.PlannerInput;

public final class TendersPopulation extends LOD implements PlannerInput {

	private SourceDataset loted;
	private StatementTemplate lotedRegion;

	private SourceDataset eurostat;
	private Path eurostatPath;

	private OutputDataset output;
	private StatementTemplate tenders;
	private StatementTemplate population;
	private StatementTemplate region;
	private IRI typeRegion;
	private Frame outputFrame;
	// private Frame untypedFrame;
	private IRI xsdInteger;

	public TendersPopulation() throws FrameException, PathException {

		loted = getDatasetFactory().getSourceDataset("loted");
		lotedRegion = getStatementTemplateFactory().getP("loted:region");

		eurostat = getDatasetFactory().getSourceDataset("eurostat");
		eurostatPath = getPathFactory().get(
				getStatementTemplateFactory().getP(
						"eurostat:numberOfCitizensInYear[year=2010]"),
				getStatementTemplateFactory().getPD(
						"eurostat:numberOfCitizens",
						"http://www.w3.org/2001/XMLSchema#int"));

		output = getDatasetFactory().getOutputDataset();
		tenders = getStatementTemplateFactory().getPD("output:tenders",
				"http://www.w3.org/2001/XMLSchema#int");
		population = getStatementTemplateFactory().getPD("output:population",
				"http://www.w3.org/2001/XMLSchema#int");
		typeRegion = getSymbolFactory().iri("output:Region");
		region = getStatementTemplateFactory().getOfType("output:Region");

		// untypedFrame = getFrameFactory().get(tenders, population);
		outputFrame = getFrameFactory().get(tenders, population, region);
		xsdInteger = new IRIImpl("http://www.w3.org/2001/XMLSchema#int");
	}

	@Override
	public InitialState getInitialState() {
		Fact[] initial = new Fact[] {
				new BasicFact(new HasSlice(), loted, lotedRegion),
				new BasicFact(new HasSlice(), eurostat, eurostatPath),
				new BasicFact(new IsShortable(), eurostatPath,
						getSymbolFactory().iri("output:population")),
				new BasicFact(new IsRevertable(), getSymbolFactory().iri(
						"loted:region"), getSymbolFactory().iri(":tender")),

				// new BasicFact(new IsOfType(), untypedFrame, typeRegion),
				new BasicFact(new HasDomain(), new IRIImpl("output:tenders"),
						typeRegion),
				new BasicFact(new HasDefaultRange(), new IRIImpl(
						"output:tenders"), xsdInteger),
				new BasicFact(new HasDomain(),
						new IRIImpl("output:population"), typeRegion),
				new BasicFact(new HasDefaultRange(), new IRIImpl(
						"output:population"), xsdInteger),

				new BasicFact(new IsSizable(), getSymbolFactory()
						.iri(":tender"), getSymbolFactory().iri(
						"output:tenders")) };
		return new InitialState(initial, getObjects());
	}

	@Override
	public Goal getGoal() {
		And and = new And();
		and.append(new AssertFact(new BasicFact(new HasSlice(), output,
				outputFrame)));
		return new GoalImpl(and);

	}

	@Override
	public Thing[] getObjects() {
		return new Thing[] { loted, lotedRegion, eurostat, eurostatPath,
				output, tenders, population, region, outputFrame, xsdInteger };
	}

	@Override
	public Operator[] getOperators() {
		return new Operator[] { new Extract(), new Filter(), new Shortcut(),
				new Append(), new Typify(), new Revert(), new Size(),
				new Link(), new Renew(), new Merge() };
	}

	@Override
	public Property[] getProperty() {
		return new Property[] { new HasSlice(), new HasType(),
				new IsShortable(), new IsRevertable(), new IsSizable(),
				new Extracted(), new NeededSlice(), new HasDomain(),
				new HasDefaultRange(),
				// new IsOfType(),
				new Linked(), new Renewed() };
	}
}
