package harmony.core.api.assessment;

public interface ScoredAssessment extends Assessment {

	public void reset(float maxScore);
	
	public float getScore();
}
