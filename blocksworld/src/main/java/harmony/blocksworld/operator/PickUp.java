package harmony.blocksworld.operator;

import harmony.blocksworld.Block;
import harmony.blocksworld.property.ArmEmpty;
import harmony.blocksworld.property.Clear;
import harmony.blocksworld.property.Holding;
import harmony.blocksworld.property.OnTable;
import harmony.core.api.condition.Condition;
import harmony.core.api.effect.Effect;
import harmony.core.api.operator.OperatorException;
import harmony.core.api.thing.Thing;
import harmony.core.impl.condition.AssertFact;
import harmony.core.impl.effect.BasicEffect;
import harmony.core.impl.fact.BasicFact;
import harmony.core.impl.operator.AbstractOperator;

public final class PickUp extends AbstractOperator {

	@SuppressWarnings("unchecked")
	public PickUp() {
		super("PickUp", Block.class);
	}

	public Condition getPrecondition(final Thing... things)
			throws OperatorException {
		return new AssertFact(new BasicFact(new OnTable(), things[0]),
				new BasicFact(new Clear(), things[0]), new BasicFact(
						new ArmEmpty()));
	}

	public Effect getEffect(final Thing... things) throws OperatorException {
		BasicEffect e = new BasicEffect();
		e.toAdd(new BasicFact(new Holding(), things[0]));
		e.toRemove(new BasicFact(new OnTable(), things[0]), new BasicFact(
				new ArmEmpty()));
		return e;
	}

}
