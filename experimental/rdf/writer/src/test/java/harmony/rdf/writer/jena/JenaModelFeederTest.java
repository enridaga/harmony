package harmony.rdf.writer.jena;

import harmony.core.api.fact.Fact;
import harmony.core.api.goal.Goal;
import harmony.core.api.operator.Operator;
import harmony.core.api.property.Property;
import harmony.core.api.thing.Thing;
import harmony.core.impl.condition.And;
import harmony.core.impl.condition.AssertFact;
import harmony.core.impl.fact.BasicFact;
import harmony.core.impl.goal.GoalImpl;
import harmony.core.impl.property.BasicProperty;
import harmony.core.impl.thing.Something;
import harmony.lod.operator.Append;
import harmony.lod.property.HasSlice;
import harmony.rdf.writer.lod.LODDomain;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFErrorHandler;
//import org.apache.jena.rdf.model.RDFWriter;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.Lang;

public class JenaModelFeederTest {

	private JenaModelFeeder feeder;
	private Logger log = LoggerFactory.getLogger(getClass());

	@Before
	public void prepare() {
		feeder = new JenaModelFeeder(new LODDomain(
				"http://www.example.org/jenamodelfeedertest/"));
	}

	@Test
	public void testModel() throws Exception {
		log.info("testModel()");
		Thing m = new Something("aModel");
		feeder.traverse(m);
		report(feeder.getModel());
		writeFile("testModel.rdf");
		Assert.assertTrue(readFile("testModel.rdf").size() == 3);
	}

	@Test
	public void testOperator() throws Exception {
		log.info("testOperator()");
		Operator o = new Append();
		feeder.traverse(o);
		report(feeder.getModel());
		writeFile("testOperator.rdf");
		Assert.assertTrue(readFile("testOperator.rdf").size() == 10);
	}

	@Test
	public void testProperty() throws Exception {
		log.info("testProperty()");
		Property o = new HasSlice();
		feeder.traverse(o);
		report(feeder.getModel());
		writeFile("testProperty.rdf");
		Assert.assertTrue(readFile("testProperty.rdf").size() == 10);
	}

	@Test
	public void testFact() throws Exception {
		log.info("testFact()");
		@SuppressWarnings("unchecked")
		Fact i = new BasicFact(new BasicProperty("aProperty",
				Something.class, Something.class), new Something(
				"aModel1"), new Something("aModel2"));
		feeder.traverse(i);
		report(feeder.getModel());
		writeFile("testFact.rdf");
		Assert.assertTrue(readFile("testFact.rdf").size() == 21);
	}

	@Test
	public void testGoal() throws Exception {
		log.info("testGoal()");
		@SuppressWarnings("unchecked")
		Goal g = new GoalImpl(new And().append(new AssertFact(new BasicFact(
				new BasicProperty("aProperty", Thing.class, Thing.class),
				new Something("X"), new Something("Y")))));
		feeder.traverse(g);
		report(feeder.getModel());
		writeFile("testGoal.rdf");
		report(readFile("testGoal.rdf"));
	}

	private void writeFile(String fileName) throws Exception {
//		RDFDataMgr.write();
//		RDFWriter w = feeder.getModel().getWriter();
////		RDFDataMgr.;
//		w.setErrorHandler(new RDFErrorHandler() {
//
//			@Override
//			public void warning(Exception e) {
//				log.warn("writeFile() ", e);
//			}
//
//			@Override
//			public void fatalError(Exception e) {
//				log.error("writeFile() ", e);
//			}
//
//			@Override
//			public void error(Exception e) {
//				log.error("writeFile() ", e);
//			}
//		});
		File f = file(fileName);
		f.delete();
		f.getParentFile().mkdirs();
		RDFDataMgr.write(new FileOutputStream(f), feeder.getModel(), Lang.RDFXML) ;
//		w.write(feeder.getModel(), new FileOutputStream(f), null);
		log.info(" written to {}", f.getAbsolutePath());
	}

	private File file(String name) throws URISyntaxException, IOException {
		File res = new File(getClass().getResource("").getFile()
				+ System.getProperty("file.separator"), name);
		log.debug("File: {} ", res);
		return res;
	}

	private org.apache.jena.rdf.model.Model readFile(String fileName)
			throws URISyntaxException, IOException {
		File f = file(fileName);
		log.info(" reading {}", f.getAbsolutePath());
		return ModelFactory.createDefaultModel().read(f.toURI().toString());
	}

	private void report(org.apache.jena.rdf.model.Model model) {
		log.info(" {} statements loaded:", model.size());
		StmtIterator i = feeder.getModel().listStatements();
		while (i.hasNext()) {
			log.info(" {}", i.next());
		}
	}
}
