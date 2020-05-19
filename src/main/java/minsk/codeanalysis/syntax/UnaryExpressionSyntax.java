package minsk.codeanalysis.syntax;

import java.util.List;

public class UnaryExpressionSyntax extends ExpressionSyntax {
	private final ExpressionSyntax operand;
	private final SyntaxToken operatorToken;
	
	public UnaryExpressionSyntax(SyntaxToken operatorToken, ExpressionSyntax operand) {
 		this.operatorToken = operatorToken;
		this.operand = operand;
	}
	
	public ExpressionSyntax getOperand() {
		return operand;
	}

	public SyntaxToken getOperatorToken() {
		return operatorToken;
	}
	
	@Override
	public List<SyntaxNode> getChildren() {
		return List.of(getOperatorToken(), getOperand());
	}

	@Override
	public SyntaxKind getKind() {
		return SyntaxKind.UnaryExpression;
	}
}