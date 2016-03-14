package harmony.core.api.operator;

@SuppressWarnings("serial")
public class OperatorException extends Exception {

	private Operator operator;

	public OperatorException(Operator operator, String string) {
		super(string);
		setOperator(operator);
	}

	public OperatorException(Operator operator, String string, Throwable cause) {
		super(string, cause);
		setOperator(operator);
	}

	public OperatorException(Operator operator, Throwable cause) {
		super(cause);
		setOperator(operator);
	}

	public Operator getOperator() {
		return operator;
	}

	protected void setOperator(Operator operator) {
		this.operator = operator;
	}
}
