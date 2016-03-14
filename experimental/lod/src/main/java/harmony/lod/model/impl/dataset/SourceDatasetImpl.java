package harmony.lod.model.impl.dataset;

import harmony.lod.model.api.dataset.SourceDataset;

public final class SourceDatasetImpl extends AbstractDataset implements SourceDataset{

	public SourceDatasetImpl(String name) {
		super(name);
	}
	
	@Override
	public String getSignature() {
		return new StringBuilder().append("(SourceDataset ").append(getName())
				.append(")").toString();
	}

	@Override
	public boolean isReadOnly() {
		return true;
	}
}
