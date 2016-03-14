package harmony.core.impl.effect;

import harmony.core.api.effect.Effect;
import harmony.core.api.effect.GroundEffect;
import harmony.core.api.fact.Fact;
import harmony.core.api.property.Property;
import harmony.core.api.state.State;
import harmony.core.api.thing.Thing;
import harmony.core.impl.fact.BasicFact;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public final class BasicEffect implements Effect {

	private List<Fact> toAdd;
	private List<Fact> toRemove;

	public BasicEffect() {
		toAdd = new ArrayList<Fact>();
		toRemove = new ArrayList<Fact>();
	}

	public BasicEffect(Fact toAdd) {
		this();
		toAdd(toAdd);
	}

	public BasicEffect(Property at, Thing... things) {
		this(new BasicFact(at, things));
	}

	public BasicEffect toAdd(Fact... facts) {
		toAdd.addAll(Arrays.asList(facts));
		return this;
	}

	public BasicEffect toRemove(Fact... facts) {
		toRemove.addAll(Arrays.asList(facts));
		return this;
	}

	@Override
	public GroundEffect asGroundEffect(final State state) {
		final Fact[] add = this.toAdd.toArray(new Fact[toAdd.size()]);
		final Fact[] remove = this.toRemove.toArray(new Fact[toRemove.size()]);
		return new GroundEffect() {

			@Override
			public Fact[] remove() {
				return remove;
			}

			@Override
			public Fact[] add() {
				return add;
			}

			@Override
			public Thing[] create() {
				return new Thing[0];
			}

			public Thing[] destroy() {
				return new Thing[0];
			}
		};
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
