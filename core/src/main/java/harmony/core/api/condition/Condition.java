package harmony.core.api.condition;



public interface Condition extends Comparable<Condition>{

	public boolean accept(ConditionVisitor assessment);
	
}
