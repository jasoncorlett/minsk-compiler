package minsk.codeanalysis;

import java.util.List;

public class ParenthesizedExpressionSyntax extends ExpressionSyntax {
	private final SyntaxToken openParenToken;
	private final ExpressionSyntax expression;
	private final SyntaxToken closeParenToken;

	public ParenthesizedExpressionSyntax(SyntaxToken openParenToken, ExpressionSyntax expr, SyntaxToken closeParenToken) {
		super(SyntaxKind.ParenthesizedExpression);
		this.openParenToken = openParenToken;
		this.expression = expr;
		this.closeParenToken = closeParenToken;
	}

	public SyntaxToken getOpenParenToken() {
		return openParenToken;
	}

	public ExpressionSyntax getExpression() {
		return expression;
	}

	public SyntaxToken getCloseParenToken() {
		return closeParenToken;
	}

	@Override
	public Iterable<SyntaxNode> getChildren() {
		return List.of(getOpenParenToken(), getExpression(), getCloseParenToken());
	}
}