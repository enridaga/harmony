package harmony.core.impl.effect;

import harmony.core.api.effect.GroundEffect;
import harmony.core.api.effect.MigrateFact;
import harmony.core.api.fact.Fact;
import harmony.core.api.property.Property;
import harmony.core.api.state.State;
import harmony.core.api.thing.Thing;
import harmony.core.impl.fact.BasicFact;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @deprecated
 */
public class MigrateFactImpl implements MigrateFact {

	private int position = 0;
	private Property p;
	private Thing from;
	private Thing to;

	public MigrateFactImpl(Property p, int position, Thing from, Thing to) {
		this.p = p;
		this.from = from;
		this.to = to;
	}

	@Override
	public Property getProperty() {
		return p;
	}

	@Override
	public GroundEffect asGroundEffect(final State state) {
		final Set<Fact> remove = new HashSet<Fact>();
		remove.addAll(state.getFactRegistry().getFacts(getFromModel()));
		remove.retainAll(state.getFactRegistry().getFacts(getProperty()));
		final Set<Fact> add = new HashSet<Fact>();
		for (Fact f : remove) {
			if (f.getThing(getPosition()).equals(getFromModel())) {
				List<Thing> m = f.getThings();
				Thing[] par = m.toArray(new Thing[m.size()]);
				par[getPosition()] = getToModel();
				Fact newFact = new BasicFact(getProperty(),
						par);
				add.add(newFact);
			} else {
				remove.remove(f);
			}
		}
		
		return new GroundEffect() {

			@Override
			public Fact[] remove() {
				return remove.toArray(new Fact[remove.size()]);
			}

			@Override
			public Fact[] add() {
				return add.toArray(new Fact[add.size()]);
			}

			@Override
			public Thing[] create() {
				return new Thing[0];
			}

			@Override
			public Thing[] destroy() {
				return new Thing[0];
			}
		};
	}

	@Override
	public Thing getFromModel() {
		return from;
	}

	@Override
	public Thing getToModel() {
		return to;
	}

	@Override
	public int getPosition() {
		return position;
	}
}
