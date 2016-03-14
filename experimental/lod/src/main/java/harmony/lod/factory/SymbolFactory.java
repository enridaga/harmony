package harmony.lod.factory;

import harmony.lod.model.api.symbol.Datatype;
import harmony.lod.model.api.symbol.IRI;
import harmony.lod.model.api.symbol.Lang;
import harmony.lod.model.api.symbol.Literal;

public interface SymbolFactory {

	public Datatype datatype(String string);
	
	public Lang lang(String lang);
	
	public IRI iri(String iri);

	public Literal literal(String value);

	public Literal literall(String value, String lang);

	public Literal literald(String value, String datatype);
}
