package harmony.rdf.writer.jena;

import harmony.rdf.writer.RDFWriterTraverseerDelegate;
import harmony.rdf.writer.mapping.Mapper;
import harmony.tools.traverse.GenericTraverseerTaskMaster;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;

public class JenaModelFeeder extends GenericTraverseerTaskMaster {

	private Model model = null;
	private Mapper mapper = null;

	public JenaModelFeeder(Mapper mapper) {
		super();
		this.mapper = mapper;
		setup();
	}

	private void setup() {
		model = ModelFactory.createDefaultModel();
		JenaStatementBuilder builder = new JenaStatementBuilder();
		for (RDFWriterTraverseerDelegate del : mapper
				.getDelegates(builder)) {
			addDelegate(del);
		}
	}

	@Override
	public void handleExperience(Object experience) {
		if (experience instanceof Statement) {
			model.add((Statement)  experience);
		}
	}

	public Model getModel() {
		return model;
	}
}
