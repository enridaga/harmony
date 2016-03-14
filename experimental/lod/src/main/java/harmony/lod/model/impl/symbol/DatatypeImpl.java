package harmony.lod.model.impl.symbol;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import harmony.lod.model.api.symbol.Datatype;
import harmony.lod.model.api.symbol.IRI;

public class DatatypeImpl implements Datatype {

	private String iri = null;

	public DatatypeImpl(String iri) {
		this.iri = iri;
	}

	public DatatypeImpl(IRI iri) {
		this.iri = iri.toString();
	}

	@Override
	public String toString() {
		return iri;
	}

	public boolean equals(Object o) {
		if (o instanceof Datatype) {
			return o.toString().equals(toString());
		}
		return false;
	}

	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(getSignature()).toHashCode();
	}

	@Override
	public String getSignature() {
		return new StringBuilder().append("^^").append("<").append(toString())
				.append(">").toString();
	}

	@Override
	public IRI toIRI() {
		return new IRIImpl(toString());
	}

}
