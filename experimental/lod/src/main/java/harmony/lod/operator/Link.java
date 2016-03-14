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
import harmony.lod.model.api.slice.Slice;
import harmony.lod.property.Extracted;
import harmony.lod.property.Linked;

public class Link extends AbstractOperator {

	@SuppressWarnings("unchecked")
	public Link() {
		super("Link");
	}

	@SuppressWarnings("unchecked")
	@Override
	public Condition getPrecondition(Thing... things)
			throws OperatorException {
		And and = new And();
		and.append(new Not(new AssertFact(new BasicFact(new Linked()))));
		and.append(new Exists(Slice.class) {

			@Override
			public Condition getCondition(Thing... m) {
				// TODO Auto-generated method stub
				return new AssertFact(new BasicFact(new Extracted(), m[0]));
			}
		});
		return and;
	}

	@Override
	public Effect getEffect(Thing... things) throws OperatorException {
		BasicEffect e = new BasicEffect();
		e.toAdd(new BasicFact(new Linked()));
		return e;
	}
}
