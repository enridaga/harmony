package harmony.rdf.writer.jena;

import harmony.rdf.writer.TripleFactory;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.AnonId;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFList;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.apache.jena.rdf.model.impl.ResourceImpl;
import org.apache.jena.rdf.model.impl.StatementImpl;

public class JenaStatementBuilder implements TripleFactory<Statement> {

	private int bnodeIndex = 0;
	
	public String bnode() {
		String bnode = "_:bnode_" + Integer.toString(bnodeIndex);
		bnodeIndex++;
		return bnode;
	}
	
	private Resource buildResource(String string) {
		if (string.startsWith("_:")) {
			return new ResourceImpl(new AnonId(string.substring(2)));
		} else {
			return new ResourceImpl(string);
		}
	}

	public Statement spo(String subject, String property, String object) {
		Resource s = buildResource(subject);
		Property p = new PropertyImpl(property);
		Resource o = buildResource(object);
		return new StatementImpl(s, p, o);
	}

	public Statement spll(String subject, String property, String value, String lang) {
		Resource s = buildResource(subject);
		Property p = new PropertyImpl(property);
		Literal o = ModelFactory.createDefaultModel()
				.createLiteral(value, lang);
		return new StatementImpl(s, p, o);
	}

	public Statement spvd(String subject, String property, String value,
			String datatype) {
		Resource s = buildResource(subject);
		Property p = new PropertyImpl(property);
		Literal o = ModelFactory.createDefaultModel().createTypedLiteral(value,
				datatype);
		return new StatementImpl(s, p, o);
	}

	public Statement isA(String subject, String object) {
		return spo(subject, RDF_NS + "type", object);
	}

	public Statement lbl(String subject, String value, String lang) {
		return spll(subject, RDFS_NS + "label", value, lang);
	}

	public Statement list(String subject, String property, String[] objects) {
		Model q = ModelFactory.createDefaultModel();
		List<Resource> listValues = new ArrayList<Resource>();
		for (String obj : objects) {
			listValues.add(new ResourceImpl(obj));
		}
		RDFList list = q.createList(listValues.iterator());
		Resource s = buildResource(subject);
		Property p = new PropertyImpl(property);
		return new StatementImpl(s, p, list);
	}

	public Statement list(String subject, String property, List<String> objects) {
		return list(subject, property, objects.toArray(new String[objects.size()]));
	}

}
