package harmony.lod.model.impl.slice;

import harmony.lod.model.impl.symbol.IRIImpl;
import harmony.lod.model.impl.symbol.LiteralImpl;

import org.junit.Assert;
import org.junit.Test;

public class PathImplTest {

	@Test
	public void constructor() {
		PathImpl p = new PathImpl();
		Assert.assertTrue(p.asList().isEmpty());
	}

	@Test
	public void constructor2() throws PathException {
		StatementTemplateImpl step1 = new StatementTemplateImpl(null,
				new IRIImpl("predicate1"));
		StatementTemplateImpl step2 = new StatementTemplateImpl(null,
				new IRIImpl("predicate2"));
		StatementTemplateImpl step3 = new StatementTemplateImpl(null,
				new IRIImpl("predicate3"));
		PathImpl p = new PathImpl(step1, step2, step3);

		Assert.assertFalse(p.asList().isEmpty());
		Assert.assertTrue(p.asList().size() == 3);
		Assert.assertTrue(p.getLength() == 3);

	}

	@Test
	public void incompatibleSubjectForLastObject() {
		StatementTemplateImpl step1 = new StatementTemplateImpl(new IRIImpl(
				"subject"), new IRIImpl("predicate1"));
		StatementTemplateImpl step2a = new StatementTemplateImpl(null,
				new IRIImpl("predicate2"));
		StatementTemplateImpl step2b = new StatementTemplateImpl(null,
				new IRIImpl("predicate2"), new IRIImpl("subject3"));

		StatementTemplateImpl step3 = new StatementTemplateImpl(new IRIImpl(
				"subject3"), new IRIImpl("predicate3"));

		PathException e = null;
		try {
			// This should NOT be valid
			new PathImpl(step1, step2a, step3);
		} catch (PathException ex) {
			// ex.printStackTrace();
			e = ex;
		}
		Assert.assertTrue(e != null);

		e = null;
		try {
			// This should be valid
			new PathImpl(step1, step2b, step3);
		} catch (PathException ex) {
			e = ex;
		}
		Assert.assertTrue(e == null);
	}

	@Test
	public void cannotAddStepOnClosedPath() {
		StatementTemplateImpl step1 = new StatementTemplateImpl(new IRIImpl(
				"subject"), new IRIImpl("predicate1"));
		StatementTemplateImpl step2 = new StatementTemplateImpl(null,
				new IRIImpl("predicate2"), new LiteralImpl("Ciao mondo"));
		StatementTemplateImpl step3 = new StatementTemplateImpl();

		PathException e = null;
		try {
			// This should NOT be valid
			new PathImpl(step1, step2, step3);
		} catch (PathException ex) {
			// ex.printStackTrace();
			e = ex;
		}
		Assert.assertTrue(e != null);
	}

	@Test
	public void lastObjectDoesntMatchGivenSubject() {
		StatementTemplateImpl step1 = new StatementTemplateImpl(null,
				new IRIImpl("predicate1"));

		StatementTemplateImpl step2 = new StatementTemplateImpl(null,
				new IRIImpl("predicate2"), new IRIImpl("object"));

		StatementTemplateImpl step3 = new StatementTemplateImpl(new IRIImpl(
				"notAsObject"));

		PathException e = null;
		try {
			// This should NOT be valid
			new PathImpl(step1, step2, step3);
		} catch (PathException ex) {
			// ex.printStackTrace();
			e = ex;
		}
		Assert.assertTrue(e != null);
	}

	@Test
	public void asList() throws PathException {

		StatementTemplateImpl step1 = new StatementTemplateImpl(null,
				new IRIImpl("predicate1"));
		StatementTemplateImpl step2 = new StatementTemplateImpl(null,
				new IRIImpl("predicate2"));
		StatementTemplateImpl step3 = new StatementTemplateImpl(null,
				new IRIImpl("predicate3"));

		PathImpl p = new PathImpl(step1, step2, step3);

		Assert.assertTrue(p.asList().contains(step1));
		Assert.assertTrue(p.asList().contains(step2));
		Assert.assertTrue(p.asList().contains(step3));

		Assert.assertTrue(p.asList().indexOf(step1) == 0);
		Assert.assertTrue(p.asList().indexOf(step2) == 1);
		Assert.assertTrue(p.asList().indexOf(step3) == 2);
	}

	@Test
	public void repeatedStatementTemplate() throws PathException {
		StatementTemplateImpl step = new StatementTemplateImpl(null,
				new IRIImpl("predicate"));

		PathImpl p = new PathImpl(step, step, step, step);

		Assert.assertTrue(p.asList().contains(step));
		Assert.assertTrue(p.getLength() == 4);
		Assert.assertTrue(p.asList().size() == 4);

		// indexOf will return the first occurrence
		Assert.assertTrue(p.asList().indexOf(step) == 0);

	}
	
	@Test
	public void equalsThisTest() throws PathException{
		PathImpl path = null;
		StatementTemplateImpl father = new StatementTemplateImpl(null,
				new IRIImpl("hasFather"));
		StatementTemplateImpl brother = new StatementTemplateImpl(null,
				new IRIImpl("hasBrother"));
		path = new PathImpl(father, brother);
		
		Assert.assertTrue(path.equals(path));
	}
	
	@Test
	public void equalsTest() throws PathException{
		PathImpl path = null;
		StatementTemplateImpl father = new StatementTemplateImpl(null,
				new IRIImpl("hasFather"));
		StatementTemplateImpl brother = new StatementTemplateImpl(null,
				new IRIImpl("hasBrother"));
		path = new PathImpl(father, brother);
		
		PathImpl path2 = null;
		StatementTemplateImpl father2 = new StatementTemplateImpl(null,
				new IRIImpl("hasFather"));
		StatementTemplateImpl brother2 = new StatementTemplateImpl(null,
				new IRIImpl("hasBrother"));
		path2 = new PathImpl(father2, brother2);
		

		Assert.assertTrue(path.equals(path2));
		Assert.assertTrue(path2.equals(path));
	}
}
