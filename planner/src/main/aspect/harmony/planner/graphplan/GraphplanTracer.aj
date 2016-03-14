package harmony.planner.graphplan;

import harmony.core.api.fact.Fact;
import harmony.core.api.operator.Action;
import harmony.core.api.plan.Plan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public aspect GraphplanTracer {

	protected Logger logger;

	public GraphplanTracer() {
		logger = LoggerFactory.getLogger(getClass());
	}

	pointcut planningGraphImpl(): within(PlanningGraphImpl);

	pointcut newPlanningGraphImpl(): call (PlanningGraphImpl.new(..));

	pointcut timeImpl(): within(TimeImpl);

	pointcut newTimeImpl(): call (TimeImpl.new(..));

	pointcut step(): execution(int step());

	pointcut search(): within(Graphplan) && execution(Plan search(..));

	pointcut planExtractor(): within(PlanExtractor);

	pointcut pickGoal(): planExtractor() && execution(Fact pickGoal(..));

	pointcut actionsAchieveGoalAtTime(Fact thisGoal, int i): planExtractor() && execution(Set<Action> actionsAchieveGoalAtTime(Fact, int) ) && args( thisGoal, i);

	pointcut planExtractorSearch(): planExtractor() && execution(Plan search(..));

	pointcut actionLevelImpl(): within(TimeImpl$ActionLevelImpl);
	
	pointcut mutex(Action a0, Action a1): actionLevelImpl() && execution(boolean mutex(Action,Action)) && args(a0, a1);

	pointcut searchPlan(Set<Fact> goalSet, int time): planExtractor() && execution(List<Action> searchPlan(..)) && args(goalSet, time);

	pointcut possibleActions(Set<Fact> facts, int i): planExtractor() && execution(List<Set<Action>> possibleActions(Set<Fact>, int)) && args(facts, i);

	pointcut rootFound(): planExtractor() && execution(List<Action> rootFound(..));

	pointcut memorised():  planExtractor() && execution(boolean memorised(..));

	pointcut memorise():  planExtractor() && execution(boolean memorise(..));

	pointcut nextGoalSet(): planExtractor() && execution(Set<Fact> nextGoalSet(Set<Action>, int));

	private List<String> indent = new ArrayList<String>();

	private String tab(String symbol) {
		String tab = "| ";
		for (String c : indent) {
			tab += c + " ";
		}
		return tab;
	}

	private String tab() {
		return tab("-");
	}

	private void indent(String symbol) {
		indent.add(symbol);
	}

	private void deindent() {
		indent.remove(indent.size() - 1);
	}
	
	private void indentReset(){
		indent = new ArrayList<String>();
			
	}
	
	before(): search(){
		indentReset();
		higherPA = 0;
		logger.info("Starting search");
	}

	Object around(): memorised(){
		Object o = proceed();
		logger.info("memorised: {}",o);
		return o;
	}

	Object around(): memorise(){
		Object o = proceed();
		logger.info("memorise: {}",o);
		return o;
	}
	
	List<Action> around(Set<Fact> goalSet, int time): searchPlan(goalSet, time){
		indent(Integer.toString(time));
		logger.info("{}searching plan at time {}", tab(), time);
		List<Action> al = proceed(goalSet, time);
		logger.info("{}plan: {}", tab(), al);
		deindent();
		return al;
	}

//	boolean around(Action a0, Action a1): mutex(a0, a1){
//		boolean mutex = proceed(a0, a1);
//		logger.info("Mutex? {} {} : {}", new Object[]{a0,a1,new Boolean(mutex)});
//		return mutex;
//	}
	
	after() returning(Fact fact): pickGoal(){
		logger.debug(tab() + "picked: {}", fact);
	}

	Object around(Fact thisGoal, int i): actionsAchieveGoalAtTime(thisGoal, i){
		Object o = proceed(thisGoal, i);
		logger.debug("{}achieving {}: {}", new Object[] { tab(), thisGoal, o });
		return o;
	}
	
	long higherPA = 0l;

	Object around(Set<Fact> facts, int i): possibleActions(facts, i){
		indent(">");
		long started = System.currentTimeMillis();
		logger.debug("{}goals {} to be satisfied at time {}", new Object[] {
				tab(), facts, i });
		Object o = proceed(facts, i);
		long time = ( System.currentTimeMillis() - started);
		if(time > higherPA){
			higherPA = time;
		}
		//logger.debug("{}satisfied by: {} in {}ms", new Object[] { tab(), o, time });
		//logger.info("{} satisfied by {} in {}ms", new Object[] { tab(), ((Collection)o).size(), time });
		deindent();
		return o;
	}

	Object around(): rootFound(){
		logger.info("{}found root", tab());
		Object o = proceed();
		@SuppressWarnings("unchecked")
		List<Action> actions = (List<Action>) o;
		if (actions.isEmpty()) {
			logger.info("{}all goals are satisfied", tab());
		}
		return o;
	}

	Object around(): newPlanningGraphImpl(){
		logger.info("Generating planning graph ...");
		Object o = proceed();
		logger.info("Planning graph generation has finished");
		PlanningGraph pg = (PlanningGraph) o;
		logger.info("Goal found: {}", pg.goalFound());
		logger.info("No solution: {}", pg.noSolution());
		logger.warn("Higher pa: {}", higherPA);
		return o;
	}

	after() returning (Time time): newTimeImpl(){

		logger.info("Building Time " + time.step());
		indentReset();
		logger.debug("Step {}:", time.step());
		logger.debug("  Propositions [{}]:", time.getPropositionLevel().asSet()
				.size());
		List<String> propList = new ArrayList<String>();
		Map<String, List<String>> propMutexMap = new HashMap<String, List<String>>();
		Iterator<Fact> props = time.getPropositionLevel().asSet().iterator();
		while (props.hasNext()) {
			Fact f = props.next();
			String fs = f.toString();
			propList.add(fs);
			if (!propMutexMap.containsKey(fs)) {
				propMutexMap.put(fs, new ArrayList<String>());
			}
			for (Fact o : time.getPropositionLevel().getMutex().getMutex(f)) {
				List<String> pm = propMutexMap.get(fs);
				if (!pm.contains(o.toString())) {
					pm.add(o.toString());
				}
			}
		}
		Collections.sort(propList);
		Iterator<String> prs = propList.iterator();
		while (prs.hasNext()) {
			String f = prs.next();
			logger.debug("    {}", f);
			StringBuilder ml = new StringBuilder();
			List<String> ls = propMutexMap.get(f);
			Collections.sort(ls);
			for (String s : ls) {
				ml.append(" ");
				ml.append(s);
			}
			logger.debug("      Mutex: {}", ml.toString());
		}

		Set<Action> actionSet = time.getActionLevel().asSet();
		logger.debug("  Actions [{}]:", actionSet.size());
		List<String> actionList = new ArrayList<String>();
		Map<String, List<String>> actionMutexMap = new HashMap<String, List<String>>();
		for (Action ac : actionSet) {
			actionList.add(ac.toString());
			if (!actionMutexMap.containsKey(ac.toString())) {
				actionMutexMap.put(ac.toString(), new ArrayList<String>());
			}
			for (Action o : time.getActionLevel().getMutex().getMutex(ac)) {
				if (!actionMutexMap.get(ac.toString()).contains(o.toString())) {
					actionMutexMap.get(ac.toString()).add(o.toString());
				}
			}
		}
		Collections.sort(actionList);
		Iterator<String> actit = actionList.iterator();
		while (actit.hasNext()) {
			String a = actit.next();
			logger.debug(a);
			StringBuilder ml = new StringBuilder();
			List<String> mut = actionMutexMap.get(a);
			Collections.sort(mut);
			for (String s : mut) {
				ml.append(" ");
				ml.append(s);
			}
			logger.debug("      Mutex: {}", ml.toString());
		}
	}

}
