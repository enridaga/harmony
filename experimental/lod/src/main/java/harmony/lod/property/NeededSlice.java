package harmony.lod.property;

import harmony.core.api.fact.Fact;
import harmony.core.api.property.DerivableProperty;
import harmony.core.api.state.State;
import harmony.core.api.thing.Thing;
import harmony.core.impl.assessment.FactsCollector;
import harmony.core.impl.property.BasicProperty;
import harmony.core.impl.property.DerivedPropertyException;
import harmony.core.impl.state.StaticState;
import harmony.lod.model.api.dataset.OutputDataset;
import harmony.lod.model.api.slice.Slice;
import harmony.planner.bestfirst.Node;

import java.util.HashSet;
import java.util.Set;

public class NeededSlice extends BasicProperty implements DerivableProperty {

	@SuppressWarnings("unchecked")
	public NeededSlice() {
		super("NeededSlice", Slice.class, OutputDataset.class);
	}

	@Override
	public boolean isDerivable(State state, Thing... things)
			throws DerivedPropertyException {
		
		if (state instanceof Node) {
			Set<Slice> needed = neededFor((Node) state,
					(OutputDataset) things[1]);
			if(needed.contains((Slice)things[0])){
				return true;
			}else{
				return false;
			}
		}
		throw new DerivedPropertyException(
				"This property cannot be used on states which are not instances of Node");

	}

	private Set<Slice> neededFor(Node node, OutputDataset d) {
		// Get the needed slices in the goal state
		StaticState s = new StaticState(new Fact[] {}, new Thing[]{});
		FactsCollector colly = new FactsCollector(s);
		node.getPlannerInput().getGoal().asCondition().accept(colly);
		Set<Fact> missing = colly.getMissing();
		// Collect the needed slices
		Set<Slice> slices = new HashSet<Slice>();
		for (Fact f : missing) {
			// If the fact is HasSlice and the output dataset is the one of this
			// action
			if (f.getProperty().getName().equals("HasSlice")
					&& f.getThing(0).equals(d)) {
				slices.add((Slice) f.getThing(1));
			}
		}
		return slices;
	}
}
