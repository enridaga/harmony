package harmony.tools.traverse;

public interface TraverseerDelegate extends Traverseer {
	
	public TraverseerTaskMaster getTaskMaster();
	
	public void setTaskMaster(TraverseerTaskMaster taskMaster);
	
}
