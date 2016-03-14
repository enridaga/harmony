package harmony.blocksworld.utils;

import harmony.blocksworld.Block;
import harmony.blocksworld.property.ArmEmpty;
import harmony.blocksworld.property.Clear;
import harmony.blocksworld.property.On;
import harmony.blocksworld.property.OnTable;
import harmony.core.api.fact.Fact;
import harmony.core.api.goal.Goal;
import harmony.core.impl.condition.And;
import harmony.core.impl.condition.AssertFact;
import harmony.core.impl.fact.BasicFact;
import harmony.core.impl.goal.GoalImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class BlocksworldFactsBuilder {

	private List<String> stacks = null;

	public BlocksworldFactsBuilder() {
		stacks = new ArrayList<String>();
	}

	public void append(String stack) {
		stacks.add(stack);
	}

	public Fact[] toFacts() {
		List<Fact> facts = new ArrayList<Fact>();

		for (String s : stacks) {
			String[] blocks = s.split(",");
			for (int x = 0; x < blocks.length; x++) {
				if (x == 0) {
					facts.add(new BasicFact(new OnTable(), new Block(blocks[x])));
				}
				if (x + 1 < blocks.length) {
					facts.add(new BasicFact(new On(), new Block(blocks[x + 1]),
							new Block(blocks[x])));
				} else {
					facts.add(new BasicFact(new Clear(), new Block(blocks[x])));
				}
			}
		}
		facts.add(new BasicFact(new ArmEmpty()));
		return facts.toArray(new Fact[facts.size()]);
	}

	public Goal toGoal() {
		Map<Integer, List<Fact>> hierarchy = new HashMap<Integer, List<Fact>>();
		for (String s : stacks) {
			String[] blocks = s.split(",");
			for (int x = 0; x < blocks.length; x++) {
				
				if (x == 0) {
					if (!hierarchy.containsKey(x)) {
						hierarchy.put(x, new ArrayList<Fact>());
					}
					hierarchy.get(x).add(
							new BasicFact(new OnTable(), new Block(blocks[x])));
				}
				if (x + 1 < blocks.length) {
					if (!hierarchy.containsKey(x+1)) {
						hierarchy.put(x+1, new ArrayList<Fact>());
					}
					hierarchy.get(x + 1).add(
							new BasicFact(new On(), new Block(blocks[x + 1]),
									new Block(blocks[x])));
				} else {
					hierarchy.get(x).add(
							new BasicFact(new Clear(), new Block(blocks[x])));
				}
			}
		}

		And goal = new And();
		And and = goal;
		int iterations = 0;
		int maxIter = hierarchy.entrySet().size() - 1;
		for(Entry<Integer,List<Fact>> e : hierarchy.entrySet()){
			
			for(Fact f : e.getValue()){
				and.append(new AssertFact(f));
			}
			if(iterations < maxIter){
				And next = new And();
				and.append(next);
				and = next;
			}
			iterations++;
		}
		
		return new GoalImpl(goal);
	}
}
