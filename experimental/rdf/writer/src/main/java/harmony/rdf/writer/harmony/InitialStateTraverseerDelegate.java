package harmony.rdf.writer.harmony;

import harmony.core.api.fact.Fact;
import harmony.core.impl.state.InitialState;
import harmony.rdf.writer.TripleFactory;
import harmony.rdf.writer.RDFWriterTraverseerDelegate;
import harmony.rdf.writer.mapping.Mapper;

public class InitialStateTraverseerDelegate extends RDFWriterTraverseerDelegate {


	public InitialStateTraverseerDelegate(Mapper provider,
			TripleFactory<?> factory) {
		super(provider, factory);
	}

	@Override
	public void traverse(Object object) {
		if (object instanceof InitialState) {
			InitialState iState = (InitialState) object;
			isA(iState, HarmonyDomain.InitialState);
			for (Fact f : iState.getFacts()) {
				spo(iState, HarmonyDomain.hasFact, f);
			}
		}
	}
}
