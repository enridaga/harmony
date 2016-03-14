package harmony.core.impl.condition;

import harmony.core.api.condition.Condition;
import harmony.core.api.condition.ConditionVisitor;
import harmony.core.api.thing.Thing;

public final class Equality implements Condition {

	private Thing rhs = null;
	private Thing lhs = null;

	public Equality(Thing rhs, Thing lhs) {
		this.rhs = rhs;
		this.lhs = lhs;
	}

	@Override
	public boolean accept(ConditionVisitor assessment) {
		return assessment.visit(this);
	}

	public Thing getRHS() {
		return rhs;
	}

	public Thing getLHS() {
		return lhs;
	}
	
	@Override
	public int compareTo(Condition o) {
		if(o instanceof Not){
			return compareTo(((Not) o).getCondition());
		}else
		if(o instanceof Bool || o instanceof Type){
			return 1;
		}else if (o instanceof Equality){
			return o.hashCode() > hashCode() ? hashCode() : o.hashCode();
		}
		return -1;
	}
	

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("(= ");
		sb.append(" ");
		sb.append(lhs.toString());
		sb.append(" ");
		sb.append(rhs.toString());
		sb.append(")");
		return sb.toString();
	}
}
