package harmony.rdf.writer.harmony;

import harmony.core.api.goal.Goal;
import harmony.rdf.writer.TripleFactory;
import harmony.rdf.writer.RDFWriterTraverseerDelegate;
import harmony.rdf.writer.mapping.Mapper;

public class GoalTraverseerDelegate extends RDFWriterTraverseerDelegate {


	public GoalTraverseerDelegate(Mapper provider,
			TripleFactory<?> factory) {
		super(provider, factory);
	}

	@Override
	public void traverse(Object object) {
		if (object instanceof Goal) {
			Goal g = (Goal) object;
			isA(g, HarmonyDomain.Goal);
			spo(g, HarmonyDomain.hasCondition, g.asCondition());
		}
	}
}
