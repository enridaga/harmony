package harmony.core.impl.condition;

import harmony.core.api.condition.Condition;
import harmony.core.api.condition.ConditionVisitor;
import harmony.core.api.parameters.ParametersOwner;
import harmony.core.api.thing.Thing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Exists implements Condition, ParametersOwner {

	private List<Class<? extends Thing>> types = new ArrayList<Class<? extends Thing>>();

	public Exists(Class<? extends Thing>... types){
		setParametersTypes(types);
	}
	
	protected void setParametersTypes(Class<? extends Thing>... types) {
		this.types.addAll(Arrays.asList(types));
	}

	public List<Class<? extends Thing>> getParametersTypes() {
		return types;
	}

	@Override
	public int getNumberOfParameters() {
		return types.size();
	}

	public abstract Condition getCondition(Thing... things);

	@Override
	public int compareTo(Condition o) {
		if(o instanceof Forall){
			return -1;
		}
		if(o instanceof And){
			return 1;
		}
		if (o instanceof Exists) {
			int osize = ((Exists) o).getParametersTypes().size();
			int tsize = getParametersTypes().size();
			if (osize > tsize) {
				return -1;
			} else if (tsize > osize) {
				return 1;
			} else {
				return o.hashCode() > hashCode() ? hashCode() : o.hashCode();
			}
		}
		return 1;
	}

	@Override
	public boolean accept(ConditionVisitor assessment) {
		return assessment.visit(this);
	}
	

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("(exists ");
		sb.append("(");
		for(Class<?> c : types){
			sb.append(" ");
			sb.append(c.getCanonicalName().toString());
		}
		sb.append(") ");
		sb.append(" <condition> ");
		sb.append(")");
		return sb.toString();
	}
}
