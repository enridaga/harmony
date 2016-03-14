package harmony.dsl.expression;

import harmony.dsl.thingloader.CharSequenceJavaFileObject;
import harmony.dsl.thingloader.ClassFileManager;
import harmony.dsl.thingloader.ThingTypeCreator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompileJavaSourceCodeInMemoryTest {

	Logger log = LoggerFactory.getLogger(getClass());

	ClassFileManager fileManager;

	JavaCompiler compiler;

	@Before
	public void before() {
		compiler = ToolProvider.getSystemJavaCompiler();
		fileManager = new ClassFileManager(compiler.getStandardFileManager(
				null, null, null));
	}

	@Test
	public void test() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException {
		log.info("test()");

		ThingTypeCreator pippoCreator = new ThingTypeCreator(
				ThingTypeCreator.KIND_CLASS, "ex", "Pippo");
		List<JavaFileObject> jfiles = new ArrayList<JavaFileObject>();
		String pippoSrc = pippoCreator.buildSourceCode();
		log.info("Source code Pippo:\n{}", pippoSrc);
		jfiles.add(new CharSequenceJavaFileObject(pippoCreator
				.getCanonicalName(), pippoSrc));
		compiler.getTask(null, fileManager, null, null, null, jfiles).call();

		Class<?> pippo = fileManager.getClassLoader(null).loadClass("ex.Pippo");
		Object o = pippo.getConstructor(new Class<?>[]{String.class}).newInstance("pippo");
		Assert.assertTrue(o.getClass().getCanonicalName().equals("ex.Pippo"));

		ThingTypeCreator plutoCreator = new ThingTypeCreator(
				ThingTypeCreator.KIND_CLASS, "ex", "Pluto");
		plutoCreator.setSupertype(pippo.getCanonicalName());
		String plutoSrc = plutoCreator.buildSourceCode();
		log.info("Source code Pluto:\n{}", plutoSrc);
		jfiles.add(new CharSequenceJavaFileObject(plutoCreator
				.getCanonicalName(), plutoSrc));
		compiler.getTask(null, fileManager, null, null, null, jfiles).call();
		Class<?> pluto = fileManager.getClassLoader(null).loadClass("ex.Pluto");
		Object o2 = pluto.getConstructor(new Class<?>[]{String.class}).newInstance("pluto");

		log.info("New instance of Pluto: {}", o2.getClass());
		log.info("Is its super class equals to Pippo? {}", o2.getClass()
				.getGenericSuperclass().equals(pippo));

		Assert.assertTrue(pippo.isAssignableFrom(o2.getClass()));
		Assert.assertTrue(o2.getClass().getCanonicalName().equals("ex.Pluto"));

		// does the standard classloader find Pluto & Pippo?
		Exception ex = null;
		try {
			getClass().getClassLoader().loadClass("ex.Pluto");
		} catch (ClassNotFoundException e) {
			// no!
			ex = e;
		}
		Assert.assertTrue(ex instanceof ClassNotFoundException);
	}

}