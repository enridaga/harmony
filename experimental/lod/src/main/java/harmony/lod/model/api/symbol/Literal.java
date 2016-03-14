package harmony.lod.model.api.symbol;

public interface Literal extends Resource {

	public String getValue();
	
	public Lang getLang();
	
	public Datatype getDatatype();
	
	public boolean hasLang();
	
	public boolean hasDatatype();
}
