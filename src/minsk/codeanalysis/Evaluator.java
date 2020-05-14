package minsk.codeanalysis;

import minsk.codeanalysis.syntax.BinaryExpressionSyntax;
import minsk.codeanalysis.syntax.ExpressionSyntax;
import minsk.codeanalysis.syntax.LiteralExpressionSyntax;
import minsk.codeanalysis.syntax.ParenthesizedExpressionSyntax;
import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.UnaryExpressionSyntax;

public class Evaluator {
	private ExpressionSyntax root;

	public Evaluator(ExpressionSyntax root) {
		this.root = root;
	}
	
	public int evaluate() {
		return evaluateExpression(root);
	}
	
	public int evaluateExpression(ExpressionSyntax expr) {
		if (expr instanceof LiteralExpressionSyntax) {
			var n = (LiteralExpressionSyntax) expr;
			return (int) n.getLiteralToken().getValue();
		} else if (expr instanceof UnaryExpressionSyntax) {
			var u = (UnaryExpressionSyntax) expr;
			var operand = evaluateExpression(u.getOperand());
			
			if (u.getOperatorToken().getKind() == SyntaxKind.PlusToken) {
				return operand;
			} else if (u.getOperatorToken().getKind() == SyntaxKind.MinusToken) {
				return -operand;
			} else {
				throw new RuntimeException("Unexpected unary operator: " + u.getOperatorToken().getKind());
			}
		} else if (expr instanceof BinaryExpressionSyntax) {
			var binaryExpression = (BinaryExpressionSyntax) expr;
			
			var left = evaluateExpression(binaryExpression.getLeft());
			var right = evaluateExpression(binaryExpression.getRight());
			
			switch (binaryExpression.getOperatorToken().getKind()) {
			case PlusToken:
				return left + right;
			case MinusToken:
				return left - right;
			case StarToken:
				return left * right;
			case SlashToken:
				return left / right;
			default:
				throw new RuntimeException("Unexpected operator: " + binaryExpression.getOperatorToken().getKind());
			}
		} else if (expr instanceof ParenthesizedExpressionSyntax) {
			var paren = (ParenthesizedExpressionSyntax) expr;
			return evaluateExpression(paren.getExpression());
		}
		
		throw new RuntimeException("Invalid syntax node: " + expr.getKind());
	}
}