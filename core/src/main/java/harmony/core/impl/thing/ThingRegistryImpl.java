package harmony.core.impl.thing;

import harmony.core.api.thing.Thing;
import harmony.core.api.thing.ThingRegistry;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ThingRegistryImpl implements ThingRegistry {

	private Set<Thing> registry = null;
	private Map<Class<? extends Object>, Set<Thing>> typeMap = null;

	public ThingRegistryImpl() {
		this.registry = new HashSet<Thing>();
		this.typeMap = new HashMap<Class<? extends Object>, Set<Thing>>();
	}

	public Set<? extends Thing> get(Class<? extends Thing> c) {
		Set<? extends Thing> set = null;
		synchronized (typeMap) {
			Set<? extends Thing> t = typeMap.get(c);
			if (t == null) {
				set = Collections.emptySet();
			} else {
				set = Collections.unmodifiableSet((Set<? extends Thing>) t);
			}
		}
		return set;
	}

	@Override
	public Set<? extends Thing> asSet() {
		return Collections.unmodifiableSet((Set<? extends Thing>) registry);
	}

	public boolean put(Thing o) {
		if (registry.contains(o)) {
			return false;
		}
		synchronized (registry) {
			registry.add(o);
			Set<Class<?>> types = this.getTypesOf(o);
			this.putTypes(types, o);

		}
		return true;
	}

	private void classTransitivity(Class<?> c, Set<Class<?>> classes) {
		classes.add(c);
		Class<?> sc = c.getSuperclass();
		if (sc != null) {
			this.classTransitivity(sc, classes);
		}
	}

	private void interfaceTransitivity(Class<?> c, Set<Class<?>> interfaces) {
		if (c.isInterface()) {
			interfaces.add(c);
		}
		Class<?>[] ii = c.getInterfaces();
		for (Class<?> i : ii) {
			this.interfaceTransitivity(i, interfaces);
		}
	}

	private Set<Class<?>> getTypesOf(Thing o) {
		Set<Class<?>> types = new HashSet<Class<?>>();
		this.classTransitivity(o.getClass(), types);
		this.interfaceTransitivity(o.getClass(), types);
		return types;
	}

	private void putTypes(Set<Class<?>> t, Thing o) {
		for (Class<?> c : t) {
			this.putType(c, o);
		}
	}

	private void putType(Class<?> c, Thing o) {
		synchronized (typeMap) {
			if (!typeMap.containsKey(c)) {
				typeMap.put(c, new HashSet<Thing>());
			}
			Set<Thing> s = typeMap.get(c);
			s.add(o);
		}
	}

	public Set<Class<? extends Object>> types() {
		Set<Class<? extends Object>> set = null;
		synchronized (typeMap) {
			set = Collections.unmodifiableSet(typeMap.keySet());
		}
		return set;
	}

	public int size() {
		return registry.size();
	}

	public int sizeOf(Class<?> c) {
		if (!typeMap.containsKey(c)) {
			return 0;
		}
		return typeMap.get(c).size();
	}

	public boolean remove(Class<? extends Thing> o) {
		if (!typeMap.containsKey(o)) {
			return false;
		}
		synchronized (typeMap) {
			Set<Thing> objects = typeMap.get(o);
			synchronized (registry) {
				registry.removeAll(objects);
				for (Set<Thing> values : typeMap.values()) {
					values.removeAll(objects);
				}
			}
		}
		return true;
	}

	public boolean remove(Thing o) {
		if (!registry.contains(o)) {
			return false;
		}
		synchronized (registry) {
			registry.remove(o);
			synchronized (typeMap) {
				for (Class<? extends Object> k : typeMap.keySet()) {
					typeMap.get(k).remove(o);
				}
			}
		}
		return true;
	}

	public boolean contains(Class<?> o) {
		return typeMap.containsKey(o);
	}

	public boolean contains(Thing o) {
		return registry.contains(o);
	}
}
