package minsk.codeanalysis.syntax.parser;

import minsk.codeanalysis.Nested;
import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;

public class ParenthesizedExpressionSyntax extends ExpressionSyntax {
	private final SyntaxToken openParenToken;
	private final ExpressionSyntax expression;
	private final SyntaxToken closeParenToken;

	public ParenthesizedExpressionSyntax(SyntaxToken openParenToken, ExpressionSyntax expr, SyntaxToken closeParenToken) {
		this.openParenToken = openParenToken;
		this.expression = expr;
		this.closeParenToken = closeParenToken;
	}

	@Nested(1)
	public SyntaxToken getOpenParenToken() {
		return openParenToken;
	}

	@Nested(2)
	public ExpressionSyntax getExpression() {
		return expression;
	}

	@Nested(3)
	public SyntaxToken getCloseParenToken() {
		return closeParenToken;
	}

	@Override
	public SyntaxKind getKind() {
		return SyntaxKind.ParenthesizedExpression;
	}
}