package minsk.codeanalysis.binding;

public class BoundExpressionStatement extends BoundStatement {
	private final BoundExpression expression;
	
	public BoundExpressionStatement(BoundExpression expression) {
		this.expression = expression;
	}
	
	@Override
	public BoundNodeKind getKind() {
		return BoundNodeKind.ExpressionStatement;
	}

	public BoundExpression getExpression() {
		return expression;
	}
}
