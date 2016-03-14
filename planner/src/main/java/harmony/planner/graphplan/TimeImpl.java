package harmony.planner.graphplan;

import harmony.core.api.fact.Fact;
import harmony.core.api.operator.Action;
import harmony.core.api.operator.GroundAction;
import harmony.core.api.state.State;
import harmony.core.api.thing.Thing;
import harmony.core.impl.assessment.FactsCollector;
import harmony.core.impl.state.StaticState;
import harmony.planner.ActionsProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class TimeImpl implements Time {
	private ActionLevel al;
	private PropositionLevel pl;
	private PlanningGraph pg;
	private int step = 0;

	TimeImpl(PlanningGraph pg) {
		this.pg = pg;

		this.step = pg.size();
		if (this.step > 0) {
			Time r = this.pg.getLast();
			ActionLevel l = r.getActionLevel();
			this.pl = l.next();
		} else {
			this.pl = new PropositionLevelImpl(getPlanningGraph().getRoot());
		}
	}

	@Override
	public ActionLevel getActionLevel() {
		if (al == null) {
			this.al = this.pl.next();
		}
		return al;
	}

	@Override
	public PropositionLevel getPropositionLevel() {
		return pl;
	}

	@Override
	public PlanningGraph getPlanningGraph() {
		return pg;
	}

	@Override
	public int step() {
		return step;
	}

	@Override
	public Time previous() {
		return pg.getTime(step() - 1);
	}

	@Override
	public Time next() {
		return pg.getTime(step() + 1);
	}

	@Override
	public boolean hasPrevious() {
		return (pg.getTime(step() - 1) != null);
	}

	@Override
	public boolean hasNext() {
		return (pg.getTime(step() + 1) != null);
	}

	class ActionLevelImpl implements ActionLevel {
		private Set<Action> actions;
		private Set<Thing> things;
		private Set<Action> possibleActions;
		private Mutex<Action> mutex;
		private PropositionLevel next;
		private PropositionLevel prev;
		private Map<Action, GroundAction> actionGrounded;
		private Map<Action, FactsCollector> preconditions;
		private Map<Action, Set<Fact>> additions;
		private Map<Action, Set<Fact>> deletions;

		public ActionLevelImpl(PropositionLevel pl) {
			preconditions = new HashMap<Action, FactsCollector>();
			additions = new HashMap<Action, Set<Fact>>();
			deletions = new HashMap<Action, Set<Fact>>();
			things = new HashSet<Thing>();

			things.addAll(pl.getthings());
			prev = pl;

			// All possible actions
			State st = prev.asState();
			ActionsProvider actionsProvider = new GraphplanActionsProvider();
			actions = actionsProvider.buildActions(TimeImpl.this
					.getPlanningGraph().getOperators(), st);

			// Build noop
			for (Fact f : st.getFacts()) {
				actions.add(new NoOpAction(f));
			}

			actionGrounded = new HashMap<Action, GroundAction>();

			// Build ground actions
			for (Action a : actions) {
				GroundAction ga = a.asGroundAction(st);
				actionGrounded.put(a, ga);
				additions.put(a, new HashSet<Fact>(Arrays.asList(ga.add())));
				deletions.put(a, new HashSet<Fact>(Arrays.asList(ga.remove())));
				FactsCollector colly = new FactsCollector(st);
				ga.precondition().accept(colly);
				// Set precondition analysis
				preconditions.put(a, colly);
			}

			// Init possible actions
			possibleActions = new HashSet<Action>();

			// Do not add a action when its preconditions are mutex
			for (Action a : actions) {
				FactsCollector fc = preconditions.get(a);
				if (ifMutex(fc.getShared()) || ifMutex(fc.getNegated())) {
					//
				}else{
					possibleActions.add(a);
				}
			}

			// Init action mutex
			mutex = new MutexImpl<Action>();

			// When two actions are mutually exclusive?
			for (Action a : possibleActions) {
//				if (!possibleActions.contains(a)) {
//					continue;
//				}

				for (Action a1 : possibleActions) {
					if (a1.equals(a)) {
						continue;
					}
//					if (!possibleActions.contains(a1)) {
//						continue;
//					}

					if (mutex(a, a1)) {
						mutex.put(a, a1);
					}

				}

			}

		}

		private boolean mutex(Action a0, Action a1) {
			return (interference(a0, a1) || competingNeeds(a0, a1));
		}

		private boolean competingNeeds(Action a0, Action a1) {
			FactsCollector a0facts = preconditions.get(a0);
			FactsCollector a1facts = preconditions.get(a1);

			
			// If a precondition of action A is marked as mutex of a
			// precondition of action B in pl
			Set<Fact> shared0Mutex = new HashSet<Fact>();
			Set<Fact> negated0Mutex = new HashSet<Fact>();
			for (Fact f : a0facts.getShared()) {
				shared0Mutex.addAll(prev.getMutex().getMutex(f));
			}
			for (Fact f : a0facts.getNegated()) {
				negated0Mutex.addAll(prev.getMutex().getMutex(f));
			}
			if (!Collections.disjoint(shared0Mutex, a1facts.getShared())) {
				return true;
			}
			if (!Collections.disjoint(negated0Mutex, a1facts.getNegated())) {
				return true;
			}
			Set<Fact> shared1Mutex = new HashSet<Fact>();
			Set<Fact> negated1Mutex = new HashSet<Fact>();
			for (Fact f : a1facts.getShared()) {
				shared1Mutex.addAll(prev.getMutex().getMutex(f));
			}
			for (Fact f : a1facts.getNegated()) {
				negated1Mutex.addAll(prev.getMutex().getMutex(f));
			}
			if (!Collections.disjoint(shared1Mutex, a0facts.getShared())) {
				return true;
			}
			if (!Collections.disjoint(negated1Mutex, a0facts.getNegated())) {
				return true;
			}

			// If a negated precondition is a precondition of another
			Set<Fact> a0negated = a0facts.getNegated();
			Set<Fact> a1shared = a1facts.getShared();
			if (!Collections.disjoint(a0negated, a1shared)) {
				return true;
			}
			Set<Fact> a1negated = a1facts.getNegated();
			Set<Fact> a0shared = a0facts.getShared();
			if (!Collections.disjoint(a1negated, a0shared)) {
				return true;
			}
			
			return false;
		}

		private boolean interference(Action a0, Action a1) {

			List<Fact> dels = Arrays.asList(actionGrounded.get(a0).remove());
			List<Fact> adds = Arrays.asList(actionGrounded.get(a0).add());
			boolean isMutex = false;
			for (Fact f : dels) {
				// 1.1 if an action deletes a precondition of another one
				if (preconditions.get(a1).getShared().contains(f)) {
					isMutex = true;
					break;
				}
				// 1.2 if an action deletes an addition of another one
				List<Fact> addl = new ArrayList<Fact>(
						Arrays.asList(actionGrounded.get(a1).add()));
				if (addl.removeAll(dels)) {
					isMutex = true;
					break;
				}
			}

			if (!isMutex) {
				for (Fact f : adds) {
					// 1.3 if an action adds a negated precondition of another
					// one
					if (preconditions.get(a1).getNegated().contains(f)) {
						isMutex = true;
						break;
					}
					// 1.4 if an action adds a deletion of another one
					List<Fact> reml = new ArrayList<Fact>(
							Arrays.asList(actionGrounded.get(a1).remove()));
					if (reml.removeAll(adds)) {
						isMutex = true;
						break;
					}
				}
			}
			return isMutex;
		}

		private boolean ifMutex(Set<Fact> facts) {
			for (Fact f : facts) {
				Mutex<Fact> mufac = prev.getMutex();
				Set<Fact> mutex = mufac.getMutex(f);
				if (!Collections.disjoint(facts, mutex)) {
					return true;
				}
			}
			return false;
		}

		@Override
		public Set<Thing> getthings() {
			return Collections.unmodifiableSet(things);
		}

		@Override
		public Set<Action> asSet() {
			return Collections.unmodifiableSet(possibleActions);
		}

		@Override
		public boolean contains(Action o) {
			return possibleActions.contains(o);
		}

		@Override
		public Mutex<Action> getMutex() {
			return mutex;
		}

		@Override
		public PropositionLevel next() {
			if (next == null) {
				next = new PropositionLevelImpl(this);
			}
			return next;
		}

		@Override
		public FactsCollector preconditions(Action action) {
			if (!contains(action)) {
				// FIXME null?
				return null;
			}
			return preconditions.get(action);
		}

		@Override
		public Set<Fact> additions(Action action) {
			if (!contains(action)) {
				return Collections.emptySet();
			}
			return Collections.unmodifiableSet(additions.get(action));
		}

		@Override
		public Set<Fact> deletions(Action action) {
			if (!contains(action)) {
				return Collections.emptySet();
			}
			return Collections.unmodifiableSet(deletions.get(action));
		}

		@Override
		public Set<Fact> additions() {
			Set<Fact> additionsSet = new HashSet<Fact>();
			for (Set<Fact> fs : additions.values()) {
				additionsSet.addAll(fs);
			}
			return Collections.unmodifiableSet(additionsSet);
		}

		@Override
		public Set<Fact> deletions() {
			Set<Fact> deletionsSet = new HashSet<Fact>();
			for (Set<Fact> fs : deletions.values()) {
				deletionsSet.addAll(fs);
			}
			return Collections.unmodifiableSet(deletionsSet);
		}
	}

	public class PropositionLevelImpl implements PropositionLevel {

		private ActionLevel prev;
		private Mutex<Fact> mutex;
		private Set<Fact> propositions;
		private Map<Fact, Set<Action>> addedBy;
		private Map<Fact, Set<Action>> removedBy;
		private ActionLevel next = null;
		private State asState;
		private Set<Thing> things;

		public PropositionLevelImpl(State state) {
			propositions = new HashSet<Fact>(state.getFacts());
			
			mutex = new MutexImpl<Fact>();
			addedBy = Collections.emptyMap();
			removedBy = Collections.emptyMap();
			things = new HashSet<Thing>();
			things.addAll(state.getThingRegistry().asSet());
			next = new ActionLevelImpl(this);
		}

		@Override
		public State asState() {
			if (asState == null) {
				asState = new StaticState(asSet(), getthings());
			}
			return asState;
		}

		public PropositionLevelImpl(ActionLevel al) {

			addedBy = new HashMap<Fact, Set<Action>>();
			removedBy = new HashMap<Fact, Set<Action>>();
			mutex = new MutexImpl<Fact>();
			prev = al;

			for (Action a : al.asSet()) {
				for (Fact added : al.additions(a)) {
					if (!addedBy.containsKey(added)) {
						addedBy.put(added, new HashSet<Action>());
					}
					addedBy.get(added).add(a);
				}

				for (Fact deleted : al.deletions(a)) {
					if (!removedBy.containsKey(deleted)) {
						removedBy.put(deleted, new HashSet<Action>());
					}
					removedBy.get(deleted).add(a);
				}
			}

			things = new HashSet<Thing>();
			things.addAll(al.getthings());

			propositions = new HashSet<Fact>();
			propositions.addAll(addedBy.keySet());

			// Compute mutex
			for (Fact f0 : propositions) {
				for (Fact f1 : propositions) {
					if (f0.equals(f1)) {
						continue;
					}
					if (mutex.hasMutex(f0) && mutex.getMutex(f0).contains(f1)) {
						continue;
					}
					if (mutex(f0, f1)) {
						mutex.put(f0, f1);
					}
				}
			}

			next = new ActionLevelImpl(this);
		}

		@Override
		public Set<Thing> getthings() {
			return Collections.unmodifiableSet(things);
		}

		private boolean mutex(Fact f0, Fact f1) {
			// "they are marked as exclusive if each action a having an
			// add-edge to proposition p is marked as exclusive of each
			// action b having an add-edge to proposition q."
			return mutex(addedBy(f0), addedBy(f1));
		}

		private boolean mutex(Action a0, Action a1) {
			return prev.getMutex().getMutex(a0).contains(a1);
		}

		private boolean mutex(Set<Action> a0, Set<Action> a1) {
			if(prev == null){
				return false;
			}
			
			boolean mx = true;
			for (Action a : a0) {
				if (!mutex(a, a1)) {
					return false;
				}
			}
			return mx;
		}

		private boolean mutex(Action a, Set<Action> set) {
			boolean mx = true;
			for (Action a1 : set) {
				if (!mutex(a, a1)) {
					return false;
				}
			}
			return mx;
		}

		@Override
		public Set<Fact> asSet() {
			return Collections.unmodifiableSet(propositions);
		}

		@Override
		public Set<Action> addedBy(Fact fact) {
			if (!addedBy.containsKey(fact)) {
				return Collections.emptySet();
			}
			return Collections.unmodifiableSet(addedBy.get(fact));
		}

		@Override
		public Set<Action> removedBy(Fact fact) {
			if (!removedBy.containsKey(fact)) {
				return Collections.emptySet();
			}
			return Collections.unmodifiableSet(removedBy.get(fact));
		}

		@Override
		public boolean contains(Fact o) {
			return propositions.contains(o);
		}

		@Override
		public Mutex<Fact> getMutex() {
			return mutex;
		}

		@Override
		public ActionLevel next() {
			return next;
		}

	}
}
