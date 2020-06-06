package minsk.codeanalysis;

import java.util.HashMap;
import java.util.Map;

import minsk.codeanalysis.binding.BoundAssignmentExpression;
import minsk.codeanalysis.binding.BoundBinaryExpression;
import minsk.codeanalysis.binding.BoundBlockStatement;
import minsk.codeanalysis.binding.BoundConditionalGotoStatement;
import minsk.codeanalysis.binding.BoundExpression;
import minsk.codeanalysis.binding.BoundExpressionStatement;
import minsk.codeanalysis.binding.BoundGotoStatement;
import minsk.codeanalysis.binding.BoundIfStatement;
import minsk.codeanalysis.binding.BoundLabelStatement;
import minsk.codeanalysis.binding.BoundLiteralExpression;
import minsk.codeanalysis.binding.BoundStatement;
import minsk.codeanalysis.binding.BoundUnaryExpression;
import minsk.codeanalysis.binding.BoundVariableDeclaration;
import minsk.codeanalysis.binding.BoundVariableExpression;
import minsk.codeanalysis.binding.BoundWhileStatement;
import minsk.codeanalysis.binding.VariableSymbol;
import minsk.codeanalysis.syntax.SyntaxTree;

public class Evaluator  {
	private final BoundBlockStatement root;
	private final Map<VariableSymbol, Object> variables;
	private Object lastValue;
	
	public static EvaluationResult evaluateProgram(String text) {
		return evaluateProgram(text, null);
	}
	
	public static EvaluationResult evaluateProgram(String text, Map<VariableSymbol, Object> variables) {
		var syntaxTree = SyntaxTree.parse(text);
		var compilation = new Compilation(syntaxTree);
		if (variables == null) {
			variables = new HashMap<>();
		}
		return compilation.evaluate(variables);
	}
	
	public Evaluator(BoundBlockStatement root, Map<VariableSymbol, Object> variables) {
		this.root = root;
		this.variables = variables;
	}
	
	public Object evaluate() {
		var labelToIndex = new HashMap<LabelSymbol, Integer>();
		
		for (var i = 0; i < root.getStatements().size(); i++) {
			if (root.getStatements().get(i) instanceof BoundLabelStatement label) {
				labelToIndex.put(label.getLabel(), i);
			}
		}
		
		var index = 0;
		
		while (index < root.getStatements().size()) {
			var stmt = root.getStatements().get(index);
			
			switch (stmt.getKind()) {
			case VariableDeclaration:
				evaluateVariableDeclaration((BoundVariableDeclaration) stmt);
				index++;
				break;
			case ExpressionStatement:
				evaluateExpressionStatement((BoundExpressionStatement) stmt);
				index++;
				break;
			case GotoStatement:
				var gs = (BoundGotoStatement) stmt;
				index = labelToIndex.get(gs.getLabel());
				break;
			case ConditionalGotoStatement:
				var cgs = (BoundConditionalGotoStatement) stmt;
				var condition = (boolean) evaluateExpression(cgs.getCondition());
				
				if (condition == cgs.getJumpWhen()) {
					index = labelToIndex.get(cgs.getLabel());
				}
				else {
					index++;
				}
				break;
			case LabelStatement:
				index++;
				break;
			default:
				throw new RuntimeException("Unexpected statement: " + stmt.getKind());
			}
		}

		return lastValue;
	}
	
	private void evaluateVariableDeclaration(BoundVariableDeclaration node) {
		lastValue = evaluateExpression(node.getInitializer());
		variables.put(node.getVariable(), lastValue);
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
		case Modulo:
			return (int) left % (int) right;
		case LogicalAnd:
			return (boolean) left && (boolean) right;
		case LogicalOr:
			return (boolean) left || (boolean) right;
		case Equals:
			return left.equals(right);
		case NotEquals:
			return !left.equals(right);
		case Less:
			return (int) left < (int) right;
		case LessEquals:
			return (int) left <= (int) right;
		case Greater:
			return (int) left > (int) right;
		case GreaterEquals:
			return (int) left >= (int) right;
		case BitwiseAnd:
			if (binaryExpression.getType() == Integer.class) {
				return (int) left & (int) right;
			}
			else {
				return (boolean) left & (boolean) right;
			}
		case BitwiseOr:
			if (binaryExpression.getType() == Integer.class) {
				return (int) left | (int) right;
			}
			else {
				return (boolean) left | (boolean) right;
			}
		case BitwiseXor:
			if (binaryExpression.getType() == Integer.class) {
				return (int) left ^ (int) right;
			}
			else {
				return (boolean) left ^ (boolean) right;
			}
		default:
			throw new RuntimeException("Unexpected operator: " + binaryExpression.getOperator().getSyntaxKind().getFixedText());
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