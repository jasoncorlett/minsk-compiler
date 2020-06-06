package minsk.codeanalysis.syntax.parser;

import minsk.codeanalysis.Nested;
import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;

public class BinaryExpressionSyntax extends ExpressionSyntax {
	private final ExpressionSyntax left;
	private final ExpressionSyntax right;
	private final SyntaxToken operatorToken;
	
	public BinaryExpressionSyntax(ExpressionSyntax left, SyntaxToken operatorToken, ExpressionSyntax right) {
		this.operatorToken = operatorToken;
		this.left = left;
		this.right = right;
	}
	
	@Nested(1)
	public ExpressionSyntax getLeft() {
		return left;
	}

	@Nested(2)
	public SyntaxToken getOperatorToken() {
		return operatorToken;
	}

	@Nested(3)
	public ExpressionSyntax getRight() {
		return right;
	}

	@Override
	public SyntaxKind getKind() {
		return SyntaxKind.BinaryExpression;
	}
}
