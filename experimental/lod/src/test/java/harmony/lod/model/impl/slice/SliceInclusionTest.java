package harmony.lod.model.impl.slice;

import harmony.lod.model.api.slice.Statement;
import harmony.lod.model.api.symbol.Datatype;
import harmony.lod.model.api.symbol.IRI;
import harmony.lod.model.api.symbol.Lang;
import harmony.lod.model.api.symbol.Literal;
import harmony.lod.model.impl.slice.ChainImpl;
import harmony.lod.model.impl.slice.PathException;
import harmony.lod.model.impl.slice.PathImpl;
import harmony.lod.model.impl.slice.StatementImpl;
import harmony.lod.model.impl.slice.StatementTemplateImpl;
import harmony.lod.model.impl.symbol.DatatypeImpl;
import harmony.lod.model.impl.symbol.IRIImpl;
import harmony.lod.model.impl.symbol.LangImpl;
import harmony.lod.model.impl.symbol.LiteralImpl;
import junit.framework.Assert;

import org.junit.Test;

public class SliceInclusionTest {

	@Test
	public void aStatementTemplateIncludesItself() {
		IRI s = new IRIImpl("s");
		IRI p = new IRIImpl("p");
		IRI o = new IRIImpl("o");
		Literal v = new LiteralImpl("a value");
		Datatype d = new DatatypeImpl("aDatatype");
		Lang it = new LangImpl("it");

		StatementTemplateImpl st = new StatementTemplateImpl();
		Assert.assertTrue(st.includes(st));

		StatementTemplateImpl st1 = new StatementTemplateImpl(s);
		Assert.assertTrue(st1.includes(st1));

		StatementTemplateImpl st2 = new StatementTemplateImpl(null, p);
		Assert.assertTrue(st2.includes(st2));

		StatementTemplateImpl st3 = new StatementTemplateImpl(null, null, o);
		Assert.assertTrue(st3.includes(st3));

		StatementTemplateImpl st4 = new StatementTemplateImpl(null, null, v);
		Assert.assertTrue(st4.includes(st4));

		StatementTemplateImpl st5 = new StatementTemplateImpl(null, null, d);
		Assert.assertTrue(st5.includes(st5));

		StatementTemplateImpl st6 = new StatementTemplateImpl(null, null, it);
		Assert.assertTrue(st6.includes(st6));

	}

	@Test
	public void aStatementTemplateIncludingAnother() {
		StatementTemplateImpl st = new StatementTemplateImpl(new IRIImpl(
				"aSubject"), new IRIImpl("aPredicate"));
		StatementTemplateImpl st2 = new StatementTemplateImpl(new IRIImpl(
				"aSubject"), new IRIImpl("aPredicate"),
				new DatatypeImpl("aDatatype"));

		Assert.assertTrue(st.includes(st2));
		Assert.assertFalse(st2.includes(st));
	}

	@Test
	public void aStatementTemplateWhichIncludesAStatement() throws SliceException {
		StatementTemplateImpl st = new StatementTemplateImpl(null, new IRIImpl(
				"aPredicate"));
		Statement s = new StatementImpl(new IRIImpl("aSubject"), new IRIImpl(
				"aPredicate"), new IRIImpl("aObject"));
		Assert.assertTrue(st.includes(s));
		Assert.assertFalse(s.includes(st));

		Statement s2 = new StatementImpl(new IRIImpl("aSubject"), new IRIImpl(
				"aPredicate2"), new IRIImpl("aObject"));
		Assert.assertFalse(st.includes(s2));
	}

	@Test
	public void aPathIncludesItself() throws PathException {
		StatementTemplateImpl st1 = new StatementTemplateImpl(null,
				new IRIImpl("worksWith"));
		StatementTemplateImpl st2 = new StatementTemplateImpl(null,
				new IRIImpl("friend"));
		StatementTemplateImpl st3 = new StatementTemplateImpl(null,
				new IRIImpl("friend"));
		PathImpl path = new PathImpl(st1, st2, st3);

		Assert.assertTrue(path.includes(path));
	}

	@Test
	public void aPathDoesNotIncludeAnyOfItsStatementTemplates()
			throws PathException {
		StatementTemplateImpl st1 = new StatementTemplateImpl(null,
				new IRIImpl("worksWith"));
		StatementTemplateImpl st2 = new StatementTemplateImpl(null,
				new IRIImpl("friend"));
		StatementTemplateImpl st3 = new StatementTemplateImpl(null,
				new IRIImpl("friend"));
		PathImpl path = new PathImpl(st1, st2, st3);

		Assert.assertFalse(path.includes(st1));
		Assert.assertFalse(path.includes(st2));
		Assert.assertFalse(path.includes(st3));
	}

	@Test
	public void aPathDoesNotIncludeAnyOfItsSubPathsAndViceversa()
			throws PathException {
		StatementTemplateImpl st1 = new StatementTemplateImpl(null,
				new IRIImpl("worksWith"));
		StatementTemplateImpl st2 = new StatementTemplateImpl(null,
				new IRIImpl("friend"));
		StatementTemplateImpl st3 = new StatementTemplateImpl(null,
				new IRIImpl("friend"));
		PathImpl path = new PathImpl(st1, st2, st3);

		PathImpl subPath = new PathImpl(st1, st2);

		Assert.assertFalse(path.includes(subPath));
		Assert.assertFalse(subPath.includes(path));
	}

	@Test
	public void aPathIncludingAnotherPath() throws PathException {
		StatementTemplateImpl st1 = new StatementTemplateImpl(null,
				new IRIImpl("worksWith"));
		StatementTemplateImpl st2 = new StatementTemplateImpl(null,
				new IRIImpl("friend"));
		StatementTemplateImpl st3 = new StatementTemplateImpl(null,
				new IRIImpl("friend"));
		PathImpl path = new PathImpl(st1, st2, st3);

		StatementTemplateImpl st4 = new StatementTemplateImpl(null,
				new IRIImpl("friend"), new DatatypeImpl(
						"emailDatatype"));
		PathImpl path2 = new PathImpl(st1, st2, st4);

		Assert.assertTrue(path.includes(path2));
		Assert.assertTrue(st3.includes(st4));
		Assert.assertFalse(st4.includes(st3));
		Assert.assertFalse(path2.includes(path));
	}

	@Test
	public void aPathIncludingAChain() throws PathException {
		StatementTemplateImpl st1 = new StatementTemplateImpl(null,
				new IRIImpl("worksWith"));
		StatementTemplateImpl st2 = new StatementTemplateImpl(null,
				new IRIImpl("friend"));
		StatementTemplateImpl st3 = new StatementTemplateImpl(null,
				new IRIImpl("friend"));
		PathImpl path = new PathImpl(st1, st2, st3);

		StatementTemplateImpl cst1 = new StatementTemplateImpl(new IRIImpl(
				"enrico"), new IRIImpl("worksWith"), new IRIImpl("andrea"));
		StatementTemplateImpl cst2 = new StatementTemplateImpl(new IRIImpl(
				"andrea"), new IRIImpl("friend"), new IRIImpl("gianluca"));
		StatementTemplateImpl cst3 = new StatementTemplateImpl(new IRIImpl(
				"gianluca"), new IRIImpl("friend"), new IRIImpl("alberto"));
		ChainImpl chain = new ChainImpl(cst1, cst2, cst3);

		Assert.assertTrue(path.includes(chain));
	}
}
