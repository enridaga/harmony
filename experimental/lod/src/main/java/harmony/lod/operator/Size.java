package harmony.lod.operator;

import harmony.core.api.condition.Condition;
import harmony.core.api.effect.Effect;
import harmony.core.api.operator.OperatorException;
import harmony.core.api.thing.Thing;
import harmony.core.impl.condition.And;
import harmony.core.impl.condition.AssertFact;
import harmony.core.impl.condition.Exists;
import harmony.core.impl.condition.Not;
import harmony.core.impl.effect.BasicEffect;
import harmony.core.impl.fact.BasicFact;
import harmony.core.impl.operator.AbstractOperator;
import harmony.lod.model.api.dataset.TempDataset;
import harmony.lod.model.api.slice.StatementTemplate;
import harmony.lod.model.api.symbol.Datatype;
import harmony.lod.model.api.symbol.IRI;
import harmony.lod.model.impl.slice.StatementTemplateImpl;
import harmony.lod.model.impl.symbol.DatatypeImpl;
import harmony.lod.model.impl.symbol.IRIImpl;
import harmony.lod.property.HasSlice;
import harmony.lod.property.IsSizable;
import harmony.lod.property.Renewed;

public class Size extends AbstractOperator {

	@SuppressWarnings("unchecked")
	public Size() {
		super("Size", TempDataset.class, StatementTemplate.class, IRI.class);
	}

	private StatementTemplate sizefy(StatementTemplate sizeable, IRI predicate) {
		Datatype datatype = new DatatypeImpl(new IRIImpl(
				"http://www.w3.org/2001/XMLSchema#int"));
		return new StatementTemplateImpl(sizeable.getSubject(), predicate,
				datatype);
	}

	@Override
	public Condition getPrecondition(Thing... things)
			throws OperatorException {
		And and = new And();
		and.append(new AssertFact(new BasicFact(new HasSlice(), things[0],
				things[1])));
		and.append(new AssertFact().append(new BasicFact(new Renewed(),
				things[0])));
		// The statement is sizable?
		and.append(new AssertFact(new BasicFact(new IsSizable(),((StatementTemplate) things[1]).getPredicate(), things[2])));
		final StatementTemplate sized = sizefy((StatementTemplate) things[1],
				(IRI) things[2]);
		// Do the given dataset already contains the sized st?
		and.append(new Not(new AssertFact(new BasicFact(new HasSlice(),
				things[0], sized))));
		// Do exists a dataset which contains the sized st?
		@SuppressWarnings("unchecked")
		Exists exists = new Exists(TempDataset.class) {
			
			@Override
			public Condition getCondition(Thing... things) {
				return new AssertFact(new BasicFact(new HasSlice(), things[0], sized));
			}
		};
		and.append(new Not(exists));
		return and;
	}

	@Override
	public Effect getEffect(Thing... things) throws OperatorException {
		StatementTemplate sized = sizefy((StatementTemplate) things[1],
				(IRI) things[2]);
		BasicEffect effect = new BasicEffect();
		effect.toAdd(new BasicFact(new HasSlice(), things[0], sized));
		return effect;
	}
}
