package minsk.codeanalysis.syntax;

public class UnaryExpressionSyntax implements ExpressionSyntax {
	private final ExpressionSyntax operand;
	private final SyntaxToken operatorToken;
	
	public UnaryExpressionSyntax(SyntaxToken operatorToken, ExpressionSyntax operand) {
 		this.operatorToken = operatorToken;
		this.operand = operand;
	}
	
	@SyntaxChild(order = 2)
	public ExpressionSyntax getOperand() {
		return operand;
	}

	@SyntaxChild(order = 1)
	public SyntaxToken getOperatorToken() {
		return operatorToken;
	}
	
	@Override
	public SyntaxKind getKind() {
		return SyntaxKind.UnaryExpression;
	}
}