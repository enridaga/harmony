package harmony.core.impl.effect;

import harmony.core.api.effect.CompositeEffect;
import harmony.core.api.effect.Effect;
import harmony.core.api.effect.GroundEffect;
import harmony.core.api.fact.Fact;
import harmony.core.api.state.State;
import harmony.core.api.thing.Thing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CompositeEffectImpl implements CompositeEffect {

	private List<Effect> effects = new ArrayList<Effect>();
	
	@Override
	public GroundEffect asGroundEffect(final State state) {
		final Set<Thing> toCreate = new HashSet<Thing>();
		final Set<Thing> toDestroy = new HashSet<Thing>();
		final Set<Fact> toAdd = new HashSet<Fact>();
		final Set<Fact> toRemove = new HashSet<Fact>();
		for(Effect e : effects){
			GroundEffect gf = e.asGroundEffect(state);
			toAdd.addAll(Arrays.asList(gf.add()));
			toRemove.addAll(Arrays.asList(gf.remove()));
			toCreate.addAll(Arrays.asList(gf.create()));
			toDestroy.addAll(Arrays.asList(gf.destroy()));
		}
		
		return new GroundEffect() {
			
			@Override
			public Fact[] remove() {
				return toRemove.toArray(new Fact[toRemove.size()]);
			}
			
			@Override
			public Fact[] add() {
				return toAdd.toArray(new Fact[toAdd.size()]);
			}

			@Override
			public Thing[] create() {
				return toCreate.toArray(new Thing[toCreate.size()]);
			}

			@Override
			public Thing[] destroy() {
				return toDestroy.toArray(new Thing[toDestroy.size()]);
			}
		};
	}

	@Override
	public void append(Effect effect) {
		effects.add(effect);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(and ");
		for(Effect c : effects){
			sb.append(" ");
			sb.append(c.toString());
		}
		sb.append(")");
		return sb.toString();
	}
}
