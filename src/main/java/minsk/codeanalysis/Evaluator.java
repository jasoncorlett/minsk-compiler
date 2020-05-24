package minsk.codeanalysis;

import java.util.Map;

import minsk.codeanalysis.binding.BoundAssignmentExpression;
import minsk.codeanalysis.binding.BoundBinaryExpression;
import minsk.codeanalysis.binding.BoundBlockStatement;
import minsk.codeanalysis.binding.BoundExpression;
import minsk.codeanalysis.binding.BoundExpressionStatement;
import minsk.codeanalysis.binding.BoundLiteralExpression;
import minsk.codeanalysis.binding.BoundStatement;
import minsk.codeanalysis.binding.BoundUnaryExpression;
import minsk.codeanalysis.binding.BoundVariableExpression;
import minsk.codeanalysis.binding.VariableSymbol;

public class Evaluator  {
	private final BoundStatement root;
	private final Map<VariableSymbol, Object> variables;
	private Object lastValue;
	
	public Evaluator(BoundStatement root, Map<VariableSymbol, Object> variables) {
		this.root = root;
		this.variables = variables;
	}
	
	public Object evaluate() {
		evaluateStatement(root);
		return lastValue;
	}
	
	private void evaluateStatement(BoundStatement node) {
		switch (node.getKind()) {
		case BlockStatement:
			evaluateBlockStatement((BoundBlockStatement) node);
			break;
		case ExpressionStatement:
			evaluateExpressionStatement((BoundExpressionStatement) node);
			break;
		default:
			throw new RuntimeException("Unexpected statement node: " + node.getKind());
		}
	}

	private void evaluateBlockStatement(BoundBlockStatement node) {
		for (var statment : node.getStatements())
			evaluateStatement(statment);
	}

	private void evaluateExpressionStatement(BoundExpressionStatement node) {
		lastValue = evaluateExpression(node.getExpression());
	}

	public Object evaluateExpression(BoundExpression expr) {
		switch (expr.getKind()) {
		case LiteralExpression:
			return evaluateLiteralExpression((BoundLiteralExpression) expr);
		case VariableExpression:
			return evaluateVariableExpression((BoundVariableExpression) expr);
		case AssignmentExpression:
			return evaluateAssignmentExpression((BoundAssignmentExpression) expr);
		case UnaryExpression:
			return evaluateUnaryExpression((BoundUnaryExpression) expr);
		case BinaryExpression:
			return evaluateBinaryExpression((BoundBinaryExpression) expr);
		default:
			throw new RuntimeException("Invalid syntax node: " + expr.getKind());
		}
	}

	private Object evaluateBinaryExpression(BoundBinaryExpression binaryExpression) {
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

	private Object evaluateUnaryExpression(BoundUnaryExpression expr) {
		var operand = evaluateExpression(expr.getOperand());
		
		switch (expr.getOperator().getKind()) {
		case Identity:
			return (int) operand;
		case Negation:
			return -(int) operand;
		case LogicalNegation:
			return !(boolean) operand;
		default:
			throw new RuntimeException("Unexpected unary operator: " + expr.getOperator());
		}
	}

	private Object evaluateAssignmentExpression(BoundAssignmentExpression a) {
		var value = evaluateExpression(a.getExpression());
		variables.put(a.getVariable(), value);			
		return value;
	}

	private Object evaluateVariableExpression(BoundVariableExpression v) {
		return variables.get(v.getVariable());
	}

	private Object evaluateLiteralExpression(BoundLiteralExpression expr) {
		return expr.getValue();
	}

	public Map<VariableSymbol, Object> getVariables() {
		return variables;
	}
}