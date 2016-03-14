package harmony.planner.bestfirst;

import harmony.core.api.fact.Fact;
import harmony.core.api.fact.FactRegistry;
import harmony.core.api.operator.Action;
import harmony.core.api.operator.GroundAction;
import harmony.core.api.operator.Operator;
import harmony.core.api.operator.OperatorRegistry;
import harmony.core.api.operator.ParametersRegistry;
import harmony.core.api.thing.Thing;
import harmony.core.api.thing.ThingRegistry;
import harmony.core.impl.assessment.Evaluator;
import harmony.core.impl.fact.FactRegistryImpl;
import harmony.core.impl.operator.OperatorRegistryImpl;
import harmony.core.impl.parameters.ParametersRegistryImpl;
import harmony.core.impl.thing.ThingRegistryImpl;
import harmony.planner.ActionsProvider;
import harmony.planner.PlannerInput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * "Note: this class has a natural ordering that is inconsistent with equals."
 * 
 * 
 * @author Enrico Daga
 * 
 */
public final class NodeImpl implements Node {
	private FactRegistry state = null;
	private ThingRegistry things = null;
	private ParametersRegistry parameters = null;
	private OperatorRegistry operators = null;
	private PlannerInput input = null;
	private Evaluator evaluator = new Evaluator(this);
	private GroundAction action = null;
	private Node parent = null;
	private ActionsProvider actionsProvider = null;

	private int far = 0;

	public boolean isRoot() {
		return parent == null;
	}

	public void setParent(Node parent) {
		this.parent = parent;
		this.far = this.parent.getDepth() + 1;
		for (Node n : getSuccessors()) {
			if (!n.isRoot()) {
				if (!n.getParent().equals(this)) {
					n.setParent(this);
				}
			}
		}
	}

	public int getDepth() {
		return this.far;
	}

	public NodeImpl(PlannerInput input, ActionsProvider provider) {
		this.input = input;
		this.actionsProvider = provider;

		operators = new OperatorRegistryImpl();
		for (Operator o : input.getOperators()) {
			operators.register(o);
		}

		things = new ThingRegistryImpl();

		state = new FactRegistryImpl();
		parameters = new ParametersRegistryImpl(things);

		setup(input.getInitialState().getFacts(), input.getObjects());
	}

	@Override
	public PlannerInput getPlannerInput() {
		return input;
	}

	protected NodeImpl(Node parent, Action action) {

		this.far = parent.getDepth() + 1;
		// Generate ground action from parent state
		this.action = action.asGroundAction(parent);
		this.parent = parent;
		this.input = parent.getPlannerInput();
		this.actionsProvider = parent.getActionsProvider();
		this.state = new FactRegistryImpl();
		this.things = new ThingRegistryImpl();
		this.parameters = new ParametersRegistryImpl(things);
		this.operators = parent.getOperatorRegistry();

		// Build current state with ground effect
		List<Fact> facts = new ArrayList<Fact>();
		facts.addAll(parent.getFacts());
		facts.addAll(Arrays.asList(this.action.add()));
		facts.removeAll(Arrays.asList(this.action.remove()));

		Set<Thing> m = new HashSet<Thing>();
		m.addAll(parent.getThingRegistry().asSet());
		m.addAll(Arrays.asList(this.action.create()));
		m.removeAll(Arrays.asList(this.action.destroy()));
		setup(facts, m.toArray(new Thing[m.size()]));
	}

	private int hashCode = 0;

	private void setup(List<Fact> facts, Thing[] md) {

		HashCodeBuilder hb = new HashCodeBuilder();
		for (Thing mm : md) {
			hb.append(mm);
			things.put(mm);
		}
		for (Fact f : facts) {
			hb.append(f);
			state.put(f);
			// FIXME Do we need this here?
			// Where do we guarantee consistency between the two?
			// Should we simply check and throw and exception if necessary?
			for (Thing m : f.getThings()) {
				things.put(m);
			}
		}
		hashCode = hb.toHashCode();

	}

	public int hashCode() {
		return hashCode;
	}

	public boolean equals(Object o) {
		if (o instanceof Node) {
			return o.hashCode() == hashCode();
		}
		return false;
	}

	public Node getParent() {
		return parent;
	}

	public List<Fact> getFacts() {
		return state.asList();
	}

	public GroundAction getAction() {
		return action;
	}

	public FactRegistry getFactRegistry() {
		return state;
	}

	@Override
	public ThingRegistry getThingRegistry() {
		return things;
	}

	@Override
	public boolean isGoal() {
		return input.getGoal().asCondition().accept(this.evaluator);
	}

	@Override
	public ParametersRegistry getParametersRegistry() {
		return parameters;
	}

	@Override
	public OperatorRegistry getOperatorRegistry() {
		return operators;
	}

	private Set<Node> successors = null;

	@Override
	public Set<Node> getSuccessors() {
		if (successors == null) {
			successors = buildSuccessors();
		}
		return successors;
	}

	protected Set<Node> buildSuccessors() {
		// Set<Node> succ = new HashSet<Node>();
		// for (Operator o : operators.asCollection()) {
		// succ.addAll(buildSuccessors(o));
		// }
		// return succ;
		Set<Node> successors = new HashSet<Node>();
		Set<Action> nextActions = actionsProvider.buildActions(
				operators.asCollection(), (Node) this);
		for (Action a : nextActions) {
			Node n = new NodeImpl(this, a);
			successors.add(n);
		}
		return successors;

	}

	// protected Set<Node> buildSuccessors(Operator o) {
	//
	// Set<Node> successors = new HashSet<Node>();
	// Set<Action> nextActions = actionsProvider.buildActions(o, this);
	// for(Action a : nextActions){
	// Node n = new NodeImpl(this, a);
	// successors.add(n);
	// }
	// return successors;
	//
	// // // If the operator does not need any input
	// // if(o.getParametersTypes().size() == 0){
	// // try {
	// // Action act = o.build(new Thing[0]);
	// // Condition precog = act.precondition();
	// // Evaluator eval = new Evaluator(this);
	// // if (precog.accept(eval)) {
	// // Node newNode = new NodeImpl(this, act);
	// // successors.add(newNode);
	// // }
	// // act = o.build();
	// // } catch (OperatorException e) {
	// // e.printStackTrace();
	// // }
	// // }else{
	// // Iterator<List<Thing>> iter = this.getParametersRegistry().iterator(o);
	// // while (iter.hasNext()) {
	// // try {
	// // List<Thing> parameters = iter.next();
	// // Action act = o.build(parameters.toArray(new Thing[parameters
	// // .size()]));
	// // Condition precog = act.precondition();
	// // Evaluator eval = new Evaluator(this);
	// // if (precog.accept(eval)) {
	// // Node newNode = new NodeImpl(this, act);
	// // successors.add(newNode);
	// // }
	// // } catch (OperatorException e) {
	// // e.printStackTrace();
	// // }
	// // }
	// // }
	// //
	// // return successors;
	// }

	@Override
	public ActionsProvider getActionsProvider() {
		return actionsProvider;
	}

}