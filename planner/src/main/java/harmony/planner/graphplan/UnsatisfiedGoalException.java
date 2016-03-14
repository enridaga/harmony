package harmony.planner.graphplan;

import harmony.core.api.fact.Fact;

import java.util.Set;

public class UnsatisfiedGoalException extends FailureException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Set<Fact> goalSet;
	private int time;

	public UnsatisfiedGoalException(Set<Fact> unsatisfied, int time) {
		goalSet = unsatisfied;
		this.time = time;
	}

	public Set<Fact> getGoal() {
		return this.goalSet;
	}

	public int getTime() {
		return time;
	}
}
