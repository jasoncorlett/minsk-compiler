package minsk.codeanalysis;

import java.util.List;

public class ParenthesizedExpressionSyntax extends ExpressionSyntax {
	public final SyntaxToken openParenToken;
	public final ExpressionSyntax expression;
	public final SyntaxToken closeParenToken;

	public ParenthesizedExpressionSyntax(SyntaxToken openParenToken, ExpressionSyntax expr, SyntaxToken closeParenToken) {
		super(SyntaxKind.ParenthesizedExpression);
		this.openParenToken = openParenToken;
		this.expression = expr;
		this.closeParenToken = closeParenToken;
	}

	@Override
	public Iterable<SyntaxNode> getChildren() {
		return List.of(openParenToken, expression, closeParenToken);
	}
}