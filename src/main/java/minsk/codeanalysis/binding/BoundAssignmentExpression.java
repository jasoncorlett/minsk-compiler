package minsk.codeanalysis.binding;

import minsk.codeanalysis.Nested;

public class BoundAssignmentExpression extends BoundExpression {

	private final VariableSymbol variable;
	private final BoundExpression expression;

	public BoundAssignmentExpression(VariableSymbol variable, BoundExpression expression) {
		this.variable = variable;
		this.expression = expression;
	}
	
	@Override
	public Class<?> getType() {
		return getExpression().getType();
	}

	@Override
	public BoundNodeKind getKind() {
		return BoundNodeKind.AssignmentExpression;
	}

	@Nested(1)
	public VariableSymbol getVariable() {
		return variable;
	}

	@Nested(2)
	public BoundExpression getExpression() {
		return expression;
	}
	
	@Override
	public String toString() {
		return "AssignmentExpression [ " + getVariable().getName() + " ]";
	}
}
