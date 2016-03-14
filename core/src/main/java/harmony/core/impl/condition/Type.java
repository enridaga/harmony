package harmony.core.impl.condition;

import harmony.core.api.condition.Condition;
import harmony.core.api.condition.ConditionVisitor;
import harmony.core.api.thing.Thing;

public final class Type implements Condition {

	private boolean is = false;
	private Thing object;
	Class<? extends Thing> type;
	public Type(Thing object, Class<? extends Thing> type){
		this.object = object;
		this.type = type;
		is = type.isInstance(object);
	}
	
	@Override
	public boolean accept(ConditionVisitor assessment) {
		return assessment.visit(this);
	}
	
	public Thing getThing(){
		return object;
	}
	
	public Class<? extends Thing> getType(){
		return type;
	}
	
	public boolean isTrue(){
		return is;
	}
	
	@Override
	public int compareTo(Condition o) {
		if(o instanceof Bool){
			return 1;
		}else if (o instanceof Type){
			return o.hashCode() > hashCode() ? hashCode() : o.hashCode();
		}
		return -1;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(type ");
		sb.append(" ");
		sb.append(object.toString());
		sb.append(" ");
		sb.append(type.getCanonicalName().toString());
		sb.append(")");
		return sb.toString();
	}
}
