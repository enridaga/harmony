package harmony.rdf.writer;

import java.util.List;

/**
 * 
 * Basic interface for defining triples in an api independent way.
 * Implementation can support different apis, for example returning a Jena
 * Statement object from the build() method
 * 
 * @author Enrico Daga
 * 
 */
public interface TripleFactory<T> {

	public final String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	public final String RDFS_NS = "http://www.w3.org/2000/01/rdf-schema#";
	public final String XSD_NS = "http://www.w3.org/2001/XMLSchema#";

	
	public String bnode();
	
	public T spo(String subject, String property, String object);

	public T spll(String subject, String property, String value, String lang);

	public T spvd(String subject, String property, String value,
			String datatype);

	public T isA(String subject, String object);

	public T lbl(String subject, String value, String lang);

	public T list(String subject, String property, String[] objects);

	public T list(String subject, String property, List<String> objects);

}
