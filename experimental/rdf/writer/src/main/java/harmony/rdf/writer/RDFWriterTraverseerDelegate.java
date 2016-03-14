package harmony.rdf.writer;

import harmony.rdf.writer.mapping.Mapper;
import harmony.tools.traverse.GenericTraverseerDelegate;

import java.util.ArrayList;
import java.util.List;

public abstract class RDFWriterTraverseerDelegate extends
		GenericTraverseerDelegate {

	
	protected String bnode(){
		return factory.bnode();
	}
	
	private TripleFactory<?> factory;
	private Mapper mapper;

	public RDFWriterTraverseerDelegate(Mapper provider, TripleFactory<?> factory) {
		this.mapper = provider;
		this.factory = factory;

	}

	protected void _spo(String subject, String property, String object) {
		Object o = factory.spo(subject, property, object);
		handleExperience(o);
	}

	protected void spo(Object subject, String property, Object object) {
		trigger(subject);
		trigger(object);
		_spo(id(subject), property, id(object));
	}

	protected void _spll(String subject, String property, String value,
			String lang) {
		Object o = factory.spll(subject, property, value, lang);
		handleExperience(o);
	}

	protected void spll(Object subject, String property, String value, String lang) {
		trigger(subject);
		_spll(id(subject), property, value, lang);
	}

	protected void _spvd(String subject, String property, String value,
			String datatype) {
		Object o = factory.spvd(subject, property, value, datatype);
		handleExperience(o);
	}

	protected void spvd(Object subject, String property, String value,
			String datatype) {
		trigger(subject);
		_spvd(id(subject), property, value, datatype);
	}

	protected void _spv(String subject, String property, String value) {
		_spvd(subject, property, value, TripleFactory.XSD_NS + "string");
	}

	protected void spv(Object subject, String property, String value) {
		trigger(subject);
		_spv(id(subject), property, value);
	}

	protected void spv(Object subject, String property, int value) {
		trigger(subject);
		_spv(id(subject), property, value);
	}

	protected void _spv(String subject, String property, int value) {
		_spvd(subject, property, Integer.toString(value),
				TripleFactory.XSD_NS + "int");
	}

	protected void spv(Object subject, String property, boolean value) {
		trigger(subject);
		_spv(id(subject), property, value);
	}

	protected void _spv(String subject, String property, boolean value) {
		_spvd(subject, property, Boolean.toString(value),
				TripleFactory.XSD_NS + "boolean");
	}

	protected void _isA(String subject, String object) {
		Object o = factory.isA(subject, object);
		handleExperience(o);
	}

	protected void isA(Object subject, String object) {
		trigger(subject);
		_isA(id(subject), object);
	}

	protected void _lbl(String subject, String value, String lang) {
		Object o = factory.lbl(subject, value, lang);
		handleExperience(o);
	}

	protected void lbl(Object subject, String value, String lang) {
		trigger(subject);
		_lbl(id(subject), value, lang);
	}

	protected void list(Object subject, String property, Object[] objects) {
		trigger(subject);
		List<String> sobjects = new ArrayList<String>();
		for (Object o : objects) {
			trigger(o);
			sobjects.add(id(o));
		}
		_list(id(subject), property, sobjects);
	}

	protected void _list(String subject, String property, List<String> objects) {
		Object o = factory.list(subject, property, objects);
		handleExperience(o);
	}

	protected void list(Object subject, String property, List<Object> objects) {
		trigger(subject);
		List<String> sobjects = new ArrayList<String>();
		for (Object o : objects) {
			trigger(o);
			sobjects.add(id(o));
		}
		_list(id(subject), property, sobjects);
	}

	protected String id(Object o) {
		if (o instanceof String) {
			return (String) o;
		}
		return mapper.id(o);
	}

	public String type(Class<?> cls) {
		trigger(cls);
		return mapper.type(cls);
	}

	public String typeOf(Object o) {
		trigger(o);
		return mapper.typeOf(o);
	}

	public Mapper getIdentityProvider() {
		return mapper;
	}

	private void trigger(Object o) {
		getTaskMaster().traverse(o);
	}

//	public TripleFactory<?> builder() {
//		return factory;
//	}
}
