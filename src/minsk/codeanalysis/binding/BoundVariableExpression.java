package minsk.codeanalysis.binding;

public class BoundVariableExpression extends BoundExpression {
	private final String name;
	private final Class<?> type;

	public BoundVariableExpression(String name, Class<?> type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	@Override
	Class<? extends Object> getType() {
		return type;
	}

	@Override
	public BoundNodeKind getKind() {
		return BoundNodeKind.VariableExpression;
	}
}
