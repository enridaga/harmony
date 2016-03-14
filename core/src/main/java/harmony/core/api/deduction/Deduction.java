package harmony.core.api.deduction;

import harmony.core.api.condition.Condition;
import harmony.core.api.fact.Fact;

public interface Deduction {

	public boolean isDeductable(Fact fact);
	
	public Condition onCondition(Fact fact);
	
}
