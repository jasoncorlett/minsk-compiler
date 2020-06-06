package minsk.codeanalysis.syntax.parser;

import minsk.codeanalysis.Nested;
import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;

public class AssignmentExpressionSyntax extends ExpressionSyntax {
	private final SyntaxToken identifierToken;
	private final SyntaxToken equalsToken;
	private final ExpressionSyntax expression;

	public AssignmentExpressionSyntax(SyntaxToken identifierToken, SyntaxToken equalsToken, ExpressionSyntax expression) {
		this.identifierToken = identifierToken;
		this.equalsToken = equalsToken;
		this.expression = expression;
	}

	@Nested(1)
	public SyntaxToken getIdentifierToken() {
		return identifierToken;
	}

	@Nested(2)
	public SyntaxToken getEqualsToken() {
		return equalsToken;
	}

	@Nested(3)
	public ExpressionSyntax getExpression() {
		return expression;
	}

	@Override
	public SyntaxKind getKind() {
		return SyntaxKind.AssignmentExpression;
	}
	
}