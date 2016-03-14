package harmony.rdf.writer.mapping;

import harmony.rdf.writer.RDFWriterTraverseerDelegate;
import harmony.rdf.writer.TripleFactory;

import java.util.Set;

public interface Mapper {
	
	public String typeOf(Object o);
	
	public String type(Class<?> c);
	
	public void map(Class<?> c, String type);
	
	public String getContextNS();
	
	public Set<RDFWriterTraverseerDelegate> getDelegates(TripleFactory<?> factory);
	
	public void attach(Class<? extends RDFWriterTraverseerDelegate> delegateType);

	public String id(Object o);
}
