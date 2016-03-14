package harmony.planner.graphplan;

import harmony.core.api.operator.Operator;
import harmony.core.api.state.State;

import java.util.List;
import java.util.Set;

public interface PlanningGraph{

	public int size();
	
	public Time getTime(int index);
	
	public List<Time> asList();
	
	public Time getFirst();
	
	public Time getLast();
	
	public Set<Operator> getOperators();
	
	public State getRoot();

	public abstract boolean noSolution();

	public abstract boolean goalFound();
	
}
