package harmony.tools.traverse;


public abstract class GenericTraverseerDelegate implements TraverseerDelegate {

	private TraverseerTaskMaster delegating = null;

	public GenericTraverseerDelegate() {
	}

	@Override
	public final TraverseerTaskMaster getTaskMaster() {
		return delegating;
	}

	@Override
	public final void setTaskMaster(TraverseerTaskMaster delegating) {
		this.delegating = delegating;
	}
	
	@Override
	public void handleExperience(Object object) {
		getTaskMaster().handleExperience(object);	
	}
}
