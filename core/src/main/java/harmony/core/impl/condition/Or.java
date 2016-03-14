package harmony.core.impl.condition;

import harmony.core.api.condition.Condition;
import harmony.core.api.condition.ConditionVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public final class Or implements Condition {

	private SortedSet<Condition> orList = new TreeSet<Condition>();

	public Or append(Condition cond) {
		orList.add(cond);
		return this;
	}

	@Override
	public boolean accept(ConditionVisitor assessment) {
		return assessment.visit(this);
	}

	public Iterator<Condition> iterator() {
		return Collections.unmodifiableSet(orList).iterator();
	}
	
	public List<Condition> asList(){
		return Collections.unmodifiableList(new ArrayList<Condition>(orList));
	}
	
	public int size(){
		return orList.size();
	}
	
	@Override
	public int compareTo(Condition o) {
		if(o instanceof Or){
			if(((Or) o).size() > size()){
				return -1;
			}else if(((Or) o).size() < size()){
				return 1;
			}
			return o.hashCode() > hashCode() ? hashCode() : o.hashCode();
		}
		return 1;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("(or ");
		for(Condition c : orList){
			sb.append(" ");
			sb.append(c.toString());
		}
		sb.append(")");
		return sb.toString();
	}
}
