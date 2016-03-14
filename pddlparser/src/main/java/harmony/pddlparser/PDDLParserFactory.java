package harmony.pddlparser;

import java.io.InputStream;

public class PDDLParserFactory {

	public static PDDLDomainParser getDomainParser(InputStream stream){
		return new PDDLParserImpl(stream);
	}
	

	public static PDDLProblemParser getProblemParser(InputStream stream){
		return new PDDLParserImpl(stream);
	}
	
	
}
