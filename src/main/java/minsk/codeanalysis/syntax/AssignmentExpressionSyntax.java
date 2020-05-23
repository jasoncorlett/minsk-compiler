package minsk.codeanalysis.syntax;

public class AssignmentExpressionSyntax implements ExpressionSyntax {
	private final SyntaxToken identifierToken;
	private final SyntaxToken equalsToken;
	private final ExpressionSyntax expression;

	public AssignmentExpressionSyntax(SyntaxToken identifierToken, SyntaxToken equalsToken, ExpressionSyntax expression) {
		this.identifierToken = identifierToken;
		this.equalsToken = equalsToken;
		this.expression = expression;
	}

	@SyntaxChild(order = 1)
	public SyntaxToken getIdentifierToken() {
		return identifierToken;
	}

	@SyntaxChild(order = 2)
	public SyntaxToken getEqualsToken() {
		return equalsToken;
	}

	@SyntaxChild(order = 3)
	public ExpressionSyntax getExpression() {
		return expression;
	}

	@Override
	public SyntaxKind getKind() {
		return SyntaxKind.AssignmentExpression;
	}
	
}