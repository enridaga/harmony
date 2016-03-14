package harmony.planner.bestfirst;

import harmony.core.api.fact.Fact;
import harmony.core.api.operator.GroundAction;
import harmony.core.api.plan.Plan;
import harmony.core.api.renderer.Renderer;
import harmony.core.api.thing.Thing;
import harmony.core.impl.renderer.RendererImpl;
import harmony.planner.bestfirst.BestFirstPlanner;
import harmony.planner.bestfirst.Node;
import harmony.planner.bestfirst.heuristic.BestNodeHeuristic;
import harmony.planner.bestfirst.heuristic.CompositeBestNodeHeuristic;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public aspect BestFirstTracer {

	protected Logger log = null;

	public BestFirstTracer() {
		log = LoggerFactory.getLogger(getClass());
	}

	pointcut compositeHeuristic(): within(harmony.planner.bestfirst.heuristic.HierarchicalCompositeBestNodeHeuristic);

	pointcut lastApplied(): compositeHeuristic() && execution (int compare(Node, Node));

	pointcut opSearcher(): within(harmony.planner.bestfirst.BestFirstPlanner);

	pointcut opNode(): within(harmony.planner.bestfirst.NodeImpl);

	pointcut getBest(): opSearcher() && execution (Node getBest());

	pointcut buildSuccessors(): opNode() && execution (Set<Node> buildSuccessors());

	pointcut planFound(): opSearcher() && execution (Plan search());

	Object around(): getBest(){
		Object o = proceed();
		Node node = ((Node) o);
		BestFirstPlanner pl = ((BestFirstPlanner) thisJoinPoint.getThis());
		int equalNodes = 0;
		int openNodesSize = pl.getOpenNodes().size();
		for (Node n : pl.getOpenNodes()) {
			int comparison = pl.getHeuristic().compare(n, node);
			if (comparison == 0) {
				equalNodes++;
			}
		}

		BestNodeHeuristic h = pl.getHeuristic();
		int gd = -1;
		gd = h.getGoalDistance(node);

		if (!node.isRoot()) {
			GroundAction act = node.getAction();
			int added = act.add().length;
			int removed = act.remove().length;
			String actName = act.operator().getName();
			Renderer rendy = new RendererImpl();
			for (Thing m : act.parameters()) {
				rendy.append(" ");
				rendy.append(m);
			}

			// log.info("=========================================");
			log.info(branchedTimes + " Best: [BF: " + branchFactor + "][GD: "
					+ gd + "][O: " + equalNodes + "/" + openNodesSize + "]["
					+ actName + " (" + rendy.toString() + ") +" + added + " -"
					+ removed + "][Facts: {}][D: {}].", node.getFacts().size(),
					node.getDepth());
			// if(actName.equals("Extract")){
			// while(node.getParent()!=null){
			// reportNode(node);
			// node = node.getParent();
			// }
			// reportNode(node);
			// }
		}

		return o;
	}

	private Map<BestNodeHeuristic, Integer> heuristicStats = new HashMap<BestNodeHeuristic, Integer>();
	private int nullHeuristic = 0;
	private int comparisons = 0;

	after(): lastApplied(){
		comparisons++;
		CompositeBestNodeHeuristic ch = ((CompositeBestNodeHeuristic) thisJoinPoint
				.getThis());
		BestNodeHeuristic last = ch.getLastApplied();
		if (last == null) {
			nullHeuristic += 1;
			return;
		}

		if (!heuristicStats.containsKey(last)) {
			heuristicStats.put(last, 1);
		} else {
			Integer count = heuristicStats.get(last);
			heuristicStats.put(last, count + 1);
		}
	}

	after(): planFound(){
		log.info("{} nodes comparisons", comparisons);
		log.info("Heuristics usage stats:");
		for (Entry<BestNodeHeuristic, Integer> e : heuristicStats.entrySet()) {
			log.info(" {} used {} times", e.getKey().getClass(), e.getValue());
		}
		log.info(" No choice: {} times", nullHeuristic);
	}

	public void reportNode(Node node) {
		GroundAction act = node.getAction();
		Renderer rendy = new RendererImpl();

		if (node.isGoal()) {
			log.info("this is the goal node");
		}
		if (node.isRoot()) {
			log.info("this is the root node");
		} else {
			log.info("Action: {}", act.operator().getName());

			// log.info("-----------------------------------------");
			// for(Thing f :node.getParent().getModelRegistry().asSet()){
			// log.info("< {} ", rendy.append(f).toString());
			// }
			// for(Fact f :node.getParent().getFacts()){
			// log.info("< {} ", rendy.append(f).toString());
			// }
			log.info("-----------------------------------------");
			for (Fact f : act.add()) {
				log.info("+ {} ", rendy.append(f).toString());
			}
			for (Fact f : act.remove()) {
				log.info("- {} ", rendy.append(f).toString());
			}
		}
		log.info("-----------------------------------------");
		for (Thing f : node.getThingRegistry().asSet()) {
			log.info("m {} ", rendy.append(f).toString());
		}
		for (Fact f : node.getFacts()) {
			log.info("f {} ", rendy.append(f).toString());
		}
		log.info("-----------------------------------------");
	}

	int branchedTimes = 0;
	float branchFactor = 0;

	/**
	 * This method inspect the branching factor and computes the average branch
	 * factor
	 * 
	 * @return
	 */
	Object around(): buildSuccessors(){
		Object o = proceed();
		int branches = ((Set<?>) o).size();
		float tmp = (branchFactor * (branchedTimes)) + branches;
		branchedTimes++;
		branchFactor = tmp / branchedTimes;
		return o;
	}

	// Object around(): buildSuccessors(){
	// long start = System.nanoTime();
	// Object o = proceed();
	// long end = System.nanoTime();
	// Set set = (Set) o;
	// log.info("Successors: {} nodes in {}ns", set.size(), end - start);
	// return o;
	// }
	//
	// Object around(): buildOperatorSuccessors(){
	//
	// Object[] args = thisJoinPoint.getArgs();
	// Operator op = ((Operator) args[1]);
	// log.info("Check operator " + op.getName());
	// long start = System.nanoTime();
	// Object o = proceed();
	// long end = System.nanoTime();
	// Set set = (Set) o;
	// log.info(" {} successors in {}ns", set.size(), end - start);
	// return o;
	// }

	// Object around(): getCandidates(){
	// Object[] args = thisJoinPoint.getArgs();
	// ThingRegistry r = (ThingRegistry) args[0];
	// long start = System.nanoTime();
	// Object o = proceed();
	// long end = System.nanoTime();
	// List<List<Thing>> list = (List<List<Thing>>) o;
	// String m = r.size() + " things in registry.";
	// log.info(m + " Computed candidates in {}ns", end - start);
	// return o;
	// }

	// long lastProcNodeTime = 0;
	// long lastFreeMemory = 0;
	//
	// Object around(): processNode(){
	// Object[] args = thisJoinPoint.getArgs();
	// SortedSet<Node> open = (SortedSet<Node>) args[1];
	// List<Node> closed = (List<Node>) args[2];
	// log.info("Process node. Open: {}. Closed: {}", open.size(),
	// closed.size());
	// long start = System.nanoTime();
	// Object o = proceed();
	// long end = System.nanoTime();
	// log.info("Processed in {}ns [{}ns more then last time]", end - start,
	// end - start - (lastProcNodeTime));
	// lastProcNodeTime = end - start;
	// long freeMem = Runtime.getRuntime().freeMemory();
	// log.info(" Free: {} bytes [{} bytes more then last time]", freeMem,
	// freeMem - lastFreeMemory);
	// lastFreeMemory = freeMem;
	// return o;
	// }

	// Object around(): getDimensions(){
	// Object o = proceed();
	// List<Integer> dimensions = (List<Integer>) o;
	// String dim = " dimensions: ";
	// int tot = 1;
	// for (Integer i : dimensions) {
	// dim += " " + i + " ";
	// tot = tot * i;
	// }
	// log.info(tot + dim);
	// return o;
	// }
	//
	// Object around(): evaluateCandidates(){
	//
	// long start = System.nanoTime();
	// Object o = proceed();
	// long end = System.nanoTime();
	// log.info("Evaluated candidates in {}ns", end - start);
	//
	// return o;
	// }

}
