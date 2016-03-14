package harmony.rdf.writer.harmony;

import harmony.core.api.property.Property;
import harmony.core.api.thing.Thing;
import harmony.rdf.writer.TripleFactory;
import harmony.rdf.writer.RDFWriterTraverseerDelegate;
import harmony.rdf.writer.mapping.Mapper;

public class PropertyTraverseerDelegate extends RDFWriterTraverseerDelegate {


	public PropertyTraverseerDelegate(Mapper provider,
			TripleFactory<?> factory) {
		super(provider, factory);
	}

	@Override
	public void traverse(Object o) {
		if (o instanceof Property) {
			Property p = (Property) o;
			isA(p, HarmonyDomain.Property);
			lbl(p, p.getName(), "en");
			int index = 0;
			for (Class<? extends Thing> m : ((Property) o).getArgTypes()) {
				String nodeId = bnode();
				_spo(id(p), HarmonyDomain.hasParameter, nodeId);
				_isA(nodeId, HarmonyDomain.Parameter);
				_spv(nodeId, HarmonyDomain.atIndex, index);
				_spo(nodeId, HarmonyDomain.ofType, getIdentityProvider().type(m));
				index++;
			}

		}
	}
}
