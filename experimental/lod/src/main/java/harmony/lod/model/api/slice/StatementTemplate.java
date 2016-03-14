package harmony.lod.model.api.slice;

import harmony.lod.model.api.symbol.Datatype;
import harmony.lod.model.api.symbol.IRI;
import harmony.lod.model.api.symbol.Lang;
import harmony.lod.model.api.symbol.Literal;

public interface StatementTemplate extends Slice {

	public IRI getSubject();
	
	public IRI getPredicate();
	
	public IRI getObject();

	public Datatype getDatatype();
	
	public Lang getLang();
	
	public Literal getValue();
	
	public boolean hasSubject();
	
	public boolean hasPredicate();

	public boolean hasObject();
	
	public boolean hasValue();
	
	public boolean hasDatatype();
	
	public boolean hasLang();

	public boolean matchValue();
	
	public boolean matchObject();
	
	public boolean isStatement();
}
