package harmony.blocksworld;

import harmony.blocksworld.operator.PickUp;
import harmony.blocksworld.operator.PutDown;
import harmony.blocksworld.operator.Stack;
import harmony.blocksworld.operator.Unstack;
import harmony.blocksworld.property.ArmEmpty;
import harmony.blocksworld.property.Clear;
import harmony.blocksworld.property.Holding;
import harmony.blocksworld.property.On;
import harmony.blocksworld.property.OnTable;
import harmony.core.api.domain.Domain;
import harmony.core.api.operator.Operator;
import harmony.core.api.property.Property;

public final class Blocksworld implements Domain {

	public Operator[] getOperators() {
		Operator[] op = {
				new PickUp(),
				new PutDown(),
				new Stack(),
				new Unstack()
		};
		return op;
	}

	public Property[] getProperty() {
		Property[] p = {
				new ArmEmpty(),
				new Clear(),
				new Holding(),
				new On(),
				new OnTable()
		};
		return p;
	}

}
