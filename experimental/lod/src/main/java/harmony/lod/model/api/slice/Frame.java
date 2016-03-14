package harmony.lod.model.api.slice;

import harmony.lod.model.api.symbol.IRI;

import java.util.Set;

public interface Frame extends Slice {

	public Set<StatementTemplate> asSet();
	
	public boolean hasSubject();
	
	public IRI getSubject();
	
	public boolean hasTypes();
	
	public Set<IRI> getTypes();
	
	public Set<IRI> getPredicates();
}
