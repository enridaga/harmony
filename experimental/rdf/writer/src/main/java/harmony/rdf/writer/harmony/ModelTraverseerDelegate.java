package harmony.rdf.writer.harmony;

import harmony.core.api.thing.Thing;
import harmony.rdf.writer.TripleFactory;
import harmony.rdf.writer.RDFWriterTraverseerDelegate;
import harmony.rdf.writer.mapping.Mapper;

public class ModelTraverseerDelegate extends RDFWriterTraverseerDelegate {

	public ModelTraverseerDelegate(Mapper provider,
			TripleFactory<?> factory) {
		super(provider, factory);
	}

	@Override
	public void traverse(Object o) {
		if (o instanceof Thing) {
			isA(o, HarmonyDomain.Model);
			lbl(o, ((Thing) o).getSignature(), "en");
			spv(o, HarmonyDomain.hasSignature, ((Thing) o).getSignature());
		}
	}

}
