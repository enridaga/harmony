package harmony.planner.bestfirst;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import harmony.core.api.plan.Plan;
import harmony.planner.ActionsProvider;
import harmony.planner.NoSolutionException;
import harmony.planner.PlannerInput;
import harmony.planner.SimpleActionsProvider;
import harmony.planner.bestfirst.heuristic.BestNodeHeuristic;

/**
 * Best First Forward searcher.
 * 
 * @author Enrico Daga
 *
 */
public final class BestFirstPlanner implements BestFirstSearcher {

	private BestNodeHeuristic comparator = null;
	private OpenNodes<Node> open = null;
	private List<Node> closed = null;
	private Node root = null;
	private Node goal = null;
	private Plan plan = null;
	private int maxClosedNodes = -1;

	public BestFirstPlanner(PlannerInput input, BestNodeHeuristic nodeComparator) {
		this(input,nodeComparator, new SimpleActionsProvider());
	}
	

	public BestFirstPlanner(PlannerInput input, BestNodeHeuristic nodeComparator, ActionsProvider actionsProvider) {
		comparator = nodeComparator;
		open = new OpenNodes<Node>(comparator);
		closed = new ArrayList<Node>();
		root = new NodeImpl(input, actionsProvider);
	}
	
	public void setMaxClosedNodes(int max){
		maxClosedNodes = max;
	}
	
	public int getMaxClosedNodes(){
		return maxClosedNodes;
	}

	public BestFirstPlanner(PlannerInput input) {
		this(input, new AstarHeuristic());
	}

	public Plan search() throws NoSolutionException {
		/**
		 * <pre>
		 * OPEN = [initial state]
		 * CLOSED = []
		 * while OPEN is not empty
		 * do
		 *  1. Remove the best node from OPEN, call it n, add it to CLOSED.
		 *  2. If n is the goal state, backtrace path to n (through recorded parents) and return path.
		 *  3. Create n's successors.
		 *  4. For each successor do:
		 *        a. If it is not in CLOSED: evaluate it, add it to OPEN, and record its parent.
		 *        b. Otherwise: change recorded parent if this new path is better than previous one.
		 * done
		 * </pre>
		 */
		open.add(root);
		while (!open.isEmpty()) {

			if(maxClosedNodes != -1 && closed.size() >= maxClosedNodes){
				throw new NoSolutionException(getLastSearchReport());
			}
			
			Node n = getBest();
			open.remove(n);
			closed.add(n);

			boolean success = n.isGoal();
			if (success) {
				goal = n;
				plan = new BacktracePlan(n);
				return plan;
			} else {
				Set<Node> succ = n.getSuccessors();

				for (Node sn : succ) {
					if (!closed.contains(sn)) {
						open.add(sn);
					} else {
						Node cNode = closed.get(closed.indexOf(sn));
						// The shortest is the best
						if (sn.getDepth() < cNode.getDepth()) {
							cNode.setParent(sn.getParent());
						}
					}
				}
			}
		}

		throw new NoSolutionException(getLastSearchReport());
	}

	@Override
	public Node getBest() {
		return open.last();
	}

	@Override
	public OpenNodes<Node> getOpenNodes(){
		return open;
	}
	
	@Override
	public BestFirstSearchReport getLastSearchReport() {
		final Set<Node> fopen = Collections.unmodifiableSet(open);
		final Set<Node> fclosed = Collections.unmodifiableSet(new HashSet<Node>(closed));
		final Node froot = root;
		final Node fgoal = goal;
		final Plan fplan = plan;
		return new BestFirstSearchReport() {

			@Override
			public Set<Node> openNodes() {
				return fopen;
			}

			@Override
			public Set<Node> closedNodes() {
				return fclosed;
			}
			
			@Override
			public Node getGoalNode() throws NoSolutionException {
				if(fgoal == null) throw new NoSolutionException(this);
				return fgoal;
			}
			
			@Override
			public Node getRootNode() {
				return froot;
			}
			
			@Override
			public boolean goalFound() {
				return fgoal != null;
			}
			
			@Override
			public Plan getPlan() {
				return fplan;
			}
		};
	}

	@Override
	public BestNodeHeuristic getHeuristic() {
		return comparator;
	}
}
