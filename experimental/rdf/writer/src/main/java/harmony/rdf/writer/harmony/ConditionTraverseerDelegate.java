package harmony.rdf.writer.harmony;

import harmony.core.api.condition.Condition;
import harmony.core.api.condition.ConditionVisitor;
import harmony.core.api.fact.Fact;
import harmony.core.impl.condition.And;
import harmony.core.impl.condition.AssertFact;
import harmony.core.impl.condition.Bool;
import harmony.core.impl.condition.Equality;
import harmony.core.impl.condition.Exists;
import harmony.core.impl.condition.Forall;
import harmony.core.impl.condition.Not;
import harmony.core.impl.condition.Or;
import harmony.core.impl.condition.Type;
import harmony.core.impl.condition.When;
import harmony.rdf.writer.RDFWriterTraverseerDelegate;
import harmony.rdf.writer.TripleFactory;
import harmony.rdf.writer.mapping.Mapper;

import java.util.Iterator;

public class ConditionTraverseerDelegate extends RDFWriterTraverseerDelegate {

	private ConditionTraverseerVisitor vis;
	
	public ConditionTraverseerDelegate(Mapper provider, TripleFactory<?> factory) {
		super(provider, factory);
		vis = new ConditionTraverseerVisitor();
	}

	@Override
	public void traverse(Object object) {
		if (object instanceof Condition) {
			Condition condition = (Condition) object;
			isA(condition, HarmonyDomain.Condition);
			condition.accept(vis);
		}
	}

	class ConditionTraverseerVisitor implements ConditionVisitor {
		@Override
		public boolean visit(And and) {
			isA(and, HarmonyDomain.And);
			for(Condition c : and.asList()){
				spo(and, HarmonyDomain.hasCondition, c);
			}
			return false;
		}

		@Override
		public boolean visit(Not cond) {
			isA(cond, HarmonyDomain.Not);
			spo(cond, HarmonyDomain.hasCondition, cond.getCondition());
			return false;
		}

		@Override
		public boolean visit(AssertFact cond) {
			isA(cond, HarmonyDomain.AssertFact);
			Iterator<Fact> i = cond.toFacts();
			while(i.hasNext()){
				spo(cond, HarmonyDomain.hasFact, i.next());
			}
			return false;
		}

		@Override
		public boolean visit(Equality cond) {
			isA(cond, HarmonyDomain.Equality);
			spo(cond, HarmonyDomain.hasObject, cond.getLHS());
			spo(cond, HarmonyDomain.hasObject, cond.getRHS());
			return false;
		}

		@Override
		public boolean visit(Bool bool) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean visit(Or or) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean visit(When when) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean visit(Exists exists) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean visit(Forall forall) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean visit(Type type) {
			// TODO Auto-generated method stub
			return false;
		}
	}
}
