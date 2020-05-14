package minsk.codeanalysis;

import java.util.List;

public class UnaryExpressionSyntax extends ExpressionSyntax {
	public final ExpressionSyntax operand;
	public final SyntaxToken operatorToken;
	
	public UnaryExpressionSyntax(SyntaxToken operatorToken, ExpressionSyntax operand) {
		super(SyntaxKind.UnaryExpression);
		this.operatorToken = operatorToken;
		this.operand = operand;
	}
	
	@Override
	public Iterable<SyntaxNode> getChildren() {
		return List.of(operatorToken, operand);
	}
}