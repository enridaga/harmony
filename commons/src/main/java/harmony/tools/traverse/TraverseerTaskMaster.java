package harmony.tools.traverse;

import java.util.Set;

public interface TraverseerTaskMaster extends Traverseer {

	public void addDelegate(TraverseerDelegate traverser);

	public Set<TraverseerDelegate> getDelegates();

	public void handleExperience(Object experience);

}
