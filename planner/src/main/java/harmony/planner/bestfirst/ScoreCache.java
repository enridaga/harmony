package harmony.planner.bestfirst;


import java.util.HashMap;
import java.util.Map;

public class ScoreCache {
	private ScoreProvider scoreProvider = null;
	protected Map<Node, Integer> scoreMap = new HashMap<Node, Integer>();

	public ScoreCache() {
	}

	public void setProvider(ScoreProvider provider) {
		this.scoreProvider = provider;
	}

	public ScoreCache(ScoreProvider provider) {
		setProvider(provider);
	}

	public int getScore(Node n) {
		if (!scoreMap.containsKey(n)) {
			refreshScore(n);
		}
		return scoreMap.get(n);
	}

	public void refreshScore(Node n) {
		if (scoreProvider == null) {
			throw new IllegalStateException("Provider not set.");
		}
		scoreMap.put(n, scoreProvider.compute(n));
	}
}
