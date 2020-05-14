package minsk.codeanalysis;

import java.util.List;

public class LiteralExpressionSyntax extends ExpressionSyntax {
	public final SyntaxToken literalToken;
	public LiteralExpressionSyntax(SyntaxToken literalToken) {
		super(SyntaxKind.NumberExpression);
		this.literalToken = literalToken;
	}
	
	@Override
	public Iterable<SyntaxNode> getChildren() {
		return List.of(literalToken);
	}
}