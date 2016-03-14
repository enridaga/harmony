package harmony.blocksworld.problems;

import harmony.blocksworld.Blocksworld;
import harmony.blocksworld.BlocksworldProblem;
import harmony.core.api.domain.Domain;
import harmony.core.api.operator.Operator;
import harmony.core.api.property.Property;
import harmony.planner.PlannerInput;

public class Problem1 extends BlocksworldProblem implements PlannerInput{

	private Domain d = new Blocksworld();
	public Problem1() {
//		setInitialState("J,K,L,M", "E,F,G,H,I", "A,B,C,D");
//		setGoal("J,E,A","K,L,M", "F,G,H,I", "B,C,D");
		//
		//
		setInitialState("J,K,L,M", "E,F", "A,B");
		setGoal("J,E,A","M,L,K","F,B");
		
	}
	
	@Override
	public Operator[] getOperators() {
		return d.getOperators();
	}
	
	@Override
	public Property[] getProperty() {
		return d.getProperty();
	}
}
