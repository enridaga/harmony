package harmony.lod.operator;

import harmony.core.api.condition.Condition;
import harmony.core.api.effect.CompositeEffect;
import harmony.core.api.effect.Effect;
import harmony.core.api.operator.OperatorException;
import harmony.core.api.thing.Thing;
import harmony.core.impl.condition.And;
import harmony.core.impl.condition.AssertFact;
import harmony.core.impl.condition.Not;
import harmony.core.impl.effect.BasicEffect;
import harmony.core.impl.effect.CompositeEffectImpl;
import harmony.core.impl.effect.Create;
import harmony.core.impl.fact.BasicFact;
import harmony.core.impl.operator.AbstractOperator;
import harmony.lod.model.api.dataset.SourceDataset;
import harmony.lod.model.api.dataset.TempDataset;
import harmony.lod.model.api.slice.Slice;
import harmony.lod.model.impl.dataset.TempDatasetImpl;
import harmony.lod.property.Extracted;
import harmony.lod.property.HasSlice;
import harmony.lod.property.Linked;

public class Extract extends AbstractOperator {

	@SuppressWarnings("unchecked")
	public Extract() {
		super("Extract", SourceDataset.class, Slice.class);
	}

	@Override
	public Condition getPrecondition(Thing... things) throws OperatorException {
		return new And()
				.append(new AssertFact(new BasicFact(new HasSlice(), things[0],
						things[1]))).append(
						new Not(new AssertFact(new BasicFact(new Extracted(),
								things[1]))));
	}

	@Override
	public Effect getEffect(final Thing... things) throws OperatorException {
		// We build a new object
		final TempDataset temp = new TempDatasetImpl();
		BasicEffect e = new BasicEffect();
		e.toAdd(new BasicFact(new Extracted(), things[1]), new BasicFact(
				new HasSlice(), temp, things[1]));
		e.toRemove(new BasicFact(new Linked()));
		CompositeEffect effect = new CompositeEffectImpl();
		effect.append(e);
		effect.append(new Create(temp));
		return e;
	}
}
