package harmony.lod.operator;

import harmony.core.api.condition.Condition;
import harmony.core.api.effect.Effect;
import harmony.core.api.operator.OperatorException;
import harmony.core.api.thing.Thing;
import harmony.core.impl.condition.And;
import harmony.core.impl.condition.AssertFact;
import harmony.core.impl.condition.Exists;
import harmony.core.impl.condition.Not;
import harmony.core.impl.condition.Or;
import harmony.core.impl.effect.BasicEffect;
import harmony.core.impl.fact.BasicFact;
import harmony.core.impl.operator.AbstractOperator;
import harmony.lod.model.api.dataset.TempDataset;
import harmony.lod.model.api.slice.StatementTemplate;
import harmony.lod.model.api.symbol.IRI;
import harmony.lod.model.impl.slice.StatementTemplateImpl;
import harmony.lod.property.HasSlice;
import harmony.lod.property.IsRevertable;
import harmony.lod.property.Renewed;

public class Revert extends AbstractOperator {

	@SuppressWarnings("unchecked")
	public Revert() {
		super("Revert", TempDataset.class, StatementTemplate.class, IRI.class);
	}

	private StatementTemplate revert(StatementTemplate revertable, IRI predicate) {
		return new StatementTemplateImpl(revertable.getObject(), predicate,
				revertable.getSubject());
	}

	@Override
	public Condition getPrecondition(Thing... things)
			throws OperatorException {
		And and = new And();

		and.append(new AssertFact().append(new BasicFact(new Renewed(),
				things[0])));
		and.append(new AssertFact(new BasicFact(new HasSlice(), things[0],
				things[1])));
		
		// The statement is revertable
		and.append(new Or().append(
				new AssertFact(new BasicFact(new IsRevertable(), things[2],
						((StatementTemplate) things[1]).getPredicate())))
				.append(new AssertFact(new BasicFact(new IsRevertable(),
						((StatementTemplate) things[1]).getPredicate(),
						things[2]))));
		
		final StatementTemplate reverted = revert((StatementTemplate) things[1],
				(IRI) things[2]);
		and.append(new Not(new AssertFact(new BasicFact(new HasSlice(),
				things[0], reverted))));

		// Do exists a dataset which contains the sized st?
		@SuppressWarnings("unchecked")
		Exists exists = new Exists(TempDataset.class) {
			
			@Override
			public Condition getCondition(Thing... things) {
				return new AssertFact(new BasicFact(new HasSlice(), things[0], reverted));
			}
		};
		and.append(new Not(exists));
		return and;
	}

	@Override
	public Effect getEffect(Thing... things) throws OperatorException {
		StatementTemplate reverted = revert((StatementTemplate) things[1],
				(IRI) things[2]);
		BasicEffect effect = new BasicEffect();
		effect.toAdd(new BasicFact(new HasSlice(), things[0], reverted));
		return effect;
	}
}
