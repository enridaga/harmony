package harmony.planner.graphplan;

import java.util.Set;

public interface GPFact {

	public Set<GPAction> addedBy();
	
	public Set<GPFact> mutex();
	
	public int time();
}
