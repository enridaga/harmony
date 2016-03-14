package harmony.planner.bestfirst;


public final class ActionCostCache extends ScoreCache implements ScoreProvider {

	public ActionCostCache() {
		setProvider(this);
	}

	@Override
	public int compute(Node n) {
		if(n.isRoot()){
			return 0;
		}
		// Action cost
		int actionCost = n.getAction().cost();
		int parentActionCost = getScore(n.getParent());
		actionCost = actionCost + parentActionCost;
		
		return actionCost;
	}
}
