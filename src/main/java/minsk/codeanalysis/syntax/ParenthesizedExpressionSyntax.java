package minsk.codeanalysis.syntax;

public record ParenthesizedExpressionSyntax(
		@SyntaxChild(1) SyntaxToken openParenToken,
		@SyntaxChild(2) ExpressionSyntax expression,
		@SyntaxChild(3) SyntaxToken closeParenToken
	) implements ExpressionSyntax {

	@Override
	public SyntaxKind kind() {
		return SyntaxKind.ParenthesizedExpression;
	}
}