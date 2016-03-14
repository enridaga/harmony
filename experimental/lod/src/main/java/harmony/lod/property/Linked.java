package harmony.lod.property;

import harmony.core.impl.property.BasicProperty;

public class Linked extends BasicProperty {
	@SuppressWarnings("unchecked")
	public Linked() {
		super("Linked");
	}

	/**
	 * This property is symmetric
	@Override
	public boolean isDerivable(State state, Thing... things)
			throws DerivedPropertyException {
		List<Fact> linked = state.getFactRegistry().getFacts(this);
		for (Fact f : linked) {
			if(f.getModel(0).equals(things[1])
					&& f.getModel(1).equals(things[0])) {
				return true;
			}
		}
		return false;
	}
	*/
}
