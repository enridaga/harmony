package harmony.rdf.writer.harmony;

import harmony.core.api.problem.Problem;
import harmony.core.api.thing.Thing;
import harmony.rdf.writer.TripleFactory;
import harmony.rdf.writer.RDFWriterTraverseerDelegate;
import harmony.rdf.writer.mapping.Mapper;

public class ProblemTraverseerDelegate extends RDFWriterTraverseerDelegate {


	public ProblemTraverseerDelegate(Mapper provider,
			TripleFactory<?> factory) {
		super(provider, factory);
	}

	@Override
	public void traverse(Object o) {
		if (o instanceof Problem) {
			Problem p = (Problem) o;
			isA(p, HarmonyDomain.Problem);
			spo(p, HarmonyDomain.hasInitialState, p.getInitialState());
			spo(p, HarmonyDomain.hasGoal, p.getGoal());

			for (Thing m : p.getObjects()) {
				spo(p, HarmonyDomain.hasObject, m);
			}
		}
	}

}
