package harmony.core.impl.condition;

import harmony.core.api.condition.Condition;
import harmony.core.api.condition.ConditionVisitor;

public final class Not implements Condition {

	private Condition cond = null;

	public Not(Condition cond) {
		this.cond = cond;
	}

	public Condition getCondition() {
		return cond;
	}

	@Override
	public boolean accept(ConditionVisitor assessment) {
		return assessment.visit(this);
	}
	
	@Override
	public int compareTo(Condition o) {
		if(o instanceof Bool){
			return 1;
		}
		return cond.compareTo(o);
	}
	

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("(not ");
		sb.append(cond.toString());
		sb.append(")");
		return sb.toString();
	}
}
