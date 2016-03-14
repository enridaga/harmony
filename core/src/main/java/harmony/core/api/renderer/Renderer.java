package harmony.core.api.renderer;

import harmony.core.api.condition.Condition;
import harmony.core.api.domain.Domain;
import harmony.core.api.effect.Effect;
import harmony.core.api.effect.GroundEffect;
import harmony.core.api.fact.Fact;
import harmony.core.api.goal.Goal;
import harmony.core.api.operator.Action;
import harmony.core.api.operator.Operator;
import harmony.core.api.problem.Problem;
import harmony.core.api.property.Property;
import harmony.core.api.state.State;
import harmony.core.api.thing.Thing;

public interface Renderer {

	public Renderer append(GroundEffect groundEffect);

	public Renderer append(Fact fact);

	public Renderer append(Goal goal);

	public Renderer append(Operator operator);

	public Renderer append(Property property);

	public Renderer append(Effect effect);

	public Renderer append(Condition condition);
	
	public Renderer append(Domain domain);
	
	public Renderer append(Problem problem);

	public Renderer append(State state);

	public Renderer append(Thing thing);

	public Renderer append(Action a);
		
	public String toString();
	
	public StringBuffer toStringBuffer();
	
	public void clear();

	public Renderer append(String str);
}
