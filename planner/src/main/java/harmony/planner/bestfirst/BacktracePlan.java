package harmony.planner.bestfirst;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import harmony.core.api.operator.GroundAction;
import harmony.core.api.plan.Plan;

public class BacktracePlan implements Plan {
	protected List<Node> path = null;

	public BacktracePlan(Node node) {
		path = buildPath(node);
	}
	
	public static List<Node> buildPath(Node node){
		List<Node> path = new ArrayList<Node>();
		while (!node.isRoot()) {
			path.add(node);
			node = (Node) node.getParent();
		}
		path.add(node);
		Collections.reverse(path);
		return path;
	}
	
	public static List<GroundAction> buildActions(List<Node> path){
		List<GroundAction> steps = new ArrayList<GroundAction>();
		for (Node n : path) {
			if (!n.isRoot())
				steps.add(n.getAction());
		}
		return steps;
	}

	public int size() {
		return getPath().size() - 1;
	}

	public List<GroundAction> getActions() {
		return buildActions(getPath());
	}

	public List<Node> getPath() {
		return Collections.unmodifiableList(path);
	}
}