package harmony.blocksworld;

import harmony.blocksworld.utils.BlocksworldFactsBuilder;
import harmony.core.api.fact.Fact;
import harmony.core.api.goal.Goal;
import harmony.core.api.problem.Problem;
import harmony.core.api.thing.Thing;
import harmony.core.impl.state.InitialState;

import java.util.HashSet;
import java.util.Set;

public class BlocksworldProblem implements Problem {

	private Fact[] init = null;
	private Goal goal = null;
	

	/**
	 * 
	 * @param stacks
	 *            An array of comma separated strings. Each string is a stack.
	 *            Each comma separated string item is a block
	 */
	public void setInitialState(String... stacks) {
		BlocksworldFactsBuilder fb = new BlocksworldFactsBuilder();
		for (String s : stacks) {
			fb.append(s);
		}
		init = fb.toFacts();
	}

	/**
	 * 
	 * @param stacks
	 *            An array of comma separated strings. Each string is a stack.
	 *            Each comma separated string item is a block
	 */
	public void setGoal(String... stacks) {
		BlocksworldFactsBuilder fb = new BlocksworldFactsBuilder();
		for (String s : stacks) {
			fb.append(s);
		}
		goal = fb.toGoal();
	}

	public InitialState getInitialState() {
		return new InitialState(init, new Thing[]{});
	}

	public Goal getGoal() {
		return goal;
	}

	public Thing[] getObjects() {
		Set<Thing> blocks = new HashSet<Thing>();
		for (Fact s : init) {
			blocks.addAll(s.getThings());
		}
		return blocks.toArray(new Block[blocks.size()]);
	}

}
