package harmony.core.api.property;

import harmony.core.api.state.State;
import harmony.core.api.thing.Thing;
import harmony.core.impl.property.DerivedPropertyException;

public interface DerivableProperty extends Property {

	public boolean isDerivable(State state, Thing... things) throws DerivedPropertyException;

}
