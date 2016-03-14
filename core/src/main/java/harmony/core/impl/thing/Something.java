package harmony.core.impl.thing;

import harmony.core.api.thing.Thing;

import org.apache.commons.lang3.builder.HashCodeBuilder;


public class Something implements Thing {

	private String signature = null;
	private int hashCode = -1;
	
	public Something(String signature){
		this.signature = signature;
		this.hashCode = new HashCodeBuilder().append(signature).toHashCode();
	}
	@Override
	public String getSignature() {
		return signature;
	}

	public int hashCode(){
		return hashCode;
	}
	
	public boolean equals(Object o){
		if(o instanceof Something){
			return hashCode() == o.hashCode();
		}
		return false;
	}
	
	@Override
	public String toString() {
		return getSignature();
	}
}
