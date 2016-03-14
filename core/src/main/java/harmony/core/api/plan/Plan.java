package harmony.core.api.plan;

import harmony.core.api.operator.GroundAction;

import java.util.List;

public interface Plan {

	public int size();
	
	public List<GroundAction> getActions();
}
