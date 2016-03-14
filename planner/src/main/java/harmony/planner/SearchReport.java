package harmony.planner;

import harmony.core.api.plan.Plan;

public interface SearchReport {
	
	public boolean goalFound();
	
	public Plan getPlan();
}
