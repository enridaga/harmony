package harmony.core.api.effect;

import harmony.core.api.property.Property;
import harmony.core.api.thing.Thing;

/**
 * 
 * @deprecated
 *
 */
public interface MigrateFact extends Effect {

	public Property getProperty();

	public Thing getFromModel();
	
	public Thing getToModel();
	
	public int getPosition();
}
