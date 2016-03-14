package harmony.rdf.writer.mapping;

import harmony.core.utils.TypesExtractor;
import harmony.rdf.writer.RDFWriterTraverseerDelegate;
import harmony.rdf.writer.TripleFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractMapper implements Mapper {

	public static final String DefaultType = "http://www.w3.org/2002/07/owl#Thing";

	private Map<Class<?>, String> typesMap;
	private TypesExtractor typesExtractor;
	private String contextNs;
	private Set<Class<? extends RDFWriterTraverseerDelegate>> delegates;

	public AbstractMapper(String contextNs) {
		this.contextNs = contextNs;
		typesExtractor = new TypesExtractor();

		typesMap = new HashMap<Class<?>, String>();

		delegates = new HashSet<Class<? extends RDFWriterTraverseerDelegate>>();
	}

	public String getContextNS() {
		return contextNs;
	}

	public final String type(Class<?> o) {
		if (typesMap.containsKey(o)) {
			return typesMap.get(o);
		} else {
			return DefaultType;
		}
	}

	public final String typeOf(Object o) {
		List<Class<?>> types = typesExtractor.extract(o);
		for (Class<?> cls : types) {
			if (typesMap.containsKey(cls)) {		
				return typesMap.get(cls);
			}
		}
		return DefaultType;
	}

	public void map(Class<?> type, String typeId) {
		typesMap.put(type, typeId);
	}

	public Set<RDFWriterTraverseerDelegate> getDelegates(
			TripleFactory<?> factory) {
		Set<RDFWriterTraverseerDelegate> instances = new HashSet<RDFWriterTraverseerDelegate>();
		for (Class<? extends RDFWriterTraverseerDelegate> cl : delegates) {
			try {
				Constructor<? extends RDFWriterTraverseerDelegate> con = cl
						.getConstructor(Mapper.class,
								TripleFactory.class);
				instances.add(con.newInstance(this, factory));
			} catch (SecurityException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (InstantiationException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return instances;
	}

	@Override
	public void attach(Class<? extends RDFWriterTraverseerDelegate> delegateType) {
		delegates.add(delegateType);
	}
}
