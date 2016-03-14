package harmony.lod.factory;

import harmony.lod.model.api.dataset.OutputDataset;
import harmony.lod.model.api.dataset.SourceDataset;
import harmony.lod.model.api.dataset.TempDataset;

public interface DatasetFactory {

	public TempDataset getTempDataset();

	public SourceDataset getSourceDataset(String endpoint);
	
	public OutputDataset getOutputDataset();
}
