package harmony.lod.model.api.slice;

import java.util.List;

public interface Path extends Slice {

	public int getLength();
	
	/**
	 * 
	 * @return an unmodifiable list
	 */
	public List<StatementTemplate> asList();

	public boolean isClosed();
	
	public boolean isChain();
}
