package harmony.rdf.writer.lod;

import harmony.lod.model.api.dataset.Dataset;
import harmony.lod.model.api.dataset.OutputDataset;
import harmony.lod.model.api.dataset.SourceDataset;
import harmony.lod.model.api.dataset.TempDataset;
import harmony.lod.model.api.slice.Path;
import harmony.lod.model.api.slice.Slice;
import harmony.lod.model.api.symbol.Symbol;
import harmony.rdf.writer.harmony.HarmonyDomain;

public class LODDomain extends HarmonyDomain {
	public static final String LOD = "lod:";
	public static final String Dataset = LOD + "Dataset";
	public static final String TempDataset = LOD + "TempDataset";
	public static final String SourceDataset = LOD + "SourceDataset";
	public static final String OutputDataset = LOD + "OutputDataset";
	public static final String Slice = LOD + "Slice";
	public static final String Symbol = LOD + "Symbol";
	public static final String Path = LOD + "Path";
	public static final String IRI = LOD + "IRI";
	
	public LODDomain(String instancesNs) {
		super(instancesNs);
		map(Dataset.class, Dataset);
		map(TempDataset.class, TempDataset);
		map(SourceDataset.class, SourceDataset);
		map(OutputDataset.class, OutputDataset);
		map(Slice.class, Slice);
		map(Symbol.class, Symbol);
		map(Path.class, Path);
	}

}
