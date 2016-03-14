package harmony.lod.operator;

import harmony.core.api.condition.Condition;
import harmony.core.api.effect.Effect;
import harmony.core.api.operator.OperatorException;
import harmony.core.api.thing.Thing;
import harmony.core.impl.condition.And;
import harmony.core.impl.condition.AssertFact;
import harmony.core.impl.condition.Not;
import harmony.core.impl.effect.BasicEffect;
import harmony.core.impl.fact.BasicFact;
import harmony.core.impl.operator.AbstractOperator;
import harmony.lod.model.api.dataset.TempDataset;
import harmony.lod.property.Linked;
import harmony.lod.property.Renewed;

public class Renew extends AbstractOperator {

	@SuppressWarnings("unchecked")
	public Renew() {
		super("Renew", TempDataset.class);
	}

	@Override
	public Condition getPrecondition(Thing... things)
			throws OperatorException {
		And and = new And();
		and.append(new AssertFact(new BasicFact(new Linked())));
		and.append(new Not(new AssertFact(new BasicFact(new Renewed(),
				things[0]))));
		return and;
	}

	@Override
	public Effect getEffect(Thing... things) throws OperatorException {
		BasicEffect e = new BasicEffect();
		e.toAdd(new BasicFact(new Renewed(), things[0]));
		return e;
	}

}
