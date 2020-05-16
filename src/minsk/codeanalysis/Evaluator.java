package minsk.codeanalysis;

import java.util.Map;

import minsk.codeanalysis.binding.BoundAssignmentExpression;
import minsk.codeanalysis.binding.BoundBinaryExpression;
import minsk.codeanalysis.binding.BoundExpression;
import minsk.codeanalysis.binding.BoundLiteralExpression;
import minsk.codeanalysis.binding.BoundUnaryExpression;
import minsk.codeanalysis.binding.BoundVariableExpression;

public class Evaluator  {
	private final BoundExpression root;
	private final Map<String, Object> variables;

	public Evaluator(BoundExpression root, Map<String, Object> variables) {
		this.root = root;
		this.variables = variables;
	}
	
	public Object evaluate() {
		return evaluateExpression(root);
	}
	
	public Object evaluateExpression(BoundExpression expr) {
		if (expr instanceof BoundLiteralExpression) {
			var n = (BoundLiteralExpression) expr;
			return n.getValue();
			
		} else if (expr instanceof BoundVariableExpression) {
			var v = (BoundVariableExpression) expr;
			return variables.get(v.getName());
			
		} else if (expr instanceof BoundAssignmentExpression) {
			var a = (BoundAssignmentExpression) expr;
			var value = evaluateExpression(a.getExpression());
			variables.put(a.getName(), value);
			return value;			
		} else if (expr instanceof BoundUnaryExpression) {
			var u = (BoundUnaryExpression) expr;
			var operand = evaluateExpression(u.getOperand());
			
			switch (u.getOperator().getKind()) {
			case Identity:
				return (int) operand;
			case Negation:
				return -(int) operand;
			case LogicalNegation:
				return !(boolean) operand;
			default:
				throw new RuntimeException("Unexpected unary operator: " + u.getOperator());
			}
		} else if (expr instanceof BoundBinaryExpression) {
			var binaryExpression = (BoundBinaryExpression) expr;
			
			var left = evaluateExpression(binaryExpression.getLeft());
			var right = evaluateExpression(binaryExpression.getRight());
			
			switch (binaryExpression.getOperator().getKind()) {
			case Addition:
				return (int) left + (int) right;
			case Subtraction:
				return (int) left - (int) right;
			case Multiplication:
				return (int) left * (int) right;
			case Division:
				return (int) left / (int) right;
			case LogicalAnd:
				return (boolean) left && (boolean) right;
			case LogicalOr:
				return (boolean) left || (boolean) right;
			case Equals:
				return left.equals(right);
			case NotEquals:
				return !left.equals(right);
			default:
				throw new RuntimeException("Unexpected operator: " + binaryExpression.getKind());
			}
		}
		
		throw new RuntimeException("Invalid syntax node: " + expr.getKind());
	}

	public Map<String, Object> getVariables() {
		return variables;
	}
}