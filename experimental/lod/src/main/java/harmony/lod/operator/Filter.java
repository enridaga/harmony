package harmony.lod.operator;

import harmony.core.api.condition.Condition;
import harmony.core.api.effect.CompositeEffect;
import harmony.core.api.effect.Effect;
import harmony.core.api.operator.OperatorException;
import harmony.core.api.thing.Thing;
import harmony.core.impl.condition.And;
import harmony.core.impl.condition.AssertFact;
import harmony.core.impl.condition.Equality;
import harmony.core.impl.condition.Exists;
import harmony.core.impl.condition.Not;
import harmony.core.impl.effect.BasicEffect;
import harmony.core.impl.effect.CompositeEffectImpl;
import harmony.core.impl.effect.ForallEffect;
import harmony.core.impl.effect.NotEffect;
import harmony.core.impl.effect.WhenEffect;
import harmony.core.impl.fact.BasicFact;
import harmony.core.impl.operator.AbstractOperator;
import harmony.lod.model.api.dataset.OutputDataset;
import harmony.lod.model.api.dataset.TempDataset;
import harmony.lod.model.api.slice.Slice;
import harmony.lod.property.HasSlice;
import harmony.lod.property.NeededSlice;
import harmony.lod.property.Renewed;

public class Filter extends AbstractOperator {

	@SuppressWarnings("unchecked")
	public Filter() {
		super("Filter", TempDataset.class, Slice.class, OutputDataset.class);
	}

	@Override
	public Condition getPrecondition(final Thing... things)
			throws OperatorException {

		@SuppressWarnings("unchecked")
		Exists exists = new Exists(Slice.class) {

			@Override
			public Condition getCondition(Thing... mo) {
				Not sliceNotEqual = new Not(new Equality(mo[0], things[1]));
				AssertFact inDataset = new AssertFact(new BasicFact(
						new HasSlice(), things[0], mo[0]));
				return new And().append(sliceNotEqual).append(inDataset);
			}
		};
		return new And()
				.append(exists)
				.append(new AssertFact().append(new BasicFact(new Renewed(),
						things[0])))
				.append(new AssertFact(new BasicFact(
								new NeededSlice(), things[1], things[2])))
				.append(new AssertFact().append(new BasicFact(new HasSlice(),
						things[0], things[1])));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Effect getEffect(final Thing... things) throws OperatorException {
		BasicEffect reput = new BasicEffect();
		reput.toAdd(new BasicFact(new HasSlice(), things[0], things[1]));
		CompositeEffect ce = new CompositeEffectImpl();
		ce.append(reput);
		ForallEffect forall = new ForallEffect(TempDataset.class, Slice.class) {
			@Override
			public Effect getEffect(Thing... ethings) {
				Equality datasetCheck = new Equality(things[0], ethings[0]);
				Equality sliceCheck = new Equality(things[1], ethings[1]);

				BasicEffect filtered = new BasicEffect();
				filtered.toAdd(new BasicFact(new HasSlice(), ethings[0],
						ethings[1]));
				return new WhenEffect(new And().append(datasetCheck).append(
						new AssertFact(new BasicFact(new HasSlice(),
								ethings[0], ethings[1]))), new WhenEffect(
						new Not(sliceCheck), new NotEffect(filtered)));
			}
		};
		ce.append(forall);
		return ce;
	}
}
