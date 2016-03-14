package harmony.maven.plugin;

import harmony.core.api.goal.Goal;
import harmony.core.api.operator.Action;
import harmony.core.api.plan.Plan;
import harmony.core.api.renderer.Renderer;
import harmony.core.impl.renderer.RendererImpl;
import harmony.planner.NoSolutionException;
import harmony.planner.PlannerInput;
import harmony.planner.bestfirst.BestFirstPlanner;
import harmony.planner.bestfirst.BestFirstSearchReport;
import harmony.planner.bestfirst.Node;
import harmony.planner.bestfirst.heuristic.BestNodeHeuristic;
import harmony.planner.bestfirst.heuristic.HierarchicalCompositeBestNodeHeuristic;
import harmony.tools.graphml.SearchReportToGraphML;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * @goal plan
 */
public class HarmonyPlan extends AbstractMojo {

	/**
	 * @parameter expression="${project.build.directory}"
	 */
	private String directory = null;

	/**
	 * @parameter expression="${plan.plannerInput}"
	 */
	private String plannerInput = null;

	/**
	 * @parameter expression="${plan.heuristics}"
	 */
	private String[] heuristics = null;
	
	/**
	 * @parameter expression="${plan.maxClosedNodes}"
	 */
	private int maxClosedNodes = -1;
	
	

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info(directory);
		if (plannerInput != null) {
			getLog().debug("plannerInput: " + plannerInput);
			try {
				@SuppressWarnings("unchecked")
				Class<PlannerInput> clazz = (Class<PlannerInput>) getClass()
						.getClassLoader().loadClass(plannerInput);
				PlannerInput input = (PlannerInput) clazz.newInstance();
				Goal goal = input.getGoal();
				goal.asCondition();
				
				HierarchicalCompositeBestNodeHeuristic heury = new HierarchicalCompositeBestNodeHeuristic();
				for (String h : heuristics) {
					getLog().debug("heuristic: " + h);
					@SuppressWarnings("unchecked")
					Class<BestNodeHeuristic> hclazz = (Class<BestNodeHeuristic>) getClass()
							.getClassLoader().loadClass(h);
					BestNodeHeuristic heuristic = (BestNodeHeuristic) hclazz
							.newInstance();
					heury.attach(heuristic);
				}

				BestFirstPlanner planner = new BestFirstPlanner(input, heury);

				planner.setMaxClosedNodes(maxClosedNodes);
				
				Renderer rendy = new RendererImpl();

				long started = System.nanoTime();
				long finished;
				BestFirstSearchReport n;
				try {
					Plan pl = planner.search();
					finished = System.nanoTime();
					for (Action a : pl.getActions()) {
						getLog().info(rendy.append(a).toString());
					}
					n = (BestFirstSearchReport) planner.getLastSearchReport();
				} catch (NoSolutionException e) {
					n = (BestFirstSearchReport) e.getSearchReport();
					finished = System.nanoTime();
					e.printStackTrace();

					getLog().info("");
					getLog().error("No solution!");
					getLog().info(n.closedNodes().size() + " closed nodes");
					for (Node d : n.closedNodes()) {
						rendy.append(d);
						getLog().info(rendy.toString());
					}

					getLog().info(n.openNodes().size() + " open nodes");
					for (Node d : n.openNodes()) {
						rendy.append(d);
						getLog().info(rendy.toString());
					}
				}
				double duration = (double) (finished - started) / 1000000000.0;
				getLog().info("Done in " + duration + " seconds");

				// Saving results
				String harmonyTargetDirectoryName = directory
						+ System.getProperty("file.separator") + "harmony"
						+ System.getProperty("file.separator");
				File harmonyTargetDirectory = new File(
						harmonyTargetDirectoryName);
				if (harmonyTargetDirectory.exists()) {
					harmonyTargetDirectory.delete();
				}
				harmonyTargetDirectory.mkdir();
				String planTargetDirectoryName = harmonyTargetDirectoryName
						+ "plan" + System.getProperty("file.separator");
				File planTargetDirectory = new File(planTargetDirectoryName);
				if (planTargetDirectory.exists()) {
					planTargetDirectory.delete();
				}
				planTargetDirectory.mkdir();

				SearchReportToGraphML graph = new SearchReportToGraphML(n);
				String graphmlFileName = planTargetDirectoryName + plannerInput + ".graphml";
				File graphmlFile = new File(graphmlFileName);
				if (graphmlFile.exists()) {
					graphmlFile.delete();
				}
				graph.writeTo(new FileOutputStream(graphmlFile));

				TransformerFactory tFactory = TransformerFactory.newInstance();
				String graphmlYedFileName = planTargetDirectoryName + plannerInput + "-yed.graphml";
				Transformer transformer = tFactory
						.newTransformer(new StreamSource(graph.getClass()
								.getResourceAsStream("/yed.xslt")));
				transformer.transform(new StreamSource(graphmlFile), new StreamResult(
						new FileOutputStream(graphmlYedFileName)));

			} catch (ClassNotFoundException e) {
				getLog().error(e);
			} catch (InstantiationException e) {
				getLog().error(e);
			} catch (IllegalAccessException e) {
				getLog().error(e);
			} catch (FileNotFoundException e) {
				getLog().error(e);
			} catch (IOException e) {
				getLog().error(e);
			} catch (TransformerConfigurationException e) {
				getLog().error(e);
			} catch (TransformerException e) {
				getLog().error(e);
			}
		} else {
			getLog().warn("Missing configuration parameter: plannerInput");
		}
	}

}
