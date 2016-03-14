package harmony.dsl.expression;

import harmony.core.api.thing.Thing;
import harmony.dsl.thingloader.ThingTypeCompiler;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Types {

	private Map<String, Set<String>> types;

	private String packageName = "inmemory.";
	private ThingTypeCompiler compiler;

	public Types(String packageName) {
		this.packageName += packageName;
		types = new HashMap<String, Set<String>>();
		compiler = new ThingTypeCompiler();
	}

	public ClassLoader getClassLoader() {
		return compiler.getClassLoader();
	}

	public Map<String, Set<String>> getMap() {
		return types;
	}

	public void addType(String type) {
		if (!types.containsKey(type)) {
			types.put(type, new HashSet<String>());
		}
	}

	public void addType(String type, String superType) {
		addType(type);
		addType(superType);
		types.get(type).add(superType);
	}

	public Set<String> getTypes() {
		return Collections.unmodifiableSet(types.keySet());
	}

	/**
	 * Return an Type which is possible to instantiate
	 * 
	 * @param type
	 * @return
	 * @throws ClassNotFoundException 
	 */
	public Class<? extends Thing> toMakeNew(String type) throws ClassNotFoundException {
		return compiler.toClass(fullName(type));
	}

	public int size() {
		return types.size();
	}

	public boolean contains(String string) {
		return types.containsKey(string);
	}

	private String fullName(String type) {
		return packageName + "." + type;
	}

	@SuppressWarnings("unchecked")
	public Class<? extends Thing> getTypeClass(String type) throws ClassNotFoundException {
		try {
			return (Class<? extends Thing>) getClassLoader().loadClass(fullName(type));
		} catch (ClassNotFoundException e) {
			compiler.compile(types, packageName);
			return (Class<? extends Thing>) getClassLoader().loadClass(fullName(type));
		}

	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ExpressionToStringStyle.STYLE);
	}
}
