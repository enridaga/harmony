package harmony.planner.bestfirst;

import harmony.planner.NoSolutionException;
import harmony.planner.SearchReport;

import java.util.Set;

public interface BestFirstSearchReport  extends SearchReport {

	public Set<Node> openNodes();
	
	public Set<Node> closedNodes();
	
	public Node getRootNode();
	
	public Node getGoalNode() throws NoSolutionException;

}
