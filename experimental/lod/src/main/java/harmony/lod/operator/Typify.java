package harmony.lod.operator;

import harmony.core.api.condition.Condition;
import harmony.core.api.effect.CompositeEffect;
import harmony.core.api.effect.Effect;
import harmony.core.api.operator.OperatorException;
import harmony.core.api.thing.Thing;
import harmony.core.impl.condition.And;
import harmony.core.impl.condition.AssertFact;
import harmony.core.impl.condition.Bool;
import harmony.core.impl.condition.Exists;
import harmony.core.impl.condition.Not;
import harmony.core.impl.effect.BasicEffect;
import harmony.core.impl.effect.CompositeEffectImpl;
import harmony.core.impl.effect.Create;
import harmony.core.impl.fact.BasicFact;
import harmony.core.impl.operator.AbstractOperator;
import harmony.lod.model.api.dataset.TempDataset;
import harmony.lod.model.api.slice.Frame;
import harmony.lod.model.api.slice.StatementTemplate;
import harmony.lod.model.api.symbol.IRI;
import harmony.lod.model.api.symbol.Symbol;
import harmony.lod.model.impl.slice.FrameException;
import harmony.lod.model.impl.slice.FrameImpl;
import harmony.lod.model.impl.slice.StatementTemplateImpl;
import harmony.lod.model.impl.symbol.IRIImpl;
import harmony.lod.property.HasDefaultRange;
import harmony.lod.property.HasDomain;
import harmony.lod.property.HasSlice;
import harmony.lod.property.Renewed;

/**
 * Creates a Frame with a given type from a statement template which has a
 * property with the type defined as domain and a default range for that
 * property exists.
 * 
 * @author Enrico Daga
 * 
 */
public class Typify extends AbstractOperator {
	@SuppressWarnings("unchecked")
	public Typify() {
		super("Typify", TempDataset.class, StatementTemplate.class,
				IRI.class);
	}

	@Override
	public Condition getPrecondition(Thing... things)
			throws OperatorException {
		And and = new And();
		// renewed
		and.append(new AssertFact().append(new BasicFact(new Renewed(),
				things[0])));
		and.append(new AssertFact(new BasicFact(new HasSlice(), things[0],
				things[1])));
		StatementTemplate st = (StatementTemplate) things[1];
		//and.append(new Bool(st.hasPredicate()));
		// XXX st.getProperty() could return null!!!
		if(!st.hasPredicate()){
			return new Bool(false);
		}
		final IRI predicate = st.getPredicate();
		and.append(new AssertFact().append(new BasicFact(new HasDomain(), st
				.getPredicate(), things[2])));

		@SuppressWarnings("unchecked")
		Exists existsDefaultRange = new Exists(Symbol.class) {
			@Override
			public Condition getCondition(Thing... things) {
				Symbol s = (Symbol) things[0];
				return new AssertFact().append(new BasicFact(
						new HasDefaultRange(), predicate, s));
			}
		};

		and.append(existsDefaultRange);

		Frame frame = createTyped(things);
		and.append(new Not(new AssertFact(new BasicFact(new HasSlice(),
				things[0], frame))));

		return and;
	}

	private Frame createTyped(final Thing... things) throws OperatorException {
		StatementTemplate st = (StatementTemplate) things[1];
		Frame frame;
		IRI clas = (IRI) things[2];
		StatementTemplate type = new StatementTemplateImpl(null, new IRIImpl(
				"http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), clas);

		// create a new Frame
		try {
			frame = new FrameImpl(st, type);
		} catch (FrameException e) {
			throw new OperatorException(this, e);
		}
		return frame;
	}

	@Override
	public Effect getEffect(Thing... things) throws OperatorException {
		Frame frame = createTyped(things);
		// Add new frame
		BasicEffect addFacts = new BasicEffect();
		addFacts.toAdd(new BasicFact(new HasSlice(), things[0], frame));
		CompositeEffect ce = new CompositeEffectImpl();
		ce.append(new Create(frame));
		// ce.append(new Destroy(things[1]));
		ce.append(addFacts);
		return ce;
	}
}
