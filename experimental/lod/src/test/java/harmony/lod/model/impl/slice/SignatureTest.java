package harmony.lod.model.impl.slice;

import harmony.core.api.thing.Thing;
import harmony.lod.model.api.slice.Frame;
import harmony.lod.model.api.slice.Path;
import harmony.lod.model.api.slice.StatementTemplate;
import harmony.lod.model.impl.slice.FrameException;
import harmony.lod.model.impl.slice.FrameImpl;
import harmony.lod.model.impl.slice.PathException;
import harmony.lod.model.impl.slice.PathImpl;
import harmony.lod.model.impl.slice.StatementTemplateImpl;
import harmony.lod.model.impl.symbol.DatatypeImpl;
import harmony.lod.model.impl.symbol.IRIImpl;
import harmony.lod.model.impl.symbol.LangImpl;
import harmony.lod.model.impl.symbol.LiteralImpl;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignatureTest {

	final String EX = "http://www.example.org/";
	private Logger log = LoggerFactory.getLogger(getClass());
	private StatementTemplate st = null;
	private Path path = null;
	private Frame frame = null;

	private void logAndTest(Thing slice, String expected) {
		log.info(slice.getSignature());
		if (!expected.equals(slice.getSignature())) {
			log.error("Expected: " + expected);
		}
		Assert.assertTrue(expected.equals(slice.getSignature()));

	}

	@Test
	public void statementToString() {
		st = new StatementTemplateImpl(new IRIImpl(EX + "me"), new IRIImpl(EX
				+ "name"));
		logAndTest(st,
				"(Statement <http://www.example.org/me> <http://www.example.org/name> ?)");
	}

	@Test
	public void statementToString2() {
		st = new StatementTemplateImpl();
		logAndTest(st, "(Statement ? ? ?)");
	}

	@Test
	public void statementToString3() {
		st = new StatementTemplateImpl(new IRIImpl(EX + "me"), new IRIImpl(EX
				+ "friend"), new IRIImpl(EX + "you"));
		logAndTest(st, "(Statement <http://www.example.org/me> "
				+ "<http://www.example.org/friend> "
				+ "<http://www.example.org/you>)");
	}

	@Test
	public void statementToString4() {
		st = new StatementTemplateImpl(null, null, new LangImpl("it"));
		logAndTest(st, "(Statement ? ? ?@it)");
	}

	@Test
	public void statementToString5() {
		st = new StatementTemplateImpl(null, null, new LiteralImpl("MyValue",
				new LangImpl("en")));
		logAndTest(st, "(Statement ? ? \"MyValue\"@en)");
	}

	@Test
	public void statementToString6() {
		st = new StatementTemplateImpl(null, null, new DatatypeImpl(EX
				+ "emailType"));
		logAndTest(st, "(Statement ? ? ?^^<http://www.example.org/emailType>)");
	}

	@Test
	public void statementToString7() {
		st = new StatementTemplateImpl(null, null, new LiteralImpl(
				"MyValue and \"your Value\""));
		logAndTest(st, "(Statement ? ? \"\"\"MyValue and \"your Value\"\"\"\")");
	}

	@Test
	public void statementToString8() {
		st = new StatementTemplateImpl(null, null, new LiteralImpl("MyValue"));
		logAndTest(st, "(Statement ? ? \"MyValue\")");
	}

	@Test
	public void statementToString9() {
		st = new StatementTemplateImpl(new IRIImpl(EX + "me"), null,
				new LangImpl("it"));
		logAndTest(st, "(Statement <http://www.example.org/me> ? ?@it)");
	}

	@Test
	public void statementToString10() {
		st = new StatementTemplateImpl(null, new IRIImpl(EX + "predicate"),
				new DatatypeImpl(EX + "emailType"));
		logAndTest(
				st,
				"(Statement ? <http://www.example.org/predicate> ?^^<http://www.example.org/emailType>)");
	}

	@Test
	public void pathToString() throws PathException {
		path = new PathImpl(new StatementTemplateImpl(null, new IRIImpl(
				"friend")), new StatementTemplateImpl(null,
				new IRIImpl("enemy")), new StatementTemplateImpl(null,
				new IRIImpl("friend")));
		logAndTest(
				path,
				"(Path (Statement ? <friend> ?) (Statement ? <enemy> ?) (Statement ? <friend> ?))");
	}

	@Test
	public void pathToString2() throws PathException {
		path = new PathImpl(new StatementTemplateImpl(null, new IRIImpl(
				"friend")));
		logAndTest(path, "(Path (Statement ? <friend> ?))");
	}

	@Test
	public void pathToString3() throws PathException {
		path = new PathImpl(new StatementTemplateImpl());
		logAndTest(path, "(Path (Statement ? ? ?))");
	}

	@Test
	public void pathToString4() throws PathException {
		path = new PathImpl();
		logAndTest(path, "(Path)");
	}

	@Test
	public void frameToString() throws FrameException {
		frame = new FrameImpl(new StatementTemplateImpl[0]);
		logAndTest(frame, "(Frame)");
	}

	@Test
	public void frameToString2() throws FrameException {
		frame = new FrameImpl(new StatementTemplateImpl(),
				new StatementTemplateImpl(), new StatementTemplateImpl(
						new IRIImpl(EX + "me")), new StatementTemplateImpl(
						null, null, new DatatypeImpl(EX + "datatype")));
		logAndTest(frame, "(Frame (Statement <http://www.example.org/me> ? ?^^<http://www.example.org/datatype>))");
	}

	@Test
	public void frameToString3() throws FrameException {
		frame = new FrameImpl(
				new StatementTemplateImpl(new IRIImpl(EX + "me")),
				new StatementTemplateImpl(null, new IRIImpl(EX + "name")),
				new StatementTemplateImpl(null, new IRIImpl(EX + "surname")),
				new StatementTemplateImpl(null, null, new DatatypeImpl(EX
						+ "email")));
		logAndTest(frame, "(Frame " +
				"(Statement <http://www.example.org/me> <http://www.example.org/name> ?) " +
				"(Statement <http://www.example.org/me> <http://www.example.org/surname> ?) "
				+ "(Statement <http://www.example.org/me> ? ?^^<http://www.example.org/email>))");
	}
	
	@Test
	public void frameToString4() throws FrameException{
		frame = new FrameImpl(
				new StatementTemplateImpl(null, new IRIImpl("name")),
				new StatementTemplateImpl(null, new IRIImpl("surname")),
				new StatementTemplateImpl(null, new IRIImpl("email")),
				new StatementTemplateImpl(null, new IRIImpl("address")),
				new StatementTemplateImpl(null, new IRIImpl("phone"))
				);
		logAndTest(frame,"(Frame" +
				" (Statement ? <address> ?)" +
				" (Statement ? <email> ?)" +
				" (Statement ? <name> ?)" +
				" (Statement ? <phone> ?)" +
				" (Statement ? <surname> ?))");
	}

	@Test
	public void frameToString5() throws FrameException{
		frame = new FrameImpl(
				new StatementTemplateImpl(new IRIImpl("bob"), new IRIImpl("name")),
				new StatementTemplateImpl(null, new IRIImpl("surname")),
				new StatementTemplateImpl(null, new IRIImpl("email")),
				new StatementTemplateImpl(null, new IRIImpl("address")),
				new StatementTemplateImpl(null, new IRIImpl("phone"))
				);
		logAndTest(frame,"(Frame" +
				" (Statement <bob> <address> ?)" +
				" (Statement <bob> <email> ?)" +
				" (Statement <bob> <name> ?)" +
				" (Statement <bob> <phone> ?)" +
				" (Statement <bob> <surname> ?))");
	}

	@Test
	public void frameToString6() throws FrameException{
		frame = new FrameImpl(
				new StatementTemplateImpl(new IRIImpl("bob"), new IRIImpl("name")),
				new StatementTemplateImpl(null, new IRIImpl("surname")),
				new StatementTemplateImpl(null, new IRIImpl("email"), new DatatypeImpl("emailDatatype")),
				new StatementTemplateImpl(null, new IRIImpl("address")),
				new StatementTemplateImpl(null, new IRIImpl("phone"))
				);
		logAndTest(frame,"(Frame" +
				" (Statement <bob> <address> ?)" +
				" (Statement <bob> <email> ?^^<emailDatatype>)" +
				" (Statement <bob> <name> ?)" +
				" (Statement <bob> <phone> ?)" +
				" (Statement <bob> <surname> ?))");
	}

	@Test
	public void frameToString7() throws FrameException{
		frame = new FrameImpl(
				new StatementTemplateImpl(new IRIImpl("bob"), new IRIImpl("name")),
				new StatementTemplateImpl(null, new IRIImpl("surname")),
				new StatementTemplateImpl(null, new IRIImpl("email"), new DatatypeImpl("emailDatatype")),
				new StatementTemplateImpl(null, new IRIImpl("address")),
				new StatementTemplateImpl(null, new IRIImpl("phone")),
				new StatementTemplateImpl(null, new IRIImpl("phone")), // This is ignored
				new StatementTemplateImpl() // This is ignored
				);
		logAndTest(frame,"(Frame" +
				" (Statement <bob> <address> ?)" +
				" (Statement <bob> <email> ?^^<emailDatatype>)" +
				" (Statement <bob> <name> ?)" +
				" (Statement <bob> <phone> ?)" +
				" (Statement <bob> <surname> ?))");
	}

	@Test
	public void frameToString8() throws FrameException{
		frame = new FrameImpl(
				new StatementTemplateImpl(null, new IRIImpl("name")),
				new StatementTemplateImpl(null, new IRIImpl("surname")),
				new StatementTemplateImpl(null, new IRIImpl("email"), new DatatypeImpl("emailDatatype")),
				new StatementTemplateImpl(null, new IRIImpl("address")), // Overwritten by the next
				new StatementTemplateImpl(null, new IRIImpl("address"), new DatatypeImpl("addressDatatype")),
				new StatementTemplateImpl(new IRIImpl("bob"), new IRIImpl("phone")),
				new StatementTemplateImpl(null, new IRIImpl("phone")), // This is ignored
				new StatementTemplateImpl() // This is ignored
				);
		logAndTest(frame,"(Frame" +
				" (Statement <bob> <address> ?^^<addressDatatype>)" +
				" (Statement <bob> <email> ?^^<emailDatatype>)" +
				" (Statement <bob> <name> ?)" +
				" (Statement <bob> <phone> ?)" +
				" (Statement <bob> <surname> ?))");
	}
	
}
