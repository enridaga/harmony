package harmony.dsl.expression;

import org.apache.commons.lang3.builder.ToStringStyle;

public class ExpressionToStringStyle extends ToStringStyle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static ToStringStyle STYLE = new ExpressionToStringStyle();

	public ExpressionToStringStyle() {
		setFieldNameValueSeparator(": ");
		setFieldSeparator(" ");
		setContentEnd("");setContentStart("");
		setUseShortClassName(true);
		setUseIdentityHashCode(false);
		setUseFieldNames(false);
	}

	@Override
	protected void appendClassName(StringBuffer buffer, Object object) {
		// Remove the Expression postfix
		buffer.append(object.getClass().getSimpleName()
				.replace("Expression", "")).append(" ");
	}

	@Override
	public void appendStart(StringBuffer buffer, Object object) {
		buffer.append("(");
		super.appendStart(buffer, object);
	}

	@Override
	public void appendEnd(StringBuffer buffer, Object object) {
		buffer.append(")");
		super.appendEnd(buffer, object);
	}
	
	
}
