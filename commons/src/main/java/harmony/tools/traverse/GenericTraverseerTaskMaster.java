package harmony.tools.traverse;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class GenericTraverseerTaskMaster implements TraverseerTaskMaster {

	private Set<TraverseerDelegate> traverseers = null;
	private Set<Object> traversed = null;

	public GenericTraverseerTaskMaster() {
		traverseers = new HashSet<TraverseerDelegate>();
		traversed = new HashSet<Object>();
	}

	public Set<TraverseerDelegate> getDelegates(){
		return Collections.unmodifiableSet(traverseers);
	}
	
	@Override
	public void addDelegate(TraverseerDelegate traverseer) {
		traverseer.setTaskMaster(this);
		traverseers.add(traverseer);
	}
	
	@Override
	public void traverse(Object object) {
		if(traversed.contains(object)){
			return;
		}
		traversed.add(object);
		for(TraverseerDelegate d : getDelegates()){
			d.traverse(object);
		}
	}
}
