package harmony.lod.property;

import harmony.core.api.fact.Fact;
import harmony.core.api.property.DerivableProperty;
import harmony.core.api.state.State;
import harmony.core.api.thing.Thing;
import harmony.core.impl.property.BasicProperty;
import harmony.lod.model.api.dataset.Dataset;
import harmony.lod.model.api.slice.Frame;
import harmony.lod.model.api.slice.Slice;
import harmony.lod.model.api.slice.StatementTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HasSlice extends BasicProperty implements DerivableProperty {

	@SuppressWarnings("unchecked")
	public HasSlice() {
		super("HasSlice", Dataset.class, Slice.class);
	}

	@Override
	public boolean isDerivable(State state, Thing... things) {
		Dataset d = (Dataset) things[0];
		Slice s = (Slice) things[1];
		List<Fact> hasSlice = new ArrayList<Fact>(state.getFactRegistry()
				.getFacts(new HasSlice()));
		List<Fact> hasSlice2 = state.getFactRegistry().getFacts(d);
		hasSlice.retainAll(hasSlice2);
		Set<Slice> sss = new HashSet<Slice>();
		for (Fact f : hasSlice) {
			Slice ss = (Slice) f.getThing(1);
			sss.add(ss);
			if (s.includes(ss)) {
				return true;
			}
		}
		if (s instanceof Frame) {
			for (StatementTemplate t : ((Frame) s).asSet()) {
				boolean match = false;
				for (Slice as : sss) {
					if (t.includes(as)) {
						match = true;
						break;
					}
				}
				if (!match) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
