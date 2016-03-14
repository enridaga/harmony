package harmony.dsl.expression;

import harmony.core.api.thing.Thing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Declarations {
	private List<String> orderedKeys;
	private List<Class<? extends Thing>> orderedValues;

	public Declarations() {
		orderedKeys = new ArrayList<String>();
		orderedValues = new ArrayList<Class<? extends Thing>>();
	}

	public List<String> getOrderedKeys() {
		return Collections.unmodifiableList(orderedKeys);
	}

	public List<Class<? extends Thing>> getOrderedValues() {
		return Collections.unmodifiableList(orderedValues);
	}

	public Scope bind(Thing... objects) throws IncorrectBindingException {
		Scope scope = new Scope();
		int index = 0;
		for (Thing thing : objects) {
			if (orderedKeys.get(index) == null) {
				throw new IncorrectBindingException(
						"Variable not declared for object at " + index);
			}
			String variable = orderedKeys.get(index);
			Class<? extends Thing> type = get(variable);
			if (type.isAssignableFrom(thing.getClass())) {
				scope.put(variable, thing);
			} else {
				throw new IncorrectBindingException("Invalid type for "
						+ variable + ". Expected: " + type + " was: "
						+ thing.getClass());
			}
			index++;
		}
		return scope;
	}

	public Class<? extends Thing> get(String key) {
		int index = orderedKeys.indexOf(key);
		return orderedValues.get(index);
	}

	public void put(String arg0, Class<? extends Thing> arg1) {
		orderedKeys.add(arg0);
		orderedValues.add(arg1);
	}

	public static Declarations emptyDeclarations() {
		return unmodifiableDeclarations(new Declarations());
	}

	public static Declarations unmodifiableDeclarations(
			Declarations declarations) {
		return new UnmodifiableDeclarations(declarations);
	}

	private static class UnmodifiableDeclarations extends Declarations {
		boolean locked = false;
		private Declarations privateDeclarations = null;

		public UnmodifiableDeclarations(Declarations map) {
			privateDeclarations = map;
			locked = true;
		}

		@Override
		public void put(String key, Class<? extends Thing> value) {
			if (locked) {
				throw new UnsupportedOperationException(
						"Cannot modify an immutable class");
			} else {
				privateDeclarations.put(key, value);
			}
		}
	}

	public int size() {
		return orderedKeys.size();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ExpressionToStringStyle.STYLE);
	}
}
