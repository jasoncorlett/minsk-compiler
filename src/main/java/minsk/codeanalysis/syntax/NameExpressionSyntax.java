package minsk.codeanalysis.syntax;

public class NameExpressionSyntax implements ExpressionSyntax {
	private final SyntaxToken identifierToken;

	public NameExpressionSyntax(SyntaxToken identifierToken) {
		this.identifierToken = identifierToken;
	}

	@SyntaxChild
	public SyntaxToken getIdentifierToken() {
		return identifierToken;
	}
	
	@Override
	public SyntaxKind getKind() {
		return SyntaxKind.NameExpression;
	}
}