package harmony.planner;

import harmony.core.api.domain.Domain;
import harmony.core.api.goal.Goal;
import harmony.core.api.operator.Operator;
import harmony.core.api.problem.Problem;
import harmony.core.api.property.Property;
import harmony.core.api.thing.Thing;
import harmony.core.impl.state.InitialState;

public class PlannerInputBuilder {
	protected Domain domain = null;
	protected Problem problem = null;

	public PlannerInputBuilder(Domain domain, Problem problem) {
		this.domain = domain;
		this.problem = problem;
	}

	public PlannerInput build() {
		return new PlannerInput() {

			public InitialState getInitialState() {
				return problem.getInitialState();
			}

			public Goal getGoal() {
				return problem.getGoal();
			}

			public Property[] getProperty() {
				return domain.getProperty();
			}

			public Operator[] getOperators() {
				return domain.getOperators();
			}
			
			@Override
			public Thing[] getObjects() {
				return problem.getObjects();
			}
		};

	}
}
