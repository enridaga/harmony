package harmony.core.impl.assessment;

import harmony.core.api.assessment.ScoredAssessment;
import harmony.core.api.condition.Condition;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class ScoredEvaluator extends Evaluator implements
		ScoredAssessment {

	private List<Float> scores;
	private float maxScore;

	public ScoredEvaluator(State state, float maxScore) {
		super(state);
		reset(maxScore);
		scores = new ArrayList<Float>();
	}

	@Override
	public void reset(float maxScore) {
		this.maxScore = maxScore;
	}

	@Override
	public float getScore() {
		float scoresSum = 0;
		for (Float f : scores) {
			scoresSum += f;
		}
		if (scoresSum == 0) {
			return 0;
		}
		scoresSum /= scores.size();
		return scoresSum;
	}

	@Override
	public boolean visit(AssertFact cond) {
		boolean r = super.visit(cond);
		if (r) {
			scores.add(maxScore);
		} else {
			scores.add(0f);
		}
		return r;
	}

	@Override
	public boolean visit(Bool bool) {
		boolean r = super.visit(bool);
		if (r) {
			scores.add(maxScore);
		} else {
			scores.add(0f);
		}
		return r;
	}

	@Override
	public boolean visit(Equality cond) {
		boolean r = super.visit(cond);
		if (r) {
			scores.add(maxScore);
		} else {
			scores.add(0f);
		}
		return r;
	}

	@Override
	public boolean visit(Not cond) {
		ScoredEvaluator e = new ScoredEvaluator(getState(), maxScore);
		boolean r = cond.getCondition().accept(e);
		scores.add(maxScore - e.getScore());
		return !r;
	}

	@Override
	public boolean visit(Or or) {
		Evaluator orEval = new Evaluator(getState());
		boolean r = orEval.visit(or);
		if (r) {
			scores.add(maxScore);
		} else {
			scores.add(0f);
		}
		return r;
	}

	@Override
	public boolean visit(Exists exists) {
		ScoredEvaluator ee = new ScoredEvaluator(getState(), maxScore);
		Iterator<List<Thing>> iter = ee.getState().getParametersRegistry()
				.iterator(exists);
		boolean r = false;
		while (iter.hasNext()) {
			List<Thing> paramValues = iter.next();
			Condition cond = exists.getCondition(paramValues
					.toArray(new Thing[paramValues.size()]));
			if (cond.accept(ee)) {
				r = true;
				break;
			}
		}
		scores.add(ee.getScore());
		return r;
	}
	
	@Override
	public boolean visit(Forall forall) {
		ScoredEvaluator ee = new ScoredEvaluator(getState(), maxScore);
		Iterator<List<Thing>> iter = ee.getState().getParametersRegistry()
				.iterator(forall);
		boolean r = true;
		while (iter.hasNext()) {
			List<Thing> paramValues = iter.next();
			Condition cond = forall.getCondition(paramValues
					.toArray(new Thing[paramValues.size()]));
			if (!cond.accept(ee) && r) {
				r = false;
			}
		}
		scores.add(ee.getScore());
		return r;
	}

	@Override
	public boolean visit(And and) {
		ScoredEvaluator ee = new ScoredEvaluator(getState(), maxScore);
		Iterator<Condition> andi = and.iterator();
		boolean r = true;
		while (andi.hasNext()) {
			if (!andi.next().accept(ee) && r) {
				r = false;
			}
		}
		scores.add(ee.getScore());
		return r;
	}
	
	@Override
	public boolean visit(When when) {
		Evaluator e = new Evaluator(getState());
		boolean r = when.when().accept(e);
		if(r){
			r = when.then().accept(this);
		}else{
			if(when.otherwise()!=null){
				r = when.otherwise().accept(this);
			}else{
				r = true;
			}
		}
		return r;
	}
}
