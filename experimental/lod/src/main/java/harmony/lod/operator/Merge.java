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
import harmony.core.impl.condition.Not;
import harmony.core.impl.effect.BasicEffect;
import harmony.core.impl.effect.CompositeEffectImpl;
import harmony.core.impl.effect.Create;
import harmony.core.impl.effect.Destroy;
import harmony.core.impl.fact.BasicFact;
import harmony.core.impl.operator.AbstractOperator;
import harmony.lod.model.api.dataset.TempDataset;
import harmony.lod.model.api.slice.Frame;
import harmony.lod.model.api.symbol.IRI;
import harmony.lod.model.impl.slice.FrameException;
import harmony.lod.model.impl.slice.FrameImpl;
import harmony.lod.property.HasSlice;

import java.util.Set;

public class Merge extends AbstractOperator {

	@SuppressWarnings("unchecked")
	public Merge() {
		super("Merge", TempDataset.class, Frame.class, Frame.class);
	}

	@Override
	public Condition getPrecondition(Thing... things)
			throws OperatorException {
		And and = new And();
		and.append(new AssertFact(new BasicFact(new HasSlice(), things[0],
				things[1])));
		and.append(new AssertFact(new BasicFact(new HasSlice(), things[0],
				things[2])));
		and.append(new Not(new Equality(things[0], things[2])));
		
		Set<IRI> typesA = ((Frame) things[1]).getTypes();
		Set<IRI> typesB = ((Frame) things[2]).getTypes();
		and.append(new Bool(typesA.equals(typesB)));
		try {
			and.append(new Not(new AssertFact(new BasicFact(new HasSlice(),
					things[0], mergedFrame(((Frame) things[1]),
							(Frame) things[2])))));
		} catch (FrameException e) {
			throw new OperatorException(this, e);
		}
		return and;
	}

	private Frame mergedFrame(Frame a, Frame b) throws FrameException {
		return new FrameImpl(a, b);
	}

	@Override
	public Effect getEffect(Thing... things) throws OperatorException {
		Frame frame;
		try {
			frame = mergedFrame((Frame) things[1],(Frame) things[2]);
		} catch (FrameException e) {
			throw new OperatorException(this, e);
		}
		CompositeEffect effect = new CompositeEffectImpl();
		effect.append(new Destroy(things[1]));
		effect.append(new Destroy(things[2]));
		effect.append(new Create(frame));
		effect.append(new BasicEffect().toAdd(new BasicFact(new HasSlice(),
					things[0], frame)));
		return effect;
	}
}
