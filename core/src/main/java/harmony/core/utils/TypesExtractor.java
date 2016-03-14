package harmony.core.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TypesExtractor {

	public List<Class<?>> extract(Object o) {
		List<Class<?>> types =  getTypesOf(o);
		Collections.sort(types, new TypesComparator());
		return Collections.unmodifiableList(types);
	}

	private void classTransitivity(Class<?> c, List<Class<?>> classes) {
		classes.add(c);
		Class<?> sc = c.getSuperclass();
		if (sc != null) {
			this.classTransitivity(sc, classes);
		}
	}

	private void interfaceTransitivity(Class<?> c, List<Class<?>> interfaces) {
		if (c.isInterface()) {
			interfaces.add(c);
		}
		Class<?>[] ii = c.getInterfaces();
		for (Class<?> i : ii) {
			this.interfaceTransitivity(i, interfaces);
		}
	}

	private List<Class<?>> getTypesOf(Object o) {
		List<Class<?>> types = new ArrayList<Class<?>>();
		this.classTransitivity(o.getClass(), types);
		this.interfaceTransitivity(o.getClass(), types);
		return types;
	}

	public class TypesComparator implements Comparator<Class<?>> {
		@Override
		public int compare(Class<?> arg0, Class<?> arg1) {
			
			if((!arg0.equals(Object.class)) && arg1.equals(Object.class)){
				return -1;
			}else if((!arg1.equals(Object.class)) && arg0.equals(Object.class)){
				return 1;
			}
			
			if(arg0.isAssignableFrom(arg1)){
				return 1;
			}else if(arg1.isAssignableFrom(arg0)){
				return -1;
			}
			
			return arg0.hashCode() > arg1.hashCode() ? -1: 1;
		}
	}
}
