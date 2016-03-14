package harmony.lod.model.impl.slice;

import harmony.lod.model.api.slice.Frame;
import harmony.lod.model.api.slice.StatementTemplate;
import harmony.lod.model.api.symbol.Datatype;
import harmony.lod.model.api.symbol.IRI;
import harmony.lod.model.api.symbol.Symbol;
import harmony.lod.model.impl.symbol.DatatypeImpl;
import harmony.lod.model.impl.symbol.IRIImpl;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FrameImplTest {

	FrameImpl frame = null;
	IRI name = null;
	IRI surname = null;
	IRI email = null;
	Datatype emailDatatype = null;
	IRI type = null;
	IRI person = null;
	IRI someone = null;

	@Before
	public void before() throws FrameException {
		name = new IRIImpl("name");
		surname = new IRIImpl("surname");
		email = new IRIImpl("email");
		emailDatatype = new DatatypeImpl("emailDatatype");
		person = new IRIImpl("Person");
		someone = new IRIImpl("me");

		/**
		 * FIXME Choose how to deal with vocabularies
		 */
		type = new IRIImpl("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");

		StatementTemplateImpl st1 = new StatementTemplateImpl(null, name);
		StatementTemplateImpl st2 = new StatementTemplateImpl(null, surname);
		StatementTemplateImpl st3 = new StatementTemplateImpl(null, email, emailDatatype);

		frame = new FrameImpl(st1, st2, st3);
	}

	@Test
	public void constructor() throws FrameException {
		Assert.assertTrue(frame != null);
	}

	@Test
	public void asSet() {
		Assert.assertTrue(!frame.asSet().isEmpty());
		Assert.assertTrue(frame.asSet().size() == 3);
	}

	@Test
	public void getSubject() throws FrameException {
		Assert.assertNull(frame.getSubject());
		Assert.assertFalse(frame.hasSubject());

		StatementTemplateImpl subject = new StatementTemplateImpl(someone);
		frame.addStatementTemplate(subject);

		Assert.assertTrue(frame.hasSubject());
		Assert.assertNotNull(frame.getSubject());
	}

	@Test
	public void getPredicates() {
		Assert.assertTrue(frame.getPredicates() instanceof Set<?>);
		Assert.assertTrue(frame.getPredicates().contains(name));
		Assert.assertTrue(frame.getPredicates().contains(surname));
		Assert.assertTrue(frame.getPredicates().contains(email));
		Assert.assertTrue(!frame.getPredicates().contains(emailDatatype));

		Set<IRI> predicates = new HashSet<IRI>();
		predicates.add(name);
		predicates.add(surname);
		predicates.add(email);

		Assert.assertTrue(frame.getPredicates().equals(predicates));
	}

	@Test
	public void getTypes() throws FrameException {
		StatementTemplateImpl st = new StatementTemplateImpl(null, type, person);
		frame.addStatementTemplate(st);

		Assert.assertTrue(frame.hasTypes());
		Assert.assertTrue(frame.getTypes().contains(person));
	}

	@Test
	public void getSymbolsInSignature() throws FrameException {
		StatementTemplateImpl st = new StatementTemplateImpl(someone, name);

		frame.addStatementTemplate(st);

		Set<Symbol> s = frame.getSymbolsInSignature();
		Assert.assertTrue(s.contains(name));
		Assert.assertTrue(s.contains(surname));
		Assert.assertTrue(s.contains(email));
		Assert.assertTrue(s.contains(emailDatatype));
		Assert.assertTrue(s.contains(someone));
	}

	@Test
	public void includes() throws FrameException {

		Assert.assertTrue(frame.includes(frame));

		StatementTemplateImpl st1 = new StatementTemplateImpl(null, name);
		StatementTemplateImpl st2 = new StatementTemplateImpl(null, surname);
		StatementTemplateImpl st3 = new StatementTemplateImpl(null, email);
		// st3.setDatatype(emailDatatype);

		Frame f1 = new FrameImpl(st1, st2);
		Assert.assertFalse(frame.includes(f1));

		Frame f2 = new FrameImpl(st1, st2, st3);
		Assert.assertFalse(frame.includes(f2));

		StatementTemplateImpl st4 = new StatementTemplateImpl(null, email,
				null, null, null, emailDatatype);
		Frame f3 = new FrameImpl(st1, st2, st4);
		Assert.assertTrue(frame.includes(f3));

		StatementTemplateImpl st5 = new StatementTemplateImpl(someone, email,
				null, null, null, emailDatatype);
		Frame f4 = new FrameImpl(st1, st2, st5);
		Assert.assertTrue(frame.includes(f4));
	}

	@Test
	public void noDuplicatedStatementTemplates() throws FrameException {
		StatementTemplate s = new StatementTemplateImpl();
		FrameImpl f = new FrameImpl(s, s, s);
		Assert.assertTrue(f.asSet().size() == 1);
	}

	@Test
	public void equals() throws FrameException {
		StatementTemplate s = new StatementTemplateImpl();
		FrameImpl f = new FrameImpl(s, s, s);
		Assert.assertTrue(f.equals(f));

		StatementTemplate s2 = new StatementTemplateImpl();
		FrameImpl f2 = new FrameImpl(s2);
		Assert.assertTrue(s.equals(s2)); // FIXME hashCodes are different!
		Assert.assertTrue(f2.equals(f));
	}
	
	@Test
	public void hascodeTest() throws FrameException{
		StatementTemplate s = new StatementTemplateImpl();
		FrameImpl f = new FrameImpl(s);
		Assert.assertTrue(f.equals(f));

		StatementTemplate s2 = new StatementTemplateImpl();
		FrameImpl f2 = new FrameImpl(s2);
		Assert.assertTrue(f.hashCode() == f2.hashCode()); 
	}
}
