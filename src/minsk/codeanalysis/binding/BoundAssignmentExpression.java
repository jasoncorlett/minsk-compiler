package minsk.codeanalysis.binding;

public class BoundAssignmentExpression extends BoundExpression {

	private final VariableSymbol variable;
	private final BoundExpression expression;

	public BoundAssignmentExpression(VariableSymbol variable, BoundExpression expression) {
		this.variable = variable;
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

	public VariableSymbol getVariable() {
		return variable;
	}

	public BoundExpression getExpression() {
		return expression;
	}
}
