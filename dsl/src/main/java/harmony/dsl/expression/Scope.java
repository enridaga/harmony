package harmony.dsl.expression;

import harmony.core.api.thing.Thing;

import java.util.HashMap;

import org.apache.commons.lang3.builder.ToStringBuilder;

@SuppressWarnings("serial")
public class Scope extends HashMap<String, Thing> {

	public Scope() {

	}

	public Scope(Scope parent) {
		putAll(parent);
	}

	public Scope(Scope... toMerge) {
		for (Scope s : toMerge) {
			putAll(s);
		}
	}

	public void inherit(Scope scope) {
		putAll(scope);
	}

	@Override
	public String toString() {
		ToStringBuilder tsb = new ToStringBuilder(this, ExpressionToStringStyle.STYLE);
		for(java.util.Map.Entry<String, Thing> e : entrySet()){
			tsb.append(e.getKey(),e.getValue());
		}
		return tsb.toString();
	}
	
// TODO
//	public boolean check(Declarations vars) {
//		for (java.util.Map.Entry<String, Class<? extends Thing>> e : vars
//				.entrySet()) {
//			if(!containsKey(e.getKey())){
//				return false;
//			}else if(!(e.getValue().isAssignableFrom(get(e.getKey()).getClass()))){
//				return false;
//			}
//		}
//		return true;
//	}
}
