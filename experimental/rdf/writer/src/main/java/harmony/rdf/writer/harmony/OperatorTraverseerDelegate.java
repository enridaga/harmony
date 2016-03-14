package harmony.rdf.writer.harmony;

import harmony.core.api.operator.Operator;
import harmony.core.api.thing.Thing;
import harmony.rdf.writer.TripleFactory;
import harmony.rdf.writer.RDFWriterTraverseerDelegate;
import harmony.rdf.writer.mapping.Mapper;

public class OperatorTraverseerDelegate extends RDFWriterTraverseerDelegate {

	public OperatorTraverseerDelegate(Mapper provider,
			TripleFactory<?> factory) {
		super(provider, factory);
	}

	@Override
	public void traverse(Object o) {
		if (o instanceof Operator) {
			Operator op = (Operator) o;

			isA(op, HarmonyDomain.Operator);
			lbl(op, op.getName(), "en");

			int index = 0;

			for (Class<? extends Thing> m : ((Operator) o).getParametersTypes()) {
				String nodeId = bnode();
				_spo(id(op), HarmonyDomain.hasParameter, nodeId);
				_isA(nodeId, HarmonyDomain.Parameter);
				_spv(nodeId, HarmonyDomain.atIndex, index);
				_spo(nodeId, HarmonyDomain.ofType, getIdentityProvider().type(m));
				index++;
			}
			// TODO
			//spo(op, HarmonyDomain.hasPrecondition, op.getPrecondition(things));
		}
	}
}
