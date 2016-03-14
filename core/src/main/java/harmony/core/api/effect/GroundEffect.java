package harmony.core.api.effect;

import harmony.core.api.fact.Fact;
import harmony.core.api.thing.Thing;

public interface GroundEffect {
	
	public Fact[] add();
	
	public Fact[] remove();
	
	public Thing[] create();
	
	public Thing[] destroy();
}
