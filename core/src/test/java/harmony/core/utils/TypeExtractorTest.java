package harmony.core.utils;

import harmony.core.api.thing.Thing;
import harmony.core.impl.thing.Something;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TypeExtractorTest {
	TypesExtractor ex = new TypesExtractor();

	Logger log = LoggerFactory.getLogger(getClass());

	@Test
	public void test() {
		Object o = new Object();
		List<Class<?>> types = ex.extract(o);
		Assert.assertTrue(types.size() == 1);
	}
	
	@Test
	public void test1() {
		List<Class<?>> types = ex.extract(this);
		Assert.assertTrue(types.size() == 2);
	}
	
	@Test
	public void test2() {
		Thing m = new Something("aModel");
		log.info("Types of Something");
		List<Class<?>> types = ex.extract(m);
		for(Class<?> c : types){
			log.info(" - {}: {}", getTypeOfType(c), c.getCanonicalName());
		}
		Assert.assertTrue(types.size() == 3);
	}

	@Test
	public void test3() {
		@SuppressWarnings("rawtypes")
		List l = new ArrayList();
		log.info("Types of ArrayList:");
		List<Class<?>> types = ex.extract(l);
		for(Class<?> c : types){
			log.info(" - {}: {}", getTypeOfType(c), c.getCanonicalName());
		}
		Assert.assertTrue(types.size() == 10);
	}

	
	private String getTypeOfType(Class<?> c){
		if(c.isAnnotation()){
			return "Annotation";
		}
		if(c.isEnum()){
			return "Enum";
		}
		if(c.isAnonymousClass()){
			return "AnonimousClass";
		}
		if(c.isInterface()){
			return "Interface";
		}
		if(c.isArray()){
			return "Array";
		}
		return "Class";
	}
}
