package harmony.lod.factory;

import harmony.lod.model.api.dataset.OutputDataset;
import harmony.lod.model.api.dataset.SourceDataset;
import harmony.lod.model.api.dataset.TempDataset;
import harmony.lod.model.impl.dataset.OutputDatasetImpl;
import harmony.lod.model.impl.dataset.SourceDatasetImpl;
import harmony.lod.model.impl.dataset.TempDatasetImpl;

final class DatasetFactoryImpl implements DatasetFactory {

	@Override
	public TempDataset getTempDataset() {
		return new TempDatasetImpl();
	}

	@Override
	public SourceDataset getSourceDataset(String endpoint) {
		return new SourceDatasetImpl(endpoint);
	}

	@Override
	public OutputDataset getOutputDataset() {
		return new OutputDatasetImpl("output");
	}

}
