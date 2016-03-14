package harmony.core.api.assessment;

import harmony.core.api.fact.Fact;

import java.util.Set;

/**
 * 
 * @author Enrico Daga
 * 
 */
public interface AssessmentReport extends Assessment {

	/**
	 * 
	 * @return The set of facts which are shared between the condition and the
	 *         state
	 */
	public Set<Fact> getShared();

	/**
	 * 
	 * @return The set of facts which are not considered in the condition
	 */
	public Set<Fact> getIgnored();

	/**
	 * 
	 * @return The set of facts which are missing in the state, but needed by
	 *         the condition
	 */
	public Set<Fact> getMissing();

	/**
	 * 
	 * @return The set of facts which are present in the state, but negated by
	 *         the condition
	 */
	public Set<Fact> getNegated();

	/**
	 * 
	 * @return The set of facts which are not present in the state, and that are
	 *         negated by the condition
	 */
	public Set<Fact> getNegatedMissing();
	
	public void clear();
}
