package harmony.core.impl.condition;

import harmony.core.api.condition.Condition;
import harmony.core.api.condition.ConditionVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public final class And implements Condition {

	private SortedSet<Condition> andList = new TreeSet<Condition>();

	public And() {
	}

	public And append(Condition cond) {
		andList.add(cond);
		return this;
	}

	@Override
	public boolean accept(ConditionVisitor assessment) {
		return assessment.visit(this);
	}

	public Iterator<Condition> iterator() {
		return Collections.unmodifiableSet(andList).iterator();
	}
	
	public List<Condition> asList(){
		return Collections.unmodifiableList(new ArrayList<Condition>(andList));
	}
	
	public int size(){
		return andList.toArray().length;
	}
	
	@Override
	public int compareTo(Condition o) {
		if(o instanceof And){
			if(((And) o).size() > size()){
				return -1;
			}else if(((And) o).size() < size()){
				return 1;
			}
			return o.hashCode() > hashCode() ? hashCode() : o.hashCode();
		}else if(o instanceof When){
			if( 2 > size()){
				return -1;
			}else {
				return 1;
			}
		}
		return 1;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("(and ");
		for(Condition c : andList){
			sb.append(" ");
			sb.append(c.toString());
		}
		sb.append(")");
		return sb.toString();
	}
}
