package harmony.dsl.thingloader;

import harmony.core.api.thing.Thing;
import harmony.core.impl.thing.Something;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

public class ThingTypeCompiler {

	private ClassFileManager fileManager;
	private JavaCompiler compiler;
	private Map<String, String> interfaceClassMap;

	public ThingTypeCompiler() {
		interfaceClassMap = new HashMap<String,String>();
		compiler = ToolProvider.getSystemJavaCompiler();
		fileManager = new ClassFileManager(compiler.getStandardFileManager(
				null, null, null));
	}

	public ClassLoader getClassLoader() {
		return fileManager.getClassLoader(null);
	}
	
	public Class<? extends Thing> toClass(String type) throws ClassNotFoundException{
		if(canMakeInstance(type)){
			return (Class<? extends Thing>) getClassLoader().loadClass(type);
		}else{
			String classType = interfaceClassMap.get(type);
			return (Class<? extends Thing>) getClassLoader().loadClass(classType);
		}
	}
	public boolean canMakeInstance(String type){
		if(interfaceClassMap.values().contains(type)){
			return true;
		}else{
			return false;
		}
	}

	public void compile(Map<String, Set<String>> types, String packageName) {
		/**
		 * Prepare the list of files to compile
		 */
		List<JavaFileObject> jfiles = new ArrayList<JavaFileObject>();

		// For each type, generate:
		for (String type : types.keySet()) {
			// 1) Interface (extending all supertypes)
			String fulltype = packageName + "." + type;
			// supertypes
			Set<String> fullsupertypes = new HashSet<String>();
			for (String supertype : types.get(type)) {
				if (supertype.toLowerCase().equals("object")) {
					fullsupertypes.add(Thing.class.getCanonicalName());
				}
				fullsupertypes.add(packageName + "." + supertype);
			}
			if (fullsupertypes.isEmpty()) {
				fullsupertypes.add(Thing.class.getCanonicalName());
			}
			ThingTypeCreator jtint = new ThingTypeCreator(
					ThingTypeCreator.KIND_INTERFACE, fulltype);
			for (String fs : fullsupertypes) {
				jtint.addInterface(fs);
			}
			// 2) A Class, extending Something and implementing all
			// the interfaces
			String className = fulltype + "Impl";
			ThingTypeCreator jtclass = new ThingTypeCreator(
					ThingTypeCreator.KIND_CLASS, className);
			jtclass.addInterface(fulltype);
			jtclass.setSupertype(Something.class.getCanonicalName());

			jfiles.add(jtint.asJavaFileObject());
			jfiles.add(jtclass.asJavaFileObject());
			
			// 3) Save the relation between the interface and the instantiable type
			interfaceClassMap.put(fulltype, className);
		}

		compiler.getTask(null, fileManager, null, null, null, jfiles).call();
	}
}
