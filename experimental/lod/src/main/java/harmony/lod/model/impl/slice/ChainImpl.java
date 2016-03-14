package harmony.lod.model.impl.slice;

import harmony.lod.model.api.slice.Chain;
import harmony.lod.model.api.slice.StatementTemplate;

public class ChainImpl extends PathImpl implements Chain {
	
	public ChainImpl(StatementTemplate...statementTemplates) throws PathException{
		super(statementTemplates);
	}
	
	@Override
	public void addStep(StatementTemplate t) throws PathException {
		// it must be a statement or a completely defined statement template
		if (t.isStatement()) {
			super.addStep(t);
		} else {
			throw new PathException("Invalid statement template for Chain: "
					+ t);
		}
	}
}
