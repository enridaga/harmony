package harmony.lod.model.impl.slice;

@SuppressWarnings("serial")
public class SliceException extends Exception{

	final static String DEFAULT_MESSAGE = "A problem occured on st modification";

	public SliceException() {
		super(DEFAULT_MESSAGE);
	}

	public SliceException(String message) {
		super(DEFAULT_MESSAGE + ": " + message);
	}
}
