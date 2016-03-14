package harmony.planner.bestfirst;

import harmony.core.api.operator.GroundAction;
import harmony.core.api.operator.OperatorRegistry;
import harmony.core.api.state.State;
import harmony.planner.ActionsProvider;
import harmony.planner.PlannerInput;

import java.util.Set;

public interface Node extends State{

	public GroundAction getAction();

	public int getDepth();

	public void setParent(Node parent);

	public ActionsProvider getActionsProvider();
	
	public Set<Node> getSuccessors();

	public boolean isRoot();

	public Node getParent();

	public PlannerInput getPlannerInput();

	public OperatorRegistry getOperatorRegistry();
	
	public boolean isGoal();
}
