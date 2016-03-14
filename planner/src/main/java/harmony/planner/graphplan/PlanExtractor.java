package harmony.planner.graphplan;

import harmony.core.api.fact.Fact;
import harmony.core.api.goal.Goal;
import harmony.core.api.operator.Action;
import harmony.core.api.operator.GroundAction;
import harmony.core.api.plan.Plan;
import harmony.core.impl.assessment.Evaluator;
import harmony.core.impl.assessment.FactsCollector;
import harmony.core.impl.goal.GoalImpl;
import harmony.core.impl.state.StaticState;
import harmony.planner.NoSolutionException;
import harmony.planner.PlannerInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlanExtractor {
	private PlanningGraph pg;
	private PlannerInput input;
	private Map<Integer, Set<Set<Fact>>> memorisedBadGoalSets;

	protected PlanExtractor(PlanningGraph pg, PlannerInput input) {
		this.pg = pg;
		this.input = input;
		this.memorisedBadGoalSets = new HashMap<Integer, Set<Set<Fact>>>();
	}

	public PlanningGraph getGraph() {
		return this.pg;
	}

	protected Plan goForwardAndRestart() throws NoSolutionException {
		pg.getLast().next();
		return extract();
	}

	protected Plan extract() throws NoSolutionException {

		final List<GroundAction> ga = new ArrayList<GroundAction>();
		List<Action> a = null;
		boolean failed = false;
		try {
			a = searchPlan(goalSet(pg.getLast(), this.input.getGoal()), pg
					.getLast().step());

			StaticState state = new StaticState(input.getInitialState()
					.getFacts(), input.getInitialState().getThingRegistry()
					.asSet());

			for (Action ac : a) {

				if (ac instanceof NoOpAction) {
					continue;
				}

				Evaluator e = new Evaluator(state);
				if (ac.precondition().accept(e)) {
					GroundAction g = ac.asGroundAction(state);
					ga.add(g);
					state.apply(g);
				} else {
					failed = true;
				}

			}
			// Test if the state satisfy the goal!
			Evaluator evaluator = new Evaluator(state);
			boolean satisfy = input.getGoal().asCondition().accept(evaluator);

			if (!satisfy) {
				failed = true;
			}

		} catch (FailureException fe) {
			failed = true;
		}

		if (failed && !pg.noSolution()) {
			return goForwardAndRestart();
		} else if (pg.noSolution()) {
			throw new NoSolutionException();
		}

		return new Plan() {

			@Override
			public int size() {
				return ga.size();
			}

			@Override
			public List<GroundAction> getActions() {
				return ga;
			}
		};
	}

	protected List<Action> rootFound(Set<Fact> goalSet)
			throws UnsatisfiedGoalException {
		Evaluator eval = new Evaluator(pg.getRoot());
		Set<Fact> unsatisfied = new HashSet<Fact>();
		for (Fact g : goalSet) {
			if (!new GoalImpl(g).asCondition().accept(eval)) {
				// If some goal is not satisfied
				unsatisfied.add(g);
			}
		}
		// If some goal is not satisfied, then throw failure
		if (unsatisfied.isEmpty()) {
			// Ok
			return Collections.emptyList();
		} else {
			throw new UnsatisfiedGoalException(unsatisfied, 0);
		}
	}

	protected boolean memorised(Set<Fact> goalSet, int time) {

		/**
		 * memorisation
		 */
		// init memorisation
		if (!memorisedBadGoalSets.containsKey(time)) {
			memorisedBadGoalSets.put(time, new HashSet<Set<Fact>>());
		}
		// if this goal set is already marked as bad
		if (memorisedBadGoalSets.get(time).contains(goalSet)) {
			return true;
		} else {
			return false;
		}
	}

	protected boolean memorise(Set<Fact> goalSet, int time) {
		if(!memorisedBadGoalSets.containsKey(time)){
			memorisedBadGoalSets.put(time, new HashSet<Set<Fact>>());
		}
		return memorisedBadGoalSets.get(time).add(goalSet);
	}

	protected Set<Fact> nextGoalSet(Set<Action> aset, int time) {
		Set<Fact> nextGoalSet = new HashSet<Fact>();
		Iterator<Action> ait = aset.iterator();
		while (ait.hasNext()) {
			// Add the preconditions of this action as new goal
			Action a = ait.next();

			if (getGraph().getTime(time - 1).getActionLevel().preconditions(a) != null) {
				Set<Fact> precond = getGraph().getTime(time - 1)
						.getActionLevel().preconditions(a).getShared();
				//for (Fact f : precond) {
					nextGoalSet.addAll(precond);
				//}
				// break;
			}

		}
		return nextGoalSet;
	}

	protected List<Action> searchPlan(Set<Fact> goalSet, int time)
			throws FailureException {

		/**
		 * root found
		 */
		// if time is 0, then check if the initial state satisfy the goals
		if (time == 0) {
			return rootFound(goalSet);
		}

		/**
		 * memorisation
		 */
		// if this goal set is already marked as bad, skip it
		if (memorised(goalSet, time)) {
			return null;
		}

		/**
		 * search actions
		 */
		List<Set<Action>> possibleActions = possibleActions(goalSet, time - 1);

		Iterator<Set<Action>> possit = possibleActions.iterator();
		while (possit.hasNext()) {
			Set<Action> aset = possit.next();
			Set<Fact> nextGoalSet = nextGoalSet(aset, time);
			List<Action> actions = searchPlan(nextGoalSet, (time - 1));
			if (actions != null ) {
				List<Action> plan = new ArrayList<Action>(actions);
				plan.addAll(aset);
				return plan;
			}else{
				memorise(nextGoalSet, (time - 1));
			}
		}

		// memorise
		memorise(goalSet, time);
		throw new FailureException();
	}

	protected Fact pickGoal(Set<Fact> newGoalSet) {
		// init goal set
		// focus on one goal
		Fact thisGoal = newGoalSet.iterator().next();
		newGoalSet.remove(thisGoal);
		return thisGoal;
	}

	protected Set<Action> actionsAchieveGoalAtTime(Fact thisGoal, int i) {
		Time t = getGraph().getTime(i + 1);
		PropositionLevel pl = t.getPropositionLevel();
		return pl.addedBy(thisGoal);
		/*
		FactsCollector fc = new FactsCollector();
		new GoalImpl(thisGoal).asCondition().accept(fc);
		Set<Action> achievedBy = new HashSet<Action>();

		for (Fact f : fc.getMissing()) {
			// Check propositions at next time
			Time t = getGraph().getTime(i + 1);
			PropositionLevel pl = t.getPropositionLevel();
			Set<Action> addedBy = pl.addedBy(f);
			if (addedBy == null) {
				// This should never happen..
				// FIXME throw RuntimeException?
				continue;
			} else {
				achievedBy.addAll(addedBy);
			}

		}
		return achievedBy;
		*/
	}

	protected boolean removeGoalsAchievedByThisActionAtTime(Set<Fact> goals,
			Action action, int time) {
		Set<Fact> additions = getGraph().getTime(time).getActionLevel()
				.additions(action);
		return goals.removeAll(additions);
/*		boolean ret = false;
		for (Fact f : additions) {
			if (goals.remove(f)) {
				// if at least 1 is removed
				ret = true;
			}
		}
		return ret;
		*/
	}

	protected List<Set<Action>> possibleActions(Set<Fact> goalSet, int i) {

		// if the goal is empty, return an empty set;
		if (goalSet.isEmpty()) {
			Set<Action> s = new HashSet<Action>();
			List<Set<Action>> ss = new ArrayList<Set<Action>>();
			ss.add(s);
			return ss;
		}

		// prepare action set
		List<Set<Action>> actionList = new ArrayList<Set<Action>>();
		// init goal set
		Set<Fact> newGoalSet = new HashSet<Fact>(goalSet);
		// boolean satisfied = false;
		// while(!newGoalSet.isEmpty()){
		// focus on one goal
		Fact thisGoal = pickGoal(newGoalSet);

		// get actions which achieve this goal at time i
		Set<Action> achievedBy = actionsAchieveGoalAtTime(thisGoal, i);

		// iterate over the actions which achieve this goal
		Iterator<Action> achit = achievedBy.iterator();
		// fist look only noops
		while (achit.hasNext()) {
			Action action = achit.next();
			if(action instanceof NoOpAction){
				Set<Fact> nextGoalSet = new HashSet<Fact>(newGoalSet);
				// (recursion) find actions for other goals at time i and check for
				// mutexes 
				// If no mutexes, push the actions
				List<Set<Action>> nextActionSet = possibleActions(nextGoalSet, i);
				actionList = populateActionsSetList(action, actionList,
						nextActionSet, i);
			}
		}
		
		achit = achievedBy.iterator();
		while (achit.hasNext()) {
			Action action = achit.next();
			if(!(action instanceof NoOpAction)){
				Set<Fact> nextGoalSet = new HashSet<Fact>(newGoalSet);
				// remove all other goals achieved by the action
				removeGoalsAchievedByThisActionAtTime(nextGoalSet, action, i);
				// (recursion) find actions for other goals at time i and check for
				// mutexes
				// If no mutexes, push the actions
				List<Set<Action>> nextActionSet;
				nextActionSet = possibleActions(nextGoalSet, i);
				actionList = populateActionsSetList(action, actionList,
						nextActionSet, i);
			}
		}
		
		// }
		return actionList;
	}

	protected List<Set<Action>> populateActionsSetList(Action action,
			List<Set<Action>> actionList, List<Set<Action>> nextActionSet, int i) {
		Iterator<Set<Action>> ait = nextActionSet.iterator();
		while (ait.hasNext()) {
			Set<Action> set = new HashSet<Action>(ait.next());
			if (noMutexes(action, set, i)) {
				set.add(action);
				actionList.add(set);
			}
		}
		return actionList;
	}

	protected boolean noMutexes(Action action, Set<Action> set, int i) {
		Iterator<Action> setit = set.iterator();
		Set<Action> mutex = getGraph().getTime(i).getActionLevel().getMutex()
				.getMutex(action);
		
		while (setit.hasNext()) {
			Action a = setit.next();
			if (mutex.contains(a)) {
				return false;
			}
		}
		return true;
	}

	protected Set<Fact> goalSet(Time time, Goal goal) {
		// Build the goal in relation to the application of the action level
		FactsCollector coll = new FactsCollector(time.getActionLevel().next()
				.asState());
		goal.asCondition().accept(coll);
		// XXX We only consider shared facts
		return coll.getShared();
	}
}
