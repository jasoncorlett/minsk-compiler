package minsk.codeanalysis.binding;

public final class BoundLiteralExpression extends BoundExpression {
	private final Object value;

	public BoundLiteralExpression(Object value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public final Class<?> getType() {
		if (getValue() == null) {
			return null;
		}
		return getValue().getClass();
	}

	@Override
	public final BoundNodeKind getKind() {
		return BoundNodeKind.LiteralExpression;
	}
	
	@Override
	public String toString() {
		return "BoundLiteralExpression [ " + value + " ]";
	}
}