package harmony.lod.factory;

import harmony.lod.model.api.symbol.Datatype;
import harmony.lod.model.api.symbol.IRI;
import harmony.lod.model.api.symbol.Lang;
import harmony.lod.model.api.symbol.Literal;
import harmony.lod.model.impl.symbol.DatatypeImpl;
import harmony.lod.model.impl.symbol.IRIImpl;
import harmony.lod.model.impl.symbol.LangImpl;
import harmony.lod.model.impl.symbol.LiteralImpl;

final class SymbolFactoryImpl implements SymbolFactory {

	@Override
	public Datatype datatype(String string) {
		return new DatatypeImpl(iri(string));
	}

	@Override
	public Lang lang(String lang) {
		return new LangImpl(lang);
	}

	@Override
	public IRI iri(String iri) {
		return new IRIImpl(iri);
	}

	@Override
	public Literal literal(String value) {
		return new LiteralImpl(value);
	}

	@Override
	public Literal literall(String value, String lang) {
		return new LiteralImpl(value, lang(lang));
	}

	@Override
	public Literal literald(String value, String datatype) {
		return new LiteralImpl(value, datatype(datatype));
	}
}
