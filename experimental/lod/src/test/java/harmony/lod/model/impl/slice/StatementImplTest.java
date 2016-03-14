package harmony.lod.model.impl.slice;

import harmony.lod.model.api.slice.Statement;
import harmony.lod.model.api.slice.StatementTemplate;
import harmony.lod.model.api.symbol.IRI;
import harmony.lod.model.impl.slice.StatementImpl;
import harmony.lod.model.impl.slice.StatementTemplateImpl;
import harmony.lod.model.impl.symbol.IRIImpl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StatementImplTest {

	private Statement st = null;
	private Statement st2 = null;
	private StatementTemplate stt = null;
	
	@Before
	public void setup() throws SliceException{
		IRI s = new IRIImpl("s");
		IRI p = new IRIImpl("p");
		IRI o = new IRIImpl("o");
		st = new StatementImpl(s, p, o);
		st2 = new StatementImpl(s, p, o);
		stt = new StatementTemplateImpl(s, p, o);
	}
	
	@Test
	public void equals(){
		Assert.assertTrue(st.equals(st2));
	}
	
	@Test
	public void equivalentStatementTemplate(){
		Assert.assertTrue(stt.equals(st));
	}
	
	@Test
	public void includes(){
		Assert.assertTrue(st.includes(st));
		Assert.assertTrue(st.includes(st2));
		Assert.assertTrue(st.includes(stt));
	}
	
	@Test
	public void isStatement(){
		Assert.assertTrue(st.isStatement());
		Assert.assertTrue(st2.isStatement());
		Assert.assertTrue(stt.isStatement());
	}
}
