package harmony.dsl.thingloader;

import java.io.IOException;
import java.security.SecureClassLoader;
import java.util.HashMap;
import java.util.Map;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardJavaFileManager;

public class ClassFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {

	private Map<String, JavaClassObject> jclassObjects;
	private ClassLoader cloader;

	public ClassFileManager(StandardJavaFileManager standardManager) {
		super(standardManager);
		jclassObjects = new HashMap<String, JavaClassObject>();
		cloader = new SecureClassLoader() {
			@Override
			protected Class<?> findClass(String name)
					throws ClassNotFoundException {
				if (jclassObjects.containsKey(name)) {
					JavaClassObject jclassObject = jclassObjects.get(name);
					byte[] b = jclassObject.getBytes();
					return super.defineClass(name, jclassObject.getBytes(), 0,
							b.length);
				} else {
					return super.findClass(name);
				}
			}
		};
	}

	@Override
	public ClassLoader getClassLoader(Location location) {
//		if (location != null && location.getName().equals("CLASS_PATH")) {
//			return getClass().getClassLoader();
//		}
		return cloader;
	}

	/**
	 * Gives the compiler an instance of the JavaClassObject so that the
	 * compiler can write the byte code into it.
	 */
	@Override
	public JavaFileObject getJavaFileForOutput(Location location,
			String className, Kind kind, FileObject sibling) throws IOException {
		jclassObjects.put(className, new JavaClassObject(className, kind));
		return jclassObjects.get(className);
	}
}
