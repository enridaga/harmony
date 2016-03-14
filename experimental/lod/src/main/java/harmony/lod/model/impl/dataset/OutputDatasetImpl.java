package harmony.lod.model.impl.dataset;

import harmony.lod.model.api.dataset.OutputDataset;

public final class OutputDatasetImpl extends AbstractDataset implements OutputDataset {

	public OutputDatasetImpl(String name) {
		super(name);
	}

	@Override
	public String getSignature() {
		return new StringBuilder().append("(OutputDataset ").append(getName())
				.append(")").toString();
	}
}
