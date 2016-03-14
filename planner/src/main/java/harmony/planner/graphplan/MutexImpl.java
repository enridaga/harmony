package harmony.planner.graphplan;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class MutexImpl<T> implements Mutex<T> {
	private Map<T, Set<T>> map;

	public MutexImpl(Mutex<T> mutex) {
		this();

	}

	public java.util.Set<T> getMutex(T object) {
		if (!map.containsKey(object)) {
			return Collections.emptySet();
		}
		return map.get(object);
	};

	public boolean hasMutex(T object) {
		return map.containsKey(object);
	};

	public void put(T mutex0, T mutex1) {
		if (!map.containsKey(mutex0)) {
			map.put(mutex0, new HashSet<T>());
		}
		if (!map.containsKey(mutex1)) {
			map.put(mutex1, new HashSet<T>());
		}
		map.get(mutex0).add(mutex1);
		map.get(mutex1).add(mutex0);
	};

	public MutexImpl() {
		map = new HashMap<T, Set<T>>();
	}

	public Map<T, Set<T>> asMap() {
		return new HashMap<T, Set<T>>(map);
	}
}
