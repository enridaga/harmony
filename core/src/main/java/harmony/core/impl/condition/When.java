package harmony.core.impl.condition;

import harmony.core.api.condition.Condition;
import harmony.core.api.condition.ConditionVisitor;

public class When implements Condition {

	private Condition when;
	private Condition then;
	private Condition otherwise;

	public When(Condition condition, Condition then) {
		this.when = condition;
		this.then = then;
		this.otherwise = null;
	}

	public When(Condition condition, Condition then, Condition otherwise) {
		this.when = condition;
		this.then = then;
		this.otherwise = otherwise;
	}

	@Override
	public int compareTo(Condition o) {
		if (o instanceof And) {
			if (((And) o).size() > 2) {
				return -1;
			} else {
				return 1;
			}
		} else if (o instanceof When) {
			int thenC = then().compareTo(((When) o).then());
			int elsewhereC = then().compareTo(((When) o).otherwise());
			if (thenC == elsewhereC) {
				return thenC;
			}
		}
		return o.hashCode() > hashCode() ? hashCode() : o.hashCode();
	}

	@Override
	public boolean accept(ConditionVisitor assessment) {
		return assessment.visit(this);
	}

	public Condition when() {
		return when;
	}

	public Condition then() {
		return then;
	}

	public Condition otherwise() {
		return otherwise;
	}
	

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("(when ");
		sb.append(when.toString());
		sb.append(" ");
		sb.append(then.toString());
		if(otherwise!=null){
			sb.append(" ");
			sb.append(then.toString());
		}
		sb.append(")");
		return sb.toString();
	}
}
