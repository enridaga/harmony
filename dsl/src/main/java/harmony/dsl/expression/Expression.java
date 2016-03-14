package harmony.dsl.expression;


public interface Expression<T extends Object> {

	public abstract Declarations declaresVariables();

	/**
	 * 
	 * @param scope
	 * @return
	 * @throws UnboundVariableException
	 * @throws IncorrectBindingException
	 */
	public abstract T eval(Scope scope) throws UnboundVariableException,
			IncorrectBindingException;

}
