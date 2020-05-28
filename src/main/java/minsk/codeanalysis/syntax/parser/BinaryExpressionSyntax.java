package minsk.codeanalysis.syntax.parser;

import minsk.codeanalysis.syntax.SyntaxChild;
import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;

public class BinaryExpressionSyntax implements ExpressionSyntax {
	private final ExpressionSyntax left;
	private final ExpressionSyntax right;
	private final SyntaxToken operatorToken;
	
	public BinaryExpressionSyntax(ExpressionSyntax left, SyntaxToken operatorToken, ExpressionSyntax right) {
		this.operatorToken = operatorToken;
		this.left = left;
		this.right = right;
	}
	
	@SyntaxChild(order = 1)
	public ExpressionSyntax getLeft() {
		return left;
	}

	@SyntaxChild(order = 2)
	public SyntaxToken getOperatorToken() {
		return operatorToken;
	}

	@SyntaxChild(order = 3)
	public ExpressionSyntax getRight() {
		return right;
	}

	@Override
	public SyntaxKind getKind() {
		return SyntaxKind.BinaryExpression;
	}
}
