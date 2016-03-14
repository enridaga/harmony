package harmony.tools.graphml;

import harmony.core.api.fact.Fact;
import harmony.core.api.renderer.Renderer;
import harmony.core.api.thing.Thing;
import harmony.core.impl.renderer.RendererImpl;
import harmony.planner.bestfirst.BestFirstSearchReport;
import harmony.planner.bestfirst.Node;
import harmony.planner.bestfirst.BestFirstPlanner.BacktracePlan;
import harmony.tools.serializers.Serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

public class SearchReportToGraphML implements Serializer<BestFirstSearchReport>{

	private BestFirstSearchReport report;
	private BacktracePlan plan;

	public SearchReportToGraphML(BestFirstSearchReport report) {
		setInput(report);
	}

	public void setInput(BestFirstSearchReport report) {
		this.report = report;
		this.plan = (BacktracePlan) report.getPlan();
	}

	public void writeTo(OutputStream stream) throws IOException {

		Document document = DocumentHelper.createDocument();

		Element graphml = document.addElement("graphml");
		graphml.add(DocumentHelper.createAttribute(graphml, "xmlns",
				"http://graphml.graphdrawing.org/xmlns"));
		graphml.add(DocumentHelper.createAttribute(graphml, "xmlns:xsi",
				"http://www.w3.org/2001/XMLSchema-instance"));
		graphml.add(DocumentHelper
				.createAttribute(
						graphml,
						"xsi:schemaLocation",
						"http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd"));
		initNodeAttributes(graphml);
		initEdgeAttributes(graphml);
		// Init graph
		Element graph = DocumentHelper.createElement("graph");
		graphml.add(graph);

		graph.add(DocumentHelper.createAttribute(graph, "id", "G"));
		graph.add(DocumentHelper.createAttribute(graph, "edgedefault",
				"directed"));

		// Nodes
		addNodes(graph, report.closedNodes());
		addNodes(graph, report.openNodes());
		for (Node n : report.closedNodes()) {
			addEdges(graph, n);
		}

		XMLWriter writer = new XMLWriter(stream);
		writer.write(document);
	}

	private String nodeId(Node node) {
		return "n" + node.hashCode();
	}

	private Set<Node> added = new HashSet<Node>();
	private void addNodes(Element graph, Set<Node> nodes) {
		for (Node n : nodes) {
			addNode(graph, n);
		}
	}

	private void addNode(Element graph, Node n){
		Element node = DocumentHelper.createElement("node");
		node.add(DocumentHelper.createAttribute(graph, "id", nodeId(n)));
		addNodeData(node, n);
		graph.add(node);
		added.add(n);
	}
	
	private void addEdges(Element graph, Node from) {
		int edge_c = 0;
		int successors = from.getSuccessors().size();
		if (successors == 0) {
			return;
		}
		double weight = 1d / successors;
		for (Node n : from.getSuccessors()) {
			if(!added.contains(n)){
				// skip it, this node have never been reached!
				continue;
			}
			Element edge = DocumentHelper.createElement("edge");
			edge.add(DocumentHelper.createAttribute(edge, "id",
					edgeId(from, edge_c)));
			edge.add(DocumentHelper.createAttribute(edge, "directed", "true"));
			edge.add(DocumentHelper.createAttribute(edge, "source",
					nodeId(from)));
			edge.add(DocumentHelper.createAttribute(edge, "target", nodeId(n)));
			edge.add(DocumentHelper.createAttribute(edge, "weight",
					Double.toString(weight)));
			edge.add(DocumentHelper.createAttribute(edge, "label", n
					.getAction().operator().getName()));
			addEdgeData(edge, from, n);
			graph.add(edge);
			edge_c++;
		}
	}

	private String edgeId(Node from, int edge_c) {
		return "e" + from.hashCode() + "_" + edge_c;
	}

	private void addEdgeData(Element edge, Node source, Node target) {
		Element actionData = DocumentHelper.createElement("data");
		actionData.add(DocumentHelper.createAttribute(actionData, "key",
				"action"));
		actionData.add(DocumentHelper.createText(target.getAction().operator()
				.getName()));
		edge.add(actionData);
		Element addedData = DocumentHelper.createElement("data");
		addedData.add(DocumentHelper
				.createAttribute(actionData, "key", "added"));
		Renderer rendy = new RendererImpl();
		for (Fact f : target.getAction().add()) {
			rendy.append(f).append(System.getProperty("line.separator"));
		}
		addedData.add(DocumentHelper.createText(rendy.toString()));
		edge.add(addedData);

		Element removedData = DocumentHelper.createElement("data");
		removedData.add(DocumentHelper.createAttribute(actionData, "key",
				"removed"));
		for (Fact f : target.getAction().remove()) {
			rendy.append(f).append(System.getProperty("line.separator"));
		}
		removedData.add(DocumentHelper.createText(rendy.toString()));
		edge.add(removedData);

		Element createdData = DocumentHelper.createElement("data");
		createdData.add(DocumentHelper.createAttribute(actionData, "key",
				"created"));
		for (Thing m : target.getAction().create()) {
			rendy.append(m).append(System.getProperty("line.separator"));
		}
		createdData.add(DocumentHelper.createText(rendy.toString()));
		edge.add(createdData);

		Element destroyedData = DocumentHelper.createElement("data");
		destroyedData.add(DocumentHelper.createAttribute(actionData, "key",
				"destroyed"));
		for (Thing m : target.getAction().destroy()) {
			rendy.append(m).append(System.getProperty("line.separator"));
		}
		destroyedData.add(DocumentHelper.createText(rendy.toString()));
		edge.add(destroyedData);

		// isPlanAction
		Element isPlanAction = DocumentHelper.createElement("data");
		isPlanAction.add(DocumentHelper.createAttribute(isPlanAction, "key",
				"isPlanAction"));
		String isPlanActionValue = "false";
		if (plan != null) {
			if (plan.getPath().contains(source)
					&& plan.getPath().contains(target)) {
				isPlanActionValue = "true";
			}
		}
		isPlanAction.add(DocumentHelper.createText(isPlanActionValue));
		edge.add(isPlanAction);
	}

	private void addNodeData(Element element, Node node) {
		// isGoalPath
		Element isGoalPathData = DocumentHelper.createElement("data");
		isGoalPathData.add(DocumentHelper.createAttribute(isGoalPathData,
				"key", "isGoalPath"));
		if (plan != null) {
			isGoalPathData.add(DocumentHelper.createText(plan.getPath()
					.contains(node) ? "true" : "false"));
		} else {
			isGoalPathData.add(DocumentHelper.createText("false"));
		}
		element.add(isGoalPathData);

		// Is it open or closed?
		Element branched = DocumentHelper.createElement("data");
		branched.add(DocumentHelper
				.createAttribute(branched, "key", "branched"));
		branched.add(DocumentHelper.createText(report.closedNodes().contains(
				node) ? "true" : "false"));
		element.add(branched);

		// Is it a no-solution node?
		boolean noSolutionBoolean = report.closedNodes().contains(node)
				&& (!node.isGoal()) && node.getSuccessors().size() == 0;
		Element noSolution = DocumentHelper.createElement("data");
		noSolution.add(DocumentHelper.createAttribute(branched, "key",
				"noSolution"));
		noSolution.add(DocumentHelper.createText(noSolutionBoolean ? "true"
				: "false"));
		element.add(noSolution);

		// Depth
		Element depth = DocumentHelper.createElement("data");
		depth.add(DocumentHelper.createAttribute(branched, "key", "depth"));
		depth.add(DocumentHelper.createText(Integer.toString(node.getDepth())));
		element.add(depth);

		// Facts
		Element facts = DocumentHelper.createElement("data");
		facts.add(DocumentHelper.createAttribute(branched, "key", "facts"));
		Renderer rendy = new RendererImpl();
		for (Fact f : node.getFacts()) {
			rendy.append(f).append(System.getProperty("line.separator"));
		}
		facts.add(DocumentHelper.createText(rendy.toString()));
		element.add(facts);
	}

	private void initKey(Element graph, String id, String forType,
			String attrName, String attrType) {
		Element key = DocumentHelper.createElement("key");
		key.add(DocumentHelper.createAttribute(key, "id", id));
		key.add(DocumentHelper.createAttribute(key, "for", forType));
		key.add(DocumentHelper.createAttribute(key, "attr.name", attrName));
		key.add(DocumentHelper.createAttribute(key, "attr.type", attrType));
		graph.add(key);
	}

	private void initEdgeKey(Element graph, String name, String type) {
		initKey(graph, name, "edge", name, type);
	}

	private void initNodeKey(Element graph, String name, String type) {
		initKey(graph, name, "node", name, type);
	}

	private void initNodeAttributes(Element graph) {
		initNodeKey(graph, "isGoalPath", "string");
		initNodeKey(graph, "branched", "string");
		initNodeKey(graph, "depth", "double");
		initNodeKey(graph, "facts", "string");
		initNodeKey(graph, "noSolution", "string");
	}

	private void initEdgeAttributes(Element graph) {
		initEdgeKey(graph, "action", "string");
		initEdgeKey(graph, "added", "string");
		initEdgeKey(graph, "removed", "string");
		initEdgeKey(graph, "created", "string");
		initEdgeKey(graph, "destroyed", "string");
		initEdgeKey(graph, "isPlanAction", "string");
	}
}
