package harmony.lod.operator;

import harmony.core.api.condition.Condition;
import harmony.core.api.effect.CompositeEffect;
import harmony.core.api.effect.Effect;
import harmony.core.api.operator.OperatorException;
import harmony.core.api.thing.Thing;
import harmony.core.impl.condition.And;
import harmony.core.impl.condition.AssertFact;
import harmony.core.impl.condition.Bool;
import harmony.core.impl.condition.Equality;
import harmony.core.impl.condition.Forall;
import harmony.core.impl.condition.Not;
import harmony.core.impl.condition.Type;
import harmony.core.impl.condition.When;
import harmony.core.impl.effect.BasicEffect;
import harmony.core.impl.effect.CompositeEffectImpl;
import harmony.core.impl.effect.Destroy;
import harmony.core.impl.effect.ForallEffect;
import harmony.core.impl.effect.WhenEffect;
import harmony.core.impl.fact.BasicFact;
import harmony.core.impl.operator.AbstractOperator;
import harmony.lod.model.api.dataset.Dataset;
import harmony.lod.model.api.dataset.OutputDataset;
import harmony.lod.model.api.dataset.TempDataset;
import harmony.lod.model.api.slice.Slice;
import harmony.lod.property.HasSlice;
import harmony.lod.property.NeededSlice;
import harmony.lod.property.Renewed;

public class Append extends AbstractOperator {

	@SuppressWarnings("unchecked")
	public Append() {
		super("Append", TempDataset.class, Dataset.class);
	}

	@Override
	public Condition getPrecondition(final Thing... things)
			throws OperatorException {
		// If parameter 2 is an output dataset, append the temp only if
		// it does not contains not needed slices
		@SuppressWarnings("unchecked")
		When when = new When(new Type(things[1], OutputDataset.class),
				new Forall(Slice.class) {
					@Override
					public Condition getCondition(Thing... mod) {
						And and = new And();
						and.append(new When(new AssertFact(new BasicFact(
								new HasSlice(), things[0], mod[0])),
								new AssertFact(new BasicFact(new NeededSlice(),
										mod[0], things[1])), new Bool(true)));
						return and;
					}
				},
				// // if it is not an output dataset
				// new Forall(TempDataset.class) {
				// @Override
				// public Condition getCondition(Thing... mod) {
				// return new AssertFact(new BasicFact(new Linked(),
				// mod[0], things[0]));
				// }
				// }
				new AssertFact(new BasicFact(new Renewed(), things[1])));
		return new And()
				.append(when)
				.append(new AssertFact(new BasicFact(new Renewed(), things[0])))
				.append(new Not(new Bool(((Dataset) things[1]).isReadOnly())))
				.append(new Not(new Equality(things[0], things[1])));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Effect getEffect(final Thing... things) throws OperatorException {
		CompositeEffect e = new CompositeEffectImpl();
		ForallEffect addAndRemoveSlices = new ForallEffect(Slice.class) {
			
			@Override
			public Effect getEffect(final Thing... mods) {
				BasicEffect migrateIt = new BasicEffect();
				migrateIt.toAdd(new BasicFact(new HasSlice(), things[1], mods[0]));
				migrateIt.toRemove(new BasicFact(new HasSlice(), things[0], mods[0]));
				WhenEffect we = new WhenEffect(new AssertFact(new BasicFact(new HasSlice(), things[0], mods[0])), migrateIt);
				return we;
			}
		};
		e.append(addAndRemoveSlices);
		e.append(new Destroy(things[0]));
		// e.append(new BasicEffect().toRemove(new BasicFact(new Empty(),
		// things[1])));
		return e;
	}

}
