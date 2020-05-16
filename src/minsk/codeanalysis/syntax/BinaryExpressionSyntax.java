package minsk.codeanalysis.syntax;

import java.util.List;

public class BinaryExpressionSyntax extends ExpressionSyntax {
	private final ExpressionSyntax left;
	private final ExpressionSyntax right;
	private final SyntaxToken operatorToken;
	
	public BinaryExpressionSyntax(ExpressionSyntax left, SyntaxToken operatorToken, ExpressionSyntax right) {
		this.operatorToken = operatorToken;
		this.left = left;
		this.right = right;
	}
	
	public ExpressionSyntax getLeft() {
		return left;
	}

	public ExpressionSyntax getRight() {
		return right;
	}

	public SyntaxToken getOperatorToken() {
		return operatorToken;
	}
	

	@Override
	public Iterable<SyntaxNode> getChildren() {
		return List.of(getLeft(), getOperatorToken(), getRight());
	}

	@Override
	public SyntaxKind getKind() {
		return SyntaxKind.BinaryExpression;
	}
}
