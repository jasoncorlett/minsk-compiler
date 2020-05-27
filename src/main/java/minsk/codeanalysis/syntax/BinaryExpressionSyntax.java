package minsk.codeanalysis.syntax;

public record BinaryExpressionSyntax(
		@SyntaxChild(1) ExpressionSyntax left,
		@SyntaxChild(2) SyntaxToken operatorToken,
		@SyntaxChild(3) ExpressionSyntax right
	) implements ExpressionSyntax {
	
	@Override
	public SyntaxKind kind() {
		return SyntaxKind.BinaryExpression;
	}
}
