package minsk.codeanalysis.syntax;

public record AssignmentExpressionSyntax(
		@SyntaxChild(1) SyntaxToken identifierToken,
		@SyntaxChild(2) SyntaxToken equalsToken,
		@SyntaxChild(3) ExpressionSyntax expression
	) implements ExpressionSyntax {

	@Override
	public SyntaxKind kind() {
		return SyntaxKind.AssignmentExpression;
	}
	
}