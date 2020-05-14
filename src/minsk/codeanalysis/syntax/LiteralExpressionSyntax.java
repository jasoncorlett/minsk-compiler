package minsk.codeanalysis.syntax;

import java.util.List;

public class LiteralExpressionSyntax extends ExpressionSyntax {
	private final SyntaxToken literalToken;
	private final Object value;
	
	public LiteralExpressionSyntax(SyntaxToken literalToken) {
		this(literalToken, literalToken.getValue());
	}
	
	public LiteralExpressionSyntax(SyntaxToken literalToken, Object value) {
		super(SyntaxKind.LiteralExpression);
		this.literalToken = literalToken;
		this.value = value;
	}
	
	public SyntaxToken getLiteralToken() {
		return literalToken;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public Iterable<SyntaxNode> getChildren() {
		return List.of(getLiteralToken());
	}
}