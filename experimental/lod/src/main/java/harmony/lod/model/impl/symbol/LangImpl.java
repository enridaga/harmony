package harmony.lod.model.impl.symbol;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import harmony.lod.model.api.symbol.Lang;

public class LangImpl implements Lang{

	private String lang = null;
	
	public LangImpl(String lang){
		this.lang = lang.substring(0, 2);
	}
	
	public String getSignature() {
		return new StringBuilder().append("@").append(lang).toString();
	}

	public String toString(){
		return lang;
	}
	
	public boolean equals(Object o){
		if(o instanceof Lang){
			return o.toString().equals(toString());
		}
		return false;
	}

	public int hashCode(){
		return new HashCodeBuilder(17, 31).append(getSignature()).toHashCode();
	}
}
