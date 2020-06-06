package minsk.codeanalysis.binding;

import minsk.codeanalysis.Nested;

public class BoundExpressionStatement extends BoundStatement {
	private final BoundExpression expression;
	
	public BoundExpressionStatement(BoundExpression expression) {
		this.expression = expression;
	}
	
	@Override
	public BoundNodeKind getKind() {
		return BoundNodeKind.ExpressionStatement;
	}

	@Nested
	public BoundExpression getExpression() {
		return expression;
	}
}
