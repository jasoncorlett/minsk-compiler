package minsk.codeanalysis;

import java.util.List;

public class BinaryExpressionSyntax extends ExpressionSyntax {
	public final ExpressionSyntax left;
	public final ExpressionSyntax right;
	public final SyntaxToken operatorToken;
	
	public BinaryExpressionSyntax(ExpressionSyntax left, SyntaxToken operatorToken, ExpressionSyntax right) {
		super(SyntaxKind.BinaryExpression);
		this.operatorToken = operatorToken;
		this.left = left;
		this.right = right;
	}
	
	@Override
	public Iterable<SyntaxNode> getChildren() {
		return List.of(left, operatorToken, right);
	}
}