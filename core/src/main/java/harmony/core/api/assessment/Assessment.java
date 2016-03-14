package harmony.core.api.assessment;

import harmony.core.api.condition.ConditionVisitor;
import harmony.core.api.state.State;

/**
 * 
 * @author Enrico Daga
 * 
 */
public interface Assessment extends ConditionVisitor {

	/**
	 * 
	 * @return The state object of the assessment
	 */
	public State getState();
}
