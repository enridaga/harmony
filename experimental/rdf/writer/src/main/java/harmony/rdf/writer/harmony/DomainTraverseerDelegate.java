package harmony.rdf.writer.harmony;

import harmony.core.api.domain.Domain;
import harmony.core.api.operator.Operator;
import harmony.rdf.writer.TripleFactory;
import harmony.rdf.writer.RDFWriterTraverseerDelegate;
import harmony.rdf.writer.mapping.Mapper;

public class DomainTraverseerDelegate extends RDFWriterTraverseerDelegate {
	

	public DomainTraverseerDelegate(Mapper provider,
			TripleFactory<?> factory) {
		
		super(provider, factory);
	}

	@Override
	public void traverse(Object o) {
		if (o instanceof Domain) {

			isA(o, HarmonyDomain.Domain);
			for (Operator op : ((Domain) o).getOperators()) {
				spo(o, HarmonyDomain.hasOperator, op);
			}

			for (harmony.core.api.property.Property p : ((Domain) o)
					.getProperty()) {
				spo(o, HarmonyDomain.hasProperty, p);
			}

		}
	}
}
