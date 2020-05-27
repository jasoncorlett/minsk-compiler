package minsk.codeanalysis.syntax;

public record UnaryExpressionSyntax(
		@SyntaxChild(1) SyntaxToken operatorToken,
		@SyntaxChild(2)ExpressionSyntax operand
	) implements ExpressionSyntax {
	
	@Override
	public SyntaxKind kind() {
		return SyntaxKind.UnaryExpression;
	}
}