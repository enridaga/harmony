package harmony.core.impl.assessment;

import harmony.core.api.assessment.Assessment;
import harmony.core.api.condition.Condition;
import harmony.core.api.fact.Fact;
import harmony.core.api.property.DerivableProperty;
import harmony.core.api.state.State;
import harmony.core.api.thing.Thing;
import harmony.core.impl.condition.And;
import harmony.core.impl.condition.AssertFact;
import harmony.core.impl.condition.Bool;
import harmony.core.impl.condition.Equality;
import harmony.core.impl.condition.Exists;
import harmony.core.impl.condition.Forall;
import harmony.core.impl.condition.Not;
import harmony.core.impl.condition.Or;
import harmony.core.impl.condition.Type;
import harmony.core.impl.condition.When;
import harmony.core.impl.property.DerivedPropertyException;

import java.util.Iterator;
import java.util.List;

public class Evaluator implements Assessment {

	private State state = null;

	public Evaluator(State state) {
		this.state = state;
	}

	public State getState() {
		return state;
	}

	public boolean visit(Not cond) {
		return !cond.getCondition().accept(this);
	}

	public boolean visit(AssertFact cond) {
		Iterator<Fact> it = cond.toFacts();
		while (it.hasNext()) {
			Fact n = it.next();
			if (!getState().getFactRegistry().contains(n)) {
				if (n.getProperty() instanceof DerivableProperty) {
					try {
						if(!((DerivableProperty) n.getProperty()).isDerivable(
								getState(),
								n.getThings().toArray(
										new Thing[n.getThings().size()]))){
							return false;
						}
					} catch (DerivedPropertyException e) {
						e.printStackTrace();
						return false;
					}
				}else{
					return false;
				}
			}
		}
		return true;
	}

	public boolean visit(Equality cond) {
		return cond.getLHS().equals(cond.getRHS());
	}

	public boolean visit(And and) {
		Iterator<Condition> it = and.iterator();

		while (it.hasNext()) {
			Condition c = it.next();
			if (!c.accept(this)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean visit(Or or) {
		Iterator<Condition> it = or.iterator();

		while (it.hasNext()) {
			Condition c = it.next();
			if (c.accept(this)) {
				return true;
			}
		}
		return false;
	}

	public boolean visit(Bool bool) {
		return bool.isTrue();
	}

	public boolean visit(When when) {

		boolean r = when.when().accept(this);
		if (r) {
			return when.then().accept(this);
		} else {
			if(when.otherwise()!=null){
				return when.otherwise().accept(this);
			}else{
				return true;
			}
		}
	}

	@Override
	public boolean visit(Exists exists) {
		Iterator<List<Thing>> iter = state.getParametersRegistry().iterator(
				exists);
		while (iter.hasNext()) {
			List<Thing> paramValues = iter.next();
			Condition cond = exists.getCondition(paramValues
					.toArray(new Thing[paramValues.size()]));
			if (cond.accept(this)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean visit(Forall forall) {
		Iterator<List<Thing>> iter = state.getParametersRegistry().iterator(
				forall);
		while (iter.hasNext()) {
			List<Thing> paramValues = iter.next();
			Condition cond = forall.getCondition(paramValues
					.toArray(new Thing[paramValues.size()]));
			if (!cond.accept(this)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean visit(Type type) {
		return type.isTrue();
	};
}
