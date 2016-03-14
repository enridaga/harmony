package harmony.rdf.writer.harmony;

import harmony.core.api.condition.Condition;
import harmony.core.api.domain.Domain;
import harmony.core.api.fact.Fact;
import harmony.core.api.goal.Goal;
import harmony.core.api.operator.Operator;
import harmony.core.api.problem.Problem;
import harmony.core.api.property.Property;
import harmony.core.api.thing.Thing;
import harmony.rdf.writer.mapping.AbstractMapper;

public class HarmonyDomain extends AbstractMapper {
	public static final String HARMONY = "http://www.enridaga.net/phd/harmony#";
	public static final String hasOperator = HARMONY + "hasOperator";
	public static final String hasProperty = HARMONY + "hasProperty";
	public static final String InitialState = HARMONY + "InitialState";
	public static final String hasInitialState = HARMONY + "hasInitialState";
	public static final String hasGoal = HARMONY + "hasGoal";
	public static final String hasParameter = HARMONY + "hasParameter";
	public static final String hasArgument = HARMONY + "hasArgument";
	public static final String atIndex = HARMONY + "atIndex";
	public static final String ofType = HARMONY + "ofType";
	public static final String about = HARMONY + "about";
	public static final String Domain = HARMONY + "Domain";
	public static final String Operator = HARMONY + "Operator";
	public static final String Parameter = HARMONY + "Parameter";
	public static final String Property = HARMONY + "Property";
	public static final String Model = HARMONY + "Thing";
	public static final String Fact = HARMONY + "Fact";
	public static final String hasFact = HARMONY + "hasFact";
	public static final String State = HARMONY + "State";
	public static final String Goal = HARMONY + "Goal";
	public static final String Problem = HARMONY + "Problem";
	public static final String Condition = HARMONY + "Condition";
	public static final String hasCondition = HARMONY + "hasCondition";
	public static final String hasPrecondition = HARMONY + "hasPrecondition";
	public static final String Argument = HARMONY + "Argument";
	public static final String hasObject = HARMONY + "hasObject";
	public static final String hasSignature = HARMONY + "hasSignature";
	public static final String And = HARMONY + "And";
	public static final String Not = HARMONY + "Not";
	public static final String Or = HARMONY + "Or";
	public static final String Equality = HARMONY + "Equality";
	public static final String AssertFact = HARMONY + "AssertFact";

	public HarmonyDomain(String contextNs) {
		super(contextNs);
		map(Domain.class, Domain);
		map(Problem.class, Problem);
		map(Operator.class, Operator);
		map(Fact.class, Fact);
		map(Thing.class, Model);
		map(Property.class, Property);
		map(Condition.class, Condition);

		attach(DomainTraverseerDelegate.class);
		attach(ProblemTraverseerDelegate.class);
		attach(InitialStateTraverseerDelegate.class);
		attach(GoalTraverseerDelegate.class);
		attach(PropertyTraverseerDelegate.class);
		attach(OperatorTraverseerDelegate.class);
		attach(FactTraverseerDelegate.class);
		attach(ModelTraverseerDelegate.class);
		attach(ConditionTraverseerDelegate.class);
	}

	@Override
	public final String id(Object o) {
		if (o instanceof Domain) {
			return getContextNS() + "domain/" + ((Domain) o).hashCode();
		} else if (o instanceof Problem) {
			return getContextNS() + "problem/" + ((Problem) o).hashCode();
		} else if (o instanceof Operator) {
			return getContextNS() + "operator/" + ((Operator) o).getName();
		} else if (o instanceof Property) {
			return getContextNS() + "property/" + ((Property) o).getName();
		} else if (o instanceof Fact) {
			return getContextNS() + "fact/" + ((Fact) o).getProperty().getName()
					+ "/" + o.hashCode();
		} else if (o instanceof Thing) {
			return getContextNS() + "model/" + Integer.toString(o.hashCode());
		} else if (o instanceof Condition) {
			return getContextNS() + "condition/" + Integer.toString(o.hashCode());
		} else if (o instanceof Goal) {
			return getContextNS() + "goal/" + Integer.toString(o.hashCode());
		} else if (o instanceof Class<?>) {
			// FIXME Not sure this is correct
			return getContextNS() + "type/" + ((Class<?>) o).getName();
		} else {
			return "java:" + o.getClass().getCanonicalName() + "@" + o.hashCode();
		}
	}

}
