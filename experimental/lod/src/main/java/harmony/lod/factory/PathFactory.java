package harmony.lod.factory;

import harmony.lod.model.api.slice.Path;
import harmony.lod.model.api.slice.StatementTemplate;
import harmony.lod.model.impl.slice.PathException;

public interface PathFactory {

	public Path get(StatementTemplate... statementTemplate) throws PathException;
	
	public Path getP(String... predicates) throws PathException;
}
