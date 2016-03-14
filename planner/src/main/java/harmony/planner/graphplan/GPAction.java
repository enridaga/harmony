package harmony.planner.graphplan;

import harmony.core.api.operator.Action;
import harmony.core.api.operator.GroundAction;
import harmony.core.api.state.State;

import java.util.Set;

public interface GPAction {

	public Set<GPFact> positivePreconditions();
	
	public Set<GPFact> negativePreconditions();

	public Set<GPFact> positiveEffect();

	public Set<GPFact> negativeEffect();
	
	public int time();
	
	public Set<GPAction> mutex();
	
	public boolean isNoOp();
	
	public Action toAction();
	
	public GroundAction toGroundAction(State stat);
}
