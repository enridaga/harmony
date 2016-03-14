package harmony.core.impl.effect;

import harmony.core.api.effect.Effect;
import harmony.core.api.effect.GroundEffect;
import harmony.core.api.effect.ParametrizedEffect;
import harmony.core.api.state.State;
import harmony.core.api.thing.Thing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public abstract class ForallEffect implements ParametrizedEffect {
	List<Class<? extends Thing>> params = new ArrayList<Class<? extends Thing>>();

	public ForallEffect(Class<? extends Thing>... types) {
		setParameters(types);
	}

	public void setParameters(Class<? extends Thing>... types) {
		params = Arrays.asList(types);
	};

	@Override
	public int getNumberOfParameters() {
		return params.size();
	}

	@Override
	public List<Class<? extends Thing>> getParametersTypes() {
		return params;
	}

	public abstract Effect getEffect(Thing... things);

	@Override
	public GroundEffect asGroundEffect(State state) {
		Iterator<List<Thing>> it = state.getParametersRegistry().iterator(this);
		CompositeEffectImpl cei = new CompositeEffectImpl();
		while (it.hasNext()) {
			List<Thing> l = it.next();
			cei.append(this.getEffect(l.toArray(new Thing[l.size()])));
		}
		return cei.asGroundEffect(state);
	}
	

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(forall");
		for(Class<?> c: params){
			sb.append(" ");
			sb.append(c.toString());
		}
		sb.append(")").toString();
		return sb.toString();
	}
}
