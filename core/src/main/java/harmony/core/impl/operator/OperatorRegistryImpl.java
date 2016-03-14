package harmony.core.impl.operator;

import harmony.core.api.operator.Operator;
import harmony.core.api.operator.OperatorRegistry;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OperatorRegistryImpl implements OperatorRegistry {

	protected Map<String, Operator> byName = null;

	public OperatorRegistryImpl() {
		byName = new HashMap<String, Operator>();

	}

	public void register(Operator operator) {
		synchronized (byName) {
			byName.put(operator.getName(), operator);
		}
	}

	public void unregister(String operatorName) {
		synchronized (byName) {
			byName.remove(operatorName);
		}
	}

	public boolean isRegistered(String operatorName) {
		return byName.containsKey(operatorName);
	}

	public Operator getOperator(String name) {
		return byName.get(name);
	}

	public int size() {
		return byName.size();
	}

	public Operator[] toArray() {
		return byName.values().toArray(new Operator[byName.values().size()]);
	}

	public Collection<Operator> asCollection() {
		return Collections.unmodifiableCollection(byName.values());
	}

	public OperatorRegistry clone() {
		try {
			return (OperatorRegistry) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
}
