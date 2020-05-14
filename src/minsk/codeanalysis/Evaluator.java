package minsk.codeanalysis;

import minsk.codeanalysis.binding.BoundBinaryExpression;
import minsk.codeanalysis.binding.BoundExpression;
import minsk.codeanalysis.binding.BoundLiteralExpression;
import minsk.codeanalysis.binding.BoundUnaryExpression;

public class Evaluator {
	private final BoundExpression root;

	public Evaluator(BoundExpression root) {
		this.root = root;
	}
	
	public int evaluate() {
		return evaluateExpression(root);
	}
	
	public int evaluateExpression(BoundExpression expr) {
		if (expr instanceof BoundLiteralExpression) {
			var n = (BoundLiteralExpression) expr;
			return (int) n.getValue();
		} else if (expr instanceof BoundUnaryExpression) {
			var u = (BoundUnaryExpression) expr;
			var operand = evaluateExpression(u.getOperand());
			
			switch (u.getOperatorKind()) {
			case Identity:
				return operand;
			case Negation:
				return -operand;
			default:
				throw new RuntimeException("Unexpected unary operator: " + u.getOperatorKind());
			}
		} else if (expr instanceof BoundBinaryExpression) {
			var binaryExpression = (BoundBinaryExpression) expr;
			
			var left = evaluateExpression(binaryExpression.getLeft());
			var right = evaluateExpression(binaryExpression.getRight());
			
			switch (binaryExpression.getOperaetorKind()) {
			case Addition:
				return left + right;
			case Subtraction:
				return left - right;
			case Multiplication:
				return left * right;
			case Division:
				return left / right;
			default:
				throw new RuntimeException("Unexpected operator: " + binaryExpression.getKind());
			}
		}
		
		throw new RuntimeException("Invalid syntax node: " + expr.getKind());
	}
}