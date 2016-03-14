package harmony.core.impl.condition;

import harmony.core.api.condition.Condition;
import harmony.core.api.condition.ConditionVisitor;

public final class Bool implements Condition {

	boolean cond = false;

	public Bool(boolean cond) {
		this.cond = cond;
	}

	@Override
	public boolean accept(ConditionVisitor assessment) {
		return assessment.visit(this);
	}

	public boolean isTrue() {
		return cond;
	}

	@Override
	public int compareTo(Condition o) {
		if(o instanceof Bool){
			return o.hashCode() > hashCode() ? hashCode() : o.hashCode();	
		}else{
			return -1;
		}
	}
	

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("(bool ");
		sb.append(cond);
		sb.append(")");
		return sb.toString();
	}
}
