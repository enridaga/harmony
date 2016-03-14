package harmony.lod.model.api.slice;

import harmony.core.api.thing.Thing;
import harmony.lod.model.api.symbol.Symbol;

import java.util.Set;

/**
 * A Slice is a RDF graph template.
 * 
 * @author Enrico Daga
 *
 */
public interface Slice extends Thing{
	
	public Set<Symbol> getSymbolsInSignature();
	
	/**
	 * Whether the given <tt>st</tt> represents a portion of this one
	 * 
	 * @param st
	 * @return
	 */
	public boolean includes(Slice slice);
	
	public String getSignature();
}
