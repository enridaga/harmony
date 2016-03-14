package harmony.core.impl.assessment;

import harmony.core.api.assessment.AssessmentReport;
import harmony.core.api.condition.Condition;
import harmony.core.api.fact.Fact;
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
import harmony.core.impl.condition.When;
import harmony.core.impl.state.StaticState;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * TODO
 * rename it ConditionAnalyzer
 * 
 * @author Enrico Daga
 *
 */
public class FactsCollector extends Evaluator implements AssessmentReport {

	private Set<Fact> shared;
	private Set<Fact> assertedMissing;
	private Set<Fact> negatedPresent;
	private Set<Fact> negatedMissing;

	public FactsCollector(State state) {
		super(state);
		clear();
	}
	public FactsCollector() {
		super(new StaticState());
		clear();
	}
	
	@Override
	public boolean visit(AssertFact cond) {
		Iterator<Fact> i = cond.toFacts();
		boolean ret = true;
		while (i.hasNext()) {
			Fact f = i.next();
			if (getState().getFacts().contains(f)) {
				this.shared.add(f);
			} else {
				this.assertedMissing.add(f);
				ret = false;
			}
		}
		return ret;
	}

	@Override
	public boolean visit(And and) {
		FactsCollector colly;
		boolean ret = true;
		for (Condition c : and.asList()) {
			colly = new FactsCollector(getState());
			c.accept(colly);
			shared.addAll(colly.getShared());
			negatedPresent.addAll(colly.getNegated());
			assertedMissing.addAll(colly.getMissing());
			negatedMissing.addAll(colly.getNegatedMissing());
			if (colly.getNegated().isEmpty() && colly.getMissing().isEmpty()) {
				// go ahead
			} else {
				ret = false;
			}
		}
		return ret;
	}

	@Override
	public boolean visit(Bool bool) {
		return bool.isTrue();
	}

	@Override
	public boolean visit(Equality cond) {
		return super.visit(cond);
	}

	@Override
	public boolean visit(Exists exists) {
		return super.visit(exists);
	}

	@Override
	public boolean visit(Forall forall) {
		Iterator<List<Thing>> iter = getState().getParametersRegistry()
				.iterator(forall);
		boolean ret = true;
		while (iter.hasNext()) {
			List<Thing> paramValues = iter.next();
			Condition cond = forall.getCondition(paramValues
					.toArray(new Thing[paramValues.size()]));
			if (!cond.accept(this)) {
				ret = false;
			}
		}
		return ret;
	}

	/**
	 * FIXME Double-check this
	 */
	@Override
	public boolean visit(Not cond) {
		FactsCollector colly = new FactsCollector(getState());
		boolean ret = cond.getCondition().accept(colly);
		// shared facts must be negated
		negatedPresent.addAll(colly.getShared());
		// negated facts are missing
		assertedMissing.addAll(colly.getNegated());
		// missing facts are NOT needed
		negatedMissing.addAll(colly.getMissing());
		return !ret;
	}

	/**
	 * FIXME double-check this
	 */
	@Override
	public boolean visit(Or or) {
		FactsCollector colly;
		Iterator<Condition> i = or.iterator();
		boolean ret = false;
		Set<Fact> sharedPartial = new HashSet<Fact>();
		Set<Fact> negatedMissingPartial = new HashSet<Fact>();
		Set<Fact> negPres = new HashSet<Fact>();
		Set<Fact> assMiss = new HashSet<Fact>();
		while (i.hasNext()) {
			Condition c = i.next();
			colly = new FactsCollector(getState());
			if (c.accept(colly)) {
				ret = true;
			} else {
				// We remember reasons why this condition is not true

			}
			sharedPartial.addAll(colly.getShared());
			negatedMissingPartial.addAll(colly.getNegatedMissing());
			negPres.addAll(colly.getNegated());
			assMiss.addAll(colly.getMissing());
		}
		shared.addAll(sharedPartial);
		negatedMissing.addAll(negatedMissingPartial);
		if (!ret) {
			// We add only reasons why this condition is not true
			negatedPresent.addAll(negPres);
			assertedMissing.addAll(assMiss);
		}
		return ret;
	}

	@Override
	public boolean visit(When when) {
		Evaluator e = new Evaluator(getState());
		boolean cond = when.when().accept(e);
		boolean ret;
		when.when().accept(this);
		if (cond) {
			ret = when.then().accept(this);
		} else {
			if (when.otherwise() != null) {
				ret = when.otherwise().accept(this);
			} else {
				ret = true;
			}
		}

		return ret;
	}

	@Override
	public Set<Fact> getShared() {
		return Collections.unmodifiableSet(this.shared);
	}

	@Override
	public Set<Fact> getIgnored() {
		Set<Fact> ignored = new HashSet<Fact>();
		ignored.addAll(getState().getFacts());
		ignored.removeAll(getShared());
		ignored.removeAll(getNegated());
		// ignored.removeAll(getMissing()); :)
		return Collections.unmodifiableSet(ignored);
	}

	@Override
	public Set<Fact> getMissing() {
		return Collections.unmodifiableSet(this.assertedMissing);
	}

	@Override
	public Set<Fact> getNegated() {
		return Collections.unmodifiableSet(this.negatedPresent);
	}

	@Override
	public Set<Fact> getNegatedMissing() {
		return Collections.unmodifiableSet(this.negatedMissing);
	}

	@Override
	public void clear() {
		this.assertedMissing = new HashSet<Fact>();
		this.negatedPresent = new HashSet<Fact>();
		this.shared = new HashSet<Fact>();
		this.negatedMissing = new HashSet<Fact>();
	}
}
