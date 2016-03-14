package harmony.core.impl.renderer;

import harmony.core.api.condition.Condition;
import harmony.core.api.condition.ConditionVisitor;
import harmony.core.api.domain.Domain;
import harmony.core.api.effect.Effect;
import harmony.core.api.effect.GroundEffect;
import harmony.core.api.fact.Fact;
import harmony.core.api.goal.Goal;
import harmony.core.api.operator.Action;
import harmony.core.api.operator.Operator;
import harmony.core.api.problem.Problem;
import harmony.core.api.property.Property;
import harmony.core.api.renderer.Renderer;
import harmony.core.api.state.State;
import harmony.core.api.thing.Thing;
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

import java.util.Iterator;
import java.util.List;

public class RendererImpl implements Renderer {

	private final class ConditionRenderer implements ConditionVisitor {

		@Override
		public boolean visit(Not cond) {
			b.append("(Not");
			b.append(SPACE);
			boolean bu = cond.getCondition().accept(this);
			b.append(")");
			return bu;
		}

		@Override
		public boolean visit(AssertFact cond) {
			b.append("(Assert");
			b.append(SPACE);
			Iterator<Fact> itf = cond.toFacts();
			while (itf.hasNext()) {
				RendererImpl.this.append(itf.next());
			}
			b.append(")");
			return true;
		}

		@Override
		public boolean visit(Equality cond) {
			b.append("(Eq");
			b.append(SPACE);
			RendererImpl.this.append(cond.getLHS());
			b.append(SPACE);
			RendererImpl.this.append(cond.getRHS());
			b.append(")");
			return true;
		}

		@Override
		public boolean visit(And and) {
			b.append("(And");
			for (Condition c : and.asList()) {
				b.append(SPACE);
				c.accept(this);
			}
			b.append(")");
			return true;
		}

		@Override
		public boolean visit(Bool bool) {
			b.append("(Bool");
			b.append(SPACE);
			b.append(Boolean.toString(bool.isTrue()));
			b.append(")");
			return true;
		}

		@Override
		public boolean visit(Or or) {
			b.append("(Or");
			for (Condition c : or.asList()) {
				b.append(SPACE);
				c.accept(this);
			}
			b.append(")");
			return true;
		}

		@Override
		public boolean visit(When when) {
			
			b.append("(When");
			b.append(SPACE);
			when.when().accept(this);
			b.append(SPACE);
			when.then().accept(this);
			if (when.otherwise() != null) {
				b.append(SPACE);
				when.otherwise().accept(this);
			}
			b.append(")");
			return true;
		}

		@Override
		public boolean visit(Exists exists) {
			b.append("(Exists");
			b.append(SPACE);
			b.append(exists.getNumberOfParameters());
			b.append(" parameters");
			b.append(")");
			return true;
		}

		@Override
		public boolean visit(Forall forall) {
			b.append("(Forall");
			b.append(SPACE);
			b.append(forall.getNumberOfParameters());
			b.append(" parameters");
			b.append(")");
			return true;
		}

		@Override
		public boolean visit(Type type) {
			b.append("(Type");
			b.append(SPACE);
			RendererImpl.this.append(type.getThing());
			b.append(SPACE);
			RendererImpl.this.append(type.getType().getName());
			b.append(")");
			return true;
		}

	}

	private StringBuffer b = null;

	final static char OPEN = '(';
	final static char CLOSE = ')';
	final static String ACTION = "";
	final static String PROPERTY = "";
	final static char OPERATOR = '$';
	final static String FACT = "";
	final static char EFFECT = '#';
	final static String GROUND_EFFECT = "##";
	final static char CONDITION = '?';
	final static String MODEL = "";
	final static String DOMAIN = "Domain";
	final static String PROBLEM = "Problem";
	final static String INIT = "Init";
	final static String GOAL = "Goal";
	final static char SPACE = ' ';
	final static char ADD = '+';
	final static char REMOVE = '-';
	final static char STATE = 'S';
	final static String NEWLINE = System.getProperty("line.separator");

	public RendererImpl() {
		clear();
	}

	@Override
	public Renderer append(String str) {
		b.append(str);
		return this;
	}

	@Override
	public Renderer append(GroundEffect groundEffect) {
		b.append(OPEN);
		b.append(GROUND_EFFECT);
		for (Fact f : groundEffect.add()) {
			b.append(SPACE).append(ADD);
			append(f);
		}
		for (Fact f : groundEffect.remove()) {
			b.append(SPACE).append(REMOVE);
			append(f);
		}
		b.append(CLOSE);
		return this;
	}

	@Override
	public Renderer append(Fact fact) {
		b.append(OPEN);
		b.append(FACT);
		append(fact.getProperty());

		List<Thing> mm = fact.getThings();
		for (int x = 0; x < mm.size(); x++) {
			b.append(SPACE);
			append(mm.get(x));
		}
		b.append(CLOSE);
		return this;
	}

	public Renderer append(Thing thing) {
		b.append(MODEL).append(thing.getSignature());
		return this;
	}

	@Override
	public Renderer append(Operator operator) {
		b.append(OPEN);
		b.append(OPERATOR);
		b.append(SPACE);
		b.append(operator.getName());
		b.append(CLOSE);
		return this;
	}

	@Override
	public Renderer append(Effect effect) {
		b.append(OPEN).append(EFFECT).append(effect.toString()).append(CLOSE);
		return this;
	}

	@Override
	public Renderer append(Condition condition) {
		b.append(OPEN);
		b.append(CONDITION);
		ConditionRenderer cr = new ConditionRenderer();
		condition.accept(cr);
		b.append(CLOSE);
		return this;
	}

	@Override
	public Renderer append(Domain domain) {
		b.append(OPEN);
		b.append(DOMAIN);
		b.append(SPACE);
		for (Operator o : domain.getOperators())
			append(o);
		for (Property p : domain.getProperty())
			append(p);
		b.append(CLOSE);
		return this;
	}

	@Override
	public Renderer append(Problem problem) {
		b.append(OPEN);
		b.append(PROBLEM);
		b.append(SPACE);
		for (Thing o : problem.getObjects())
			append(o);
		b.append(INIT);
		for (Fact p : problem.getInitialState().getFacts())
			append(p);
		b.append(GOAL);
		append(problem.getGoal().asCondition());
		// for (Fact p : problem.getGoal())
		// append(p);
		b.append(CLOSE);
		return this;
	}

	@Override
	public Renderer append(Property property) {
		b.append(PROPERTY);
		b.append(property.getName());
		return this;
	}

	@Override
	public Renderer append(State state) {
		b.append(OPEN);
		b.append(STATE);
		for (Fact f : state.getFacts()) {
			b.append(SPACE);
			append(f);
		}
		return this;
	}

	@Override
	public Renderer append(Action a) {
		b.append(OPEN);
		b.append(ACTION);
		b.append(a.operator().getName());
		b.append("[");
		boolean first = true;
		for (Thing m : a.parameters()) {
			if (!first) {
				b.append(", ");
			}
			first = false;
			b.append(m.getSignature());
		}
		b.append("]");

		// b.append(SPACE);
		// if(a instanceof GroundAction){
		// b.append(NEWLINE);
		// for(Fact f : ((GroundAction)a).add()){
		// b.append(ADD);
		// append(f);
		// b.append(SPACE);
		// }
		// b.append(NEWLINE);
		// for(Fact f : ((GroundAction)a).remove()){
		// b.append(REMOVE);
		// append(f);
		// b.append(SPACE);
		// }
		// }else{
		// b.append(NEWLINE);
		// append(a.precondition());
		// b.append(SPACE);
		// append(a.effect());
		// }
		b.append(CLOSE);
		return this;
	}

	public StringBuffer toStringBuffer() {
		StringBuffer buffer = b;
		clear();
		return buffer;
	}

	public String toString() {
		String str = b.toString();
		clear();
		return str;
	}

	public void clear() {
		b = new StringBuffer();
	}
	
	@Override
	public Renderer append(Goal goal) {
		b.append(OPEN);
		b.append("Goal");
		b.append(SPACE);
		append(goal.asCondition());
		b.append(CLOSE);
		return this;
	}
}
