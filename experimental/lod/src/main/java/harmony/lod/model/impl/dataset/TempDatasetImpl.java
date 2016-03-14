package harmony.lod.model.impl.dataset;

import harmony.lod.model.api.dataset.TempDataset;

public final class TempDatasetImpl extends AbstractDataset implements TempDataset {

	public TempDatasetImpl() {
		// XXX This should be enough to have non duplicates
		super(Long.toHexString(Double.doubleToLongBits(Math.random())));
	}

	@Override
	public String getSignature() {
		return new StringBuilder().append("(TempDataset ").append(getName())
				.append(")").toString();
	}

}
