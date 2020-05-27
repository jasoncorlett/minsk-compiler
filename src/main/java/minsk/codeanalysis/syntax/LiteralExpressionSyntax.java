package minsk.codeanalysis.syntax;

public class LiteralExpressionSyntax implements ExpressionSyntax {
	private final SyntaxToken literalToken;
	private final Object value;
	
	public LiteralExpressionSyntax(SyntaxToken literalToken) {
		this(literalToken, literalToken.value());
	}
	
	public LiteralExpressionSyntax(SyntaxToken literalToken, Object value) {
		this.literalToken = literalToken;
		this.value = value;
	}
	
	@SyntaxChild
	public SyntaxToken getLiteralToken() {
		return literalToken;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public SyntaxKind kind() {
		return SyntaxKind.LiteralExpression;
	}
}