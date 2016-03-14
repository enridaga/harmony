package harmony.core.impl.goal;

import harmony.core.api.condition.Condition;
import harmony.core.api.fact.Fact;
import harmony.core.api.goal.Goal;
import harmony.core.impl.condition.And;
import harmony.core.impl.condition.AssertFact;

public class GoalImpl implements Goal {

	private Condition condition = null;

	public GoalImpl(Condition condition) {
		this.condition = condition;
	}

	public GoalImpl(Fact... facts) {
		if(facts.length == 1){
			this.condition = new AssertFact(facts[0]);
		}else{
			And condition = new And();
			for (Fact f : facts)
				condition.append(new AssertFact(f));
			this.condition = condition;
		} // FIXME Else throw an exception!
	}

	@Override
	public Condition asCondition() {
		return condition;
	}
}
