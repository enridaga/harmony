package harmony.core.api.operator;


import java.util.Collection;

public interface OperatorRegistry extends Cloneable{
	
	public void register(Operator operator);

	public void unregister(String operatorName);

	public boolean isRegistered(String operatorName);

	public Operator getOperator(String name);

	public int size();
	
	public Operator[] toArray();
	
	public Collection<Operator> asCollection();
	
	public OperatorRegistry clone();
}
