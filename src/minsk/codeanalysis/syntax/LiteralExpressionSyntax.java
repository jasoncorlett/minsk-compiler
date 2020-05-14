package minsk.codeanalysis.syntax;

import java.util.List;

public class LiteralExpressionSyntax extends ExpressionSyntax {
	private final SyntaxToken literalToken;
	
	public LiteralExpressionSyntax(SyntaxToken literalToken) {
		super(SyntaxKind.NumberExpression);
		this.literalToken = literalToken;
	}
	
	public SyntaxToken getLiteralToken() {
		return literalToken;
	}

	@Override
	public Iterable<SyntaxNode> getChildren() {
		return List.of(getLiteralToken());
	}
}