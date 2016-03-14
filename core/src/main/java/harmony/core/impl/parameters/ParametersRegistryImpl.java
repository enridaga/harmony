package harmony.core.impl.parameters;

import harmony.core.api.operator.ParametersRegistry;
import harmony.core.api.parameters.ParametersOwner;
import harmony.core.api.thing.Thing;
import harmony.core.api.thing.ThingRegistry;
import harmony.core.utils.Combinator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ParametersRegistryImpl implements ParametersRegistry {

	private ThingRegistry things = null;

	public ParametersRegistryImpl(ThingRegistry modereg) {
		things = modereg;
	}

	protected List<List<Thing>> getCandidates(ThingRegistry registry,
			List<Class<? extends Thing>> paramTypes) {
		List<List<Thing>> candidates = new ArrayList<List<Thing>>();
		for (int i = 0; i < paramTypes.size(); i++) {
			Set<Thing> m = new HashSet<Thing>();
			Class<? extends Thing> type = paramTypes.get(i);
			m.addAll(registry.get(type));
			candidates.add(new ArrayList<Thing>(m));
		}
		return candidates;
	}

	protected List<Integer> getDimensions(List<List<Thing>> candidates) {

		List<Integer> dimensions = new ArrayList<Integer>();
		for (List<Thing> candy : candidates) {
			dimensions.add(candy.size() - 1);
		}
		return dimensions;
	}

	@Override
	public Iterator<List<Thing>> iterator(ParametersOwner owner) {
		final List<List<Thing>> candidates = getCandidates(things,
				owner.getParametersTypes());
		final List<Integer> dimensions = getDimensions(candidates);
		final Combinator combo = new Combinator(dimensions);
		if (things.types().containsAll(owner.getParametersTypes())) {
			return new Iterator<List<Thing>>() {

				@Override
				public boolean hasNext() {
					return combo.hasNext();
				}

				@Override
				public List<Thing> next() {
					List<Integer> indexes = combo.next();
					List<Thing> parameters = new ArrayList<Thing>();
					for (int z = 0; z < indexes.size(); z++) {
						List<Thing> candy = candidates.get(z);
						parameters.add(z, candy.get(indexes.get(z)));
					}
					return parameters;
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}

			};
		} else {
			return new Iterator<List<Thing>>() {
				@Override
				public boolean hasNext() {
					return false;
				}

				@Override
				public List<Thing> next() {
					return null;
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}
			};
		}
	}
}
