package harmony.core.impl.condition;

import harmony.core.api.condition.Condition;
import harmony.core.api.condition.ConditionVisitor;
import harmony.core.api.fact.Fact;
import harmony.core.api.property.Property;
import harmony.core.api.thing.Thing;
import harmony.core.impl.fact.BasicFact;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class AssertFact implements Condition {

	private List<Fact> facts = new ArrayList<Fact>();

	public AssertFact() {

	}

	public AssertFact(Property p, Thing... things) {
		this(new BasicFact(p, things));
	}

	public AssertFact(Fact... facts) {
		this.facts.addAll(Arrays.asList(facts));
	}

	public AssertFact append(Fact fact) {
		this.facts.add(fact);
		return this;
	}

	public Iterator<Fact> toFacts() {
		return Collections.unmodifiableList(this.facts).iterator();
	}

	public int size() {
		return facts.size();
	}

	@Override
	public boolean accept(ConditionVisitor assessment) {
		return assessment.visit(this);
	}

	@Override
	public int compareTo(Condition o) {
		if (o instanceof Bool || o instanceof Type || o instanceof Equality) {
			return 1;
		} else if (o instanceof AssertFact) {
			if (((AssertFact) o).size() > size()) {
				return -1;
			} else if (((AssertFact) o).size() < size()) {
				return 1;
			}
			return o.hashCode() > hashCode() ? hashCode() : o.hashCode();
		}
		return -1;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(assert");
		for(Fact f : facts){
			sb.append(" ");
			sb.append(f);
		}
		sb.append(")");
		return sb.toString();
	}
}
