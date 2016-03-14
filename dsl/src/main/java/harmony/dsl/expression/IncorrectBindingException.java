package harmony.dsl.expression;

@SuppressWarnings("serial")
public class IncorrectBindingException extends Exception {

	public IncorrectBindingException() {
	}

	public IncorrectBindingException(Exception cause) {
		super(cause);
	}

	public IncorrectBindingException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncorrectBindingException(String message) {
		super(message);
	}

}
