package minsk.codeanalysis;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import minsk.IOHelper;
import minsk.codeanalysis.binding.BoundAssignmentExpression;
import minsk.codeanalysis.binding.BoundBinaryExpression;
import minsk.codeanalysis.binding.BoundBlockStatement;
import minsk.codeanalysis.binding.BoundCallExpression;
import minsk.codeanalysis.binding.BoundConditionalGotoStatement;
import minsk.codeanalysis.binding.BoundExpression;
import minsk.codeanalysis.binding.BoundExpressionStatement;
import minsk.codeanalysis.binding.BoundGotoStatement;
import minsk.codeanalysis.binding.BoundLabel;
import minsk.codeanalysis.binding.BoundLabelStatement;
import minsk.codeanalysis.binding.BoundLiteralExpression;
import minsk.codeanalysis.binding.BoundUnaryExpression;
import minsk.codeanalysis.binding.BoundVariableDeclaration;
import minsk.codeanalysis.binding.BoundVariableExpression;
import minsk.codeanalysis.symbols.BuiltinFunctions;
import minsk.codeanalysis.symbols.TypeSymbol;
import minsk.codeanalysis.symbols.VariableSymbol;
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
		var labelToIndex = new HashMap<BoundLabel, Integer>();
		
		for (var i = 0; i < root.getStatements().size(); i++) {
			var node = root.getStatements().get(i);
			if (root.getStatements().get(i) instanceof BoundLabelStatement) {
				var label = (BoundLabelStatement) node;
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
		return switch (expr.getKind()) {
			case LiteralExpression -> evaluateLiteralExpression((BoundLiteralExpression) expr);
			case VariableExpression -> evaluateVariableExpression((BoundVariableExpression) expr);
			case AssignmentExpression -> evaluateAssignmentExpression((BoundAssignmentExpression) expr);
			case UnaryExpression -> evaluateUnaryExpression((BoundUnaryExpression) expr);
			case BinaryExpression -> evaluateBinaryExpression((BoundBinaryExpression) expr);
			case CallExpression ->  evaluateCallExpression((BoundCallExpression) expr);
			default -> throw new RuntimeException("Invalid syntax node: " + expr.getKind());
		};
	}

	private Object evaluateCallExpression(BoundCallExpression expr) {
		if (expr.getFunction().equals(BuiltinFunctions.input)) {
			return IOHelper.readLine();
		}
		else if (expr.getFunction().equals(BuiltinFunctions.print)) {
			var message = evaluateExpression(expr.getArguments().get(0));

			System.out.println(message);
			return null;
		}
		else {
			throw new RuntimeException("Impossible function '%s'".formatted(expr.getFunction()));
		}
	}

	private Object evaluateBinaryExpression(BoundBinaryExpression binaryExpression) {
		var left = evaluateExpression(binaryExpression.getLeft());
		var right = evaluateExpression(binaryExpression.getRight());
		
		switch (binaryExpression.getOperator().getKind()) {
		case Addition:
			if (binaryExpression.getType() == TypeSymbol.Int) {
				return (int) left + (int) right;
			}
			else {
				return left.toString() + right.toString();
			}
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
			if (binaryExpression.getType().equals(TypeSymbol.Int)) {
				return (int) left & (int) right;
			}
			else {
				return (boolean) left & (boolean) right;
			}
		case BitwiseOr:
			if (binaryExpression.getType().equals(TypeSymbol.Int)) {
				return (int) left | (int) right;
			}
			else {
				return (boolean) left | (boolean) right;
			}
		case BitwiseXor:
			if (binaryExpression.getType().equals(TypeSymbol.Int)) {
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
			return operand;
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