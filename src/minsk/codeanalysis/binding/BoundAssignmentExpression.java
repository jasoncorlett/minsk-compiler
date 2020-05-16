package minsk.codeanalysis.binding;

public class BoundAssignmentExpression extends BoundExpression {

	private final String name;
	private final BoundExpression expression;

	public BoundAssignmentExpression(String name, BoundExpression expression) {
		this.name = name;
		this.expression = expression;
	}
	
	@Override
	Class<? extends Object> getType() {
		return getExpression().getType();
	}

	@Override
	public BoundNodeKind getKind() {
		return BoundNodeKind.AssignmentExpression;
	}

	public String getName() {
		return name;
	}

	public BoundExpression getExpression() {
		return expression;
	}
}
