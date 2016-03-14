package harmony.core.api.domain;

import harmony.core.api.operator.Operator;
import harmony.core.api.property.Property;

public interface Domain {

	public Operator[] getOperators();
	
	public Property[] getProperty();

}
