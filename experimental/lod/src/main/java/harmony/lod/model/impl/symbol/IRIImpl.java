package harmony.lod.model.impl.symbol;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import harmony.lod.model.api.symbol.IRI;

public class IRIImpl implements IRI {

	private String iri = null;
	
	public IRIImpl(String iri){
		this.iri = iri;
	}
	
	@Override
	public String toString(){
		return iri;
	}
	
	public String getSignature(){
		StringBuilder sb = new StringBuilder();
		return sb.append("<").append(iri).append(">").toString();
	}
	
	public boolean equals(Object o){
		if(o instanceof IRI){
			return o.toString().equals(toString());
		}
		return false;
	}
	
	public int hashCode(){
		return new HashCodeBuilder(17, 31).append(getSignature()).toHashCode();
	}
}
