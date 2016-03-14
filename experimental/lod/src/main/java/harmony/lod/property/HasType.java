package harmony.lod.property;

import harmony.core.api.property.DerivableProperty;
import harmony.core.api.state.State;
import harmony.core.api.thing.Thing;
import harmony.core.impl.property.BasicProperty;
import harmony.lod.model.api.slice.Frame;
import harmony.lod.model.api.slice.Slice;
import harmony.lod.model.api.slice.StatementTemplate;
import harmony.lod.model.api.symbol.IRI;
import harmony.lod.model.impl.symbol.IRIImpl;

public class HasType extends BasicProperty implements DerivableProperty {

	@SuppressWarnings("unchecked")
	public HasType() {
		super("HasType", Slice.class, IRI.class);
	}

	@Override
	public boolean isDerivable(State state, Thing... things) {
		if (things[0] instanceof Frame) {
			Frame f = (Frame) things[0];
			for (IRI i : f.getTypes()) {
				if (i.equals(things[1])) {
					return true;
				}
			}
		} else if (things[0] instanceof StatementTemplate) {
			StatementTemplate t = (StatementTemplate) things[0];
			if (t.hasPredicate() && t.hasObject()) {
				if (t.getPredicate()
						.equals(new IRIImpl(
								"http://www.w3.org/1999/02/22-rdf-syntax-ns#type"))) {
					if (t.getObject().equals(things[1])) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
