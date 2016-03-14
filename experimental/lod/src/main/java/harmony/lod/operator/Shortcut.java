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
import harmony.lod.model.api.slice.Path;
import harmony.lod.model.api.slice.StatementTemplate;
import harmony.lod.model.api.symbol.IRI;
import harmony.lod.model.impl.slice.StatementTemplateImpl;
import harmony.lod.property.HasSlice;
import harmony.lod.property.IsShortable;
import harmony.lod.property.Renewed;

public class Shortcut extends AbstractOperator {

	@SuppressWarnings("unchecked")
	public Shortcut() {
		super("Shortcut", TempDataset.class, Path.class, IRI.class);
	}

	@Override
	public Condition getPrecondition(Thing... things)
			throws OperatorException {
		Path path = (Path) things[1];
		IRI predicate = (IRI) things[2];
		StatementTemplate newSt = buildShortcut(path, predicate);
		Not not = new Not(new AssertFact(new BasicFact(new HasSlice(), things[0], newSt)));
		return new And().append(not)
				.append(new AssertFact().append(new BasicFact(new Renewed(),
						things[0])))
				.append(
				new AssertFact().append(new BasicFact(new HasSlice(),
						things[0], things[1]))).append(
				new AssertFact().append(new BasicFact(new IsShortable(),
						things[1], things[2])));
	}

	protected StatementTemplate buildShortcut(Path path, IRI predicate){
		StatementTemplate first = path.asList().get(0);
		IRI subject = first.getSubject();
		StatementTemplate last = path.asList().get(path.asList().size()-1);
		StatementTemplate newSt = null;
		if(last.hasObject()){
			newSt = new StatementTemplateImpl(subject, predicate, last.getObject());
		}else if (last.hasValue()){
			newSt = new StatementTemplateImpl(subject, predicate, last.getValue());
		}else if (last.hasLang()){
			newSt = new StatementTemplateImpl(subject, predicate, last.getLang());
		}else if (last.hasDatatype()){
			newSt = new StatementTemplateImpl(subject, predicate, last.getDatatype());
		}else{
			newSt = new StatementTemplateImpl(subject, predicate);
		}
		return newSt;
	}
	
	@Override
	public Effect getEffect(Thing... things) throws OperatorException {
		// We create the new statement template
		Path path = (Path) things[1];
		IRI predicate = (IRI) things[2];
		StatementTemplate newSt = buildShortcut(path, predicate);
		BasicEffect e = new BasicEffect();
		e.toAdd(new BasicFact(new HasSlice(), things[0], newSt));
		return e;
	}

}
