package minsk.codeanalysis.syntax.parser;

import minsk.codeanalysis.Nested;
import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;

public class UnaryExpressionSyntax extends ExpressionSyntax {
	private final ExpressionSyntax operand;
	private final SyntaxToken operatorToken;
	
	public UnaryExpressionSyntax(SyntaxToken operatorToken, ExpressionSyntax operand) {
 		this.operatorToken = operatorToken;
		this.operand = operand;
	}
	
	@Nested(2)
	public ExpressionSyntax getOperand() {
		return operand;
	}

	@Nested(1)
	public SyntaxToken getOperatorToken() {
		return operatorToken;
	}
	
	@Override
	public SyntaxKind getKind() {
		return SyntaxKind.UnaryExpression;
	}
}