package harmony.lod.model.impl.slice;

import harmony.lod.model.api.slice.StatementTemplate;
import harmony.lod.model.api.symbol.Datatype;
import harmony.lod.model.api.symbol.IRI;
import harmony.lod.model.api.symbol.Lang;
import harmony.lod.model.api.symbol.Literal;
import harmony.lod.model.impl.slice.StatementTemplateImpl;
import harmony.lod.model.impl.symbol.DatatypeImpl;
import harmony.lod.model.impl.symbol.IRIImpl;
import harmony.lod.model.impl.symbol.LangImpl;
import harmony.lod.model.impl.symbol.LiteralImpl;

import org.junit.Assert;
import org.junit.Test;

public class StatementTemplateImplTest {

	@Test
	public void testSubject() {
		IRI iri = new IRIImpl("http://www.example.org/me");
		StatementTemplate st = new StatementTemplateImpl(iri);
		Assert.assertTrue(st.getSubject() != null);

		IRI subject = st.getSubject();
		Assert.assertTrue(subject.equals(iri));
	}

	@Test
	public void testPredicate() {
		IRI iri = new IRIImpl("http://www.example.org/predicate");

		StatementTemplate st = new StatementTemplateImpl(null, iri);
		Assert.assertTrue(st.getPredicate() != null);

		IRI predicate = st.getPredicate();
		Assert.assertTrue(predicate.equals(iri));
	}

	@Test
	public void testObject() {
		IRI iri = new IRIImpl("http://www.example.org/object");

		StatementTemplate st = new StatementTemplateImpl(null, null, iri);
		Assert.assertTrue(st.getObject() != null);

		IRI object = st.getObject();
		Assert.assertTrue(object.equals(iri));
	}

	@Test
	public void testValue() {
		Literal literal = new LiteralImpl("Ciao  mondo!");
		StatementTemplate st = new StatementTemplateImpl(null, null, literal);
		Assert.assertTrue(st.getValue() != null);

		Literal value = st.getValue();
		Assert.assertTrue(value.equals(literal));
	}

	@Test
	public void testLang() {
		Lang en = new LangImpl("en");
		StatementTemplate st = new StatementTemplateImpl(null, null, null,
				null, en, null);
		Assert.assertTrue(st.getLang() != null);

		Lang lang = st.getLang();
		Assert.assertTrue(en.equals(lang));
	}

	@Test
	public void testDatatype() {
		Datatype datatype = new DatatypeImpl("http://www.example.org/datatype");
		StatementTemplate st = new StatementTemplateImpl(null, null, datatype);
		Assert.assertTrue(st.getDatatype() != null);

		Datatype datatypeAdded = st.getDatatype();
		Assert.assertTrue(datatypeAdded.equals(datatype));
	}

	@Test
	public void equalsTest() {
		StatementTemplateImpl st = new StatementTemplateImpl();
		StatementTemplateImpl st2 = new StatementTemplateImpl();
		Assert.assertTrue(st2.equals(st));
	}
	
	@Test
	public void equalsThisTest() {
		StatementTemplateImpl st = new StatementTemplateImpl();
		Assert.assertTrue(st.equals(st));
	}

	@Test
	public void testGeneralIncludesGeneral() {
		StatementTemplate st = new StatementTemplateImpl();
		StatementTemplate general = new StatementTemplateImpl();
		Assert.assertTrue(general.includes(st));
	}

	@Test
	public void testIncludesItself() {
		StatementTemplate st = new StatementTemplateImpl();
		Assert.assertTrue(st.includes(st));
	}

	@Test
	public void testPIncludesSP() {
		/**
		 * a st defining only a predicate includes a st1 which defines the same
		 * predicate with a specified subject (and not the inverse)
		 */
		IRI predicate = new IRIImpl("http://www.example.org/predicate");

		StatementTemplate st = new StatementTemplateImpl(null, predicate);

		IRI subject = new IRIImpl("http://www.example.org/me");
		StatementTemplateImpl st2 = new StatementTemplateImpl(subject,
				predicate);

		Assert.assertTrue(st.includes(st2));
		Assert.assertFalse(st2.includes(st));
	}

	@Test
	public void testSPOIncludesSPO() {
		// a st defining all (spo) includes another (same spo)
		IRI subject = new IRIImpl("subject");
		IRI predicate = new IRIImpl("predicate");
		IRI object = new IRIImpl("object");
		StatementTemplate st = new StatementTemplateImpl(subject, predicate,
				object);

		StatementTemplateImpl st2 = new StatementTemplateImpl(new IRIImpl(
				"subject"), new IRIImpl("predicate"), new IRIImpl("object"));
		Assert.assertTrue(st.includes(st2));

		StatementTemplate st3 = new StatementTemplateImpl(null, predicate,
				object);
		Assert.assertTrue(st3.includes(st2));

		StatementTemplate st4 = new StatementTemplateImpl(subject, null, object);
		Assert.assertTrue(st4.includes(st2));

		StatementTemplate st5 = new StatementTemplateImpl(subject, predicate);
		Assert.assertTrue(st5.includes(st2));

		IRI another = new IRIImpl("another");

		StatementTemplate st6 = new StatementTemplateImpl(another, predicate,
				object);
		Assert.assertFalse(st6.includes(st2));
	}
}
