package harmony.planner.test;

import harmony.core.api.condition.Condition;
import harmony.core.api.effect.Effect;
import harmony.core.api.fact.Fact;
import harmony.core.api.goal.Goal;
import harmony.core.api.operator.Operator;
import harmony.core.api.operator.OperatorException;
import harmony.core.api.property.Property;
import harmony.core.api.thing.Thing;
import harmony.core.impl.condition.And;
import harmony.core.impl.condition.AssertFact;
import harmony.core.impl.condition.Equality;
import harmony.core.impl.condition.Not;
import harmony.core.impl.effect.BasicEffect;
import harmony.core.impl.fact.BasicFact;
import harmony.core.impl.goal.GoalImpl;
import harmony.core.impl.operator.AbstractOperator;
import harmony.core.impl.property.BasicProperty;
import harmony.core.impl.thing.Something;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class BlocksworldDomain extends PlannerInputTest {

	/**
	 * DOMAIN
	 */

	// Types
	public static final class Block extends Something {
		public Block(String id) {
			super(id);
		}
	}

	// Properties

	@SuppressWarnings("unchecked")
	public static final Property OnTable = new BasicProperty("OnTable",
			Block.class);
	@SuppressWarnings("unchecked")
	public static final Property ArmEmpty = new BasicProperty("ArmEmpty");
	@SuppressWarnings("unchecked")
	public static final Property Clear = new BasicProperty("Clear", Block.class);
	@SuppressWarnings("unchecked")
	public static final Property Holding = new BasicProperty("Holding",
			Block.class);
	@SuppressWarnings("unchecked")
	public static final Property On = new BasicProperty("On", Block.class,
			Block.class);

	public static Fact onTable(Thing b) {
		return new BasicFact(OnTable, (Block) b);
	}

	public static Fact armEmpty() {
		return new BasicFact(ArmEmpty);
	}

	public static Fact on(Thing b0, Thing b1) {
		return new BasicFact(On, (Block) b0, (Block) b1);
	}

	public static Fact clear(Thing b0) {
		return new BasicFact(Clear, (Block) b0);
	}

	public static Fact holding(Thing b0) {
		return new BasicFact(Holding, (Block) b0);
	}

	// Operators

	@SuppressWarnings("unchecked")
	public final static Operator PickUp = new AbstractOperator("PickUp",
			Block.class) {

		public Condition getPrecondition(final Thing... things)
				throws OperatorException {
			return new AssertFact(onTable(things[0]), clear(things[0]),
					armEmpty());
		}

		public Effect getEffect(final Thing... things) throws OperatorException {
			BasicEffect e = new BasicEffect();
			e.toAdd(holding(things[0]));
			e.toRemove(onTable(things[0]), armEmpty());
			return e;
		}

	};

	@SuppressWarnings("unchecked")
	public final static Operator PutDown = new AbstractOperator("PutDown",
			Block.class) {

		public Condition getPrecondition(final Thing... things)
				throws OperatorException {
			return new And()
					.append(new AssertFact().append(holding(things[0])))
					.append(new Not(new AssertFact().append(armEmpty())));
		}

		public Effect getEffect(final Thing... things) throws OperatorException {
			BasicEffect e = new BasicEffect();
			e.toAdd(clear(things[0]), armEmpty(), onTable(things[0]));
			e.toRemove(holding(things[0]));
			return e;
		}
	};

	@SuppressWarnings("unchecked")
	public final static Operator Stack = new AbstractOperator("Stack",
			Block.class, Block.class) {

		public Condition getPrecondition(final Thing... things)
				throws OperatorException {
			return new And()
					.append(new Not(new Equality(things[0], things[1])))
					.append(new AssertFact().append(clear(things[1])).append(
							holding(things[0])))
					.append(new Not(new AssertFact().append(armEmpty())));
		}

		public Effect getEffect(final Thing... things) throws OperatorException {
			BasicEffect e = new BasicEffect();
			e.toAdd(on(things[0], things[1]), clear(things[0]), armEmpty());
			e.toRemove(clear(things[1]), holding(things[0]));
			return e;
		}
	};

	@SuppressWarnings("unchecked")
	public final static Operator Unstack = new AbstractOperator("Unstack",
			Block.class, Block.class) {

		public Condition getPrecondition(final Thing... things)
				throws OperatorException {

			return new AssertFact(on(things[0], things[1]), armEmpty(),
					clear(things[0]));
		}

		public Effect getEffect(final Thing... things) throws OperatorException {
			BasicEffect e = new BasicEffect();
			e.toAdd(holding(things[0]), clear(things[1]));
			e.toRemove(on(things[0], things[1]), armEmpty());
			return e;
		}
	};

	public BlocksworldDomain(Fact[] init, Fact[] goalf) {
		super(init, goalf);
	}

	@Override
	public Operator[] getOperators() {
		return new Operator[] { PickUp, PutDown, Stack, Unstack };
	}

	@Override
	public Property[] getProperty() {
		return new Property[] { On, Clear, ArmEmpty, OnTable };
	}

	public static Fact[] stacksToFacts(String... stacks) {
		BlocksworldFactsBuilder bfb = new BlocksworldFactsBuilder();
		for (String stack : stacks) {
			bfb.append(stack);
		}
		return bfb.toFacts();
	}

	public static final class BlocksworldFactsBuilder {

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
						facts.add(onTable(new Block(blocks[x])));
					}
					if (x + 1 < blocks.length) {
						facts.add(on(new Block(blocks[x + 1]), new Block(
								blocks[x])));
					} else {
						facts.add(clear(new Block(blocks[x])));
					}
				}
			}
			facts.add(armEmpty());
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
						hierarchy.get(x).add(onTable(new Block(blocks[x])));
					}
					if (x + 1 < blocks.length) {
						if (!hierarchy.containsKey(x + 1)) {
							hierarchy.put(x + 1, new ArrayList<Fact>());
						}
						hierarchy.get(x + 1).add(
								on(new Block(blocks[x + 1]), new Block(
										blocks[x])));
					} else {
						hierarchy.get(x).add(clear(new Block(blocks[x])));
					}
				}
			}

			And goal = new And();
			And and = goal;
			int iterations = 0;
			int maxIter = hierarchy.entrySet().size() - 1;
			for (Entry<Integer, List<Fact>> e : hierarchy.entrySet()) {

				for (Fact f : e.getValue()) {
					and.append(new AssertFact(f));
				}
				if (iterations < maxIter) {
					And next = new And();
					and.append(next);
					and = next;
				}
				iterations++;
			}

			return new GoalImpl(goal);
		}
	}

}
