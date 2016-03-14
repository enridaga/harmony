package harmony.rdf.writer.harmony;

import harmony.core.api.fact.Fact;
import harmony.core.api.thing.Thing;
import harmony.rdf.writer.TripleFactory;
import harmony.rdf.writer.RDFWriterTraverseerDelegate;
import harmony.rdf.writer.mapping.Mapper;

public class FactTraverseerDelegate extends RDFWriterTraverseerDelegate {

	public FactTraverseerDelegate(Mapper provider, TripleFactory<?> factory) {
		super(provider, factory);
	}

	@Override
	public void traverse(Object o) {
		if (o instanceof Fact) {
			Fact f = (Fact) o;

			isA(o, HarmonyDomain.Fact);
			lbl(o, "Fact (" + f.getProperty().getName() + ")", "en");
			spo(o, HarmonyDomain.hasProperty, f.getProperty());

			int index = 0;
			for (Thing m : f.getThings()) {
				String nodeId = bnode();
				_isA(nodeId, HarmonyDomain.Argument);
				_spo(id(f), HarmonyDomain.hasArgument, nodeId);
				_spo(nodeId,HarmonyDomain.hasObject, id(m));
				_spv(nodeId, HarmonyDomain.atIndex, index);
				index++;

			}
		}
	}
}
