package harmony.lod.model.impl.symbol;

import harmony.lod.model.api.symbol.Datatype;
import harmony.lod.model.api.symbol.Lang;
import harmony.lod.model.api.symbol.Literal;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class LiteralImpl implements Literal {

	private String value = "";
	private Lang lang = null;
	private Datatype datatype = null;

	public LiteralImpl(String value){
		this.value = value;
	}
	
	public LiteralImpl(String value, Lang lang){
		this.value = value;
		this.lang = lang;
	}
	
	public LiteralImpl(String value, Datatype datatype){
		this.value = value;
		this.datatype = datatype;
	}
	
	public String getSignature() {
		StringBuilder b = new StringBuilder();
		if (value.indexOf('"') > -1) {
			b.append("\"\"\"");
			b.append(value);
			b.append("\"\"\"");
		} else {
			b.append('"');
			b.append(value);
			b.append('"');
		}
		if (lang != null) {
			b.append(lang.getSignature());
		} else if (datatype != null) {
			b.append(datatype.getSignature());
		}
		return b.toString();
	}

	public String getValue() {
		return value;
	}

	public Lang getLang() {
		return lang;
	}

	public Datatype getDatatype() {
		return datatype;
	}

	public boolean hasLang() {
		return lang != null;
	}

	public boolean hasDatatype() {
		return datatype != null;
	}
	
	public int hashCode(){
		return new HashCodeBuilder(17, 31).append(getSignature()).toHashCode();
	}
}
