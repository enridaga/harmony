package harmony.blocksworld.operator;

import harmony.blocksworld.Block;
import harmony.blocksworld.property.ArmEmpty;
import harmony.blocksworld.property.Clear;
import harmony.blocksworld.property.Holding;
import harmony.blocksworld.property.On;
import harmony.core.api.condition.Condition;
import harmony.core.api.effect.Effect;
import harmony.core.api.operator.OperatorException;
import harmony.core.api.thing.Thing;
import harmony.core.impl.condition.And;
import harmony.core.impl.condition.AssertFact;
import harmony.core.impl.condition.Equality;
import harmony.core.impl.condition.Not;
import harmony.core.impl.effect.BasicEffect;
import harmony.core.impl.fact.BasicFact;
import harmony.core.impl.operator.AbstractOperator;

public final class Stack extends AbstractOperator {

	@Override
	public int cost() {
		return 1;
	}
	
	@SuppressWarnings("unchecked")
	public Stack() {
		super("Stack", Block.class, Block.class);
	}

	public Condition getPrecondition(final Thing... things)
			throws OperatorException {
		return new And()
				.append(new Not(new Equality(things[0], things[1])))
				.append(new AssertFact().append(
						new BasicFact(new Clear(), things[1])).append(
						new BasicFact(new Holding(), things[0])))
				.append(new Not(new AssertFact().append(new BasicFact(
						new ArmEmpty()))));
	}

	public Effect getEffect(final Thing... things) throws OperatorException {
		BasicEffect e = new BasicEffect();
		e.toAdd(new BasicFact(new On(), things[0], things[1]), new BasicFact(
				new Clear(), things[0]), new BasicFact(new ArmEmpty()));
		e.toRemove(new BasicFact(new Clear(), things[1]), new BasicFact(
				new Holding(), things[0]));
		return e;
	}

}
