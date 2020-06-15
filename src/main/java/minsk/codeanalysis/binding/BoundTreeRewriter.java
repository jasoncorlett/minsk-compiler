package minsk.codeanalysis.binding;

import java.util.ArrayList;
import java.util.LinkedList;

public abstract class BoundTreeRewriter {

	public BoundStatement rewriteStatement(BoundStatement node) {
		return switch (node.getKind()) {
			case BlockStatement 		-> rewriteBlockStatement((BoundBlockStatement) node);
			case VariableDeclaration 	-> rewriteVariableDeclaration((BoundVariableDeclaration) node);
			case IfStatement 			-> rewriteIfStatement((BoundIfStatement) node);
			case WhileStatement 		-> rewriteWhileStatement((BoundWhileStatement) node);
			case DoStatement 			-> rewriteDoStatement((BoundDoStatement) node);
			case UntilStatement			-> rewriteUntilStatement((BoundUntilStatement) node);
			case ForStatement 			-> rewriteForStatement((BoundForStatement) node);
			case LabelStatement			-> rewriteLabelStatement((BoundLabelStatement) node);
			case GotoStatement			-> rewriteGotoStatement((BoundGotoStatement) node);
			case ConditionalGotoStatement -> rewriteConditionalGotoStatement((BoundConditionalGotoStatement) node);
			case ExpressionStatement 	-> rewriteExpressionStatement((BoundExpressionStatement) node);
			default 					-> throw new IllegalArgumentException("Unexpected value: " + node.getKind());
		};
	}

	protected BoundStatement rewriteBlockStatement(BoundBlockStatement node) {
		var changed = false;
		var result = new LinkedList<BoundStatement>();
				
		for (var oldStatement : node.getStatements()) {
			var newStatement = rewriteStatement(oldStatement);
			
			if (!newStatement.equals(oldStatement)) {
				changed = true;
				result.add(newStatement);
			}
			else {
				result.add(oldStatement);
			}
		}
		
		return (changed) ? new BoundBlockStatement(result) : node;
	}

	protected BoundStatement rewriteVariableDeclaration(BoundVariableDeclaration node) {
		var initializer = rewriteExpression(node.getInitializer());
		
		return (node.getInitializer().equals(initializer)) ? node : new BoundVariableDeclaration(node.getVariable(), initializer);
	}

	protected BoundStatement rewriteIfStatement(BoundIfStatement node) {
		var condition = rewriteExpression(node.getCondition());
		var thenStatement = rewriteStatement(node.getThenStatement());
		var elseClause = node.getElseClause() == null ? null : rewriteStatement(node.getElseClause());
		
		if (node.getCondition().equals(condition)
				&& node.getThenStatement().equals(thenStatement)
				&& (node.getElseClause() == null || node.getElseClause().equals(elseClause))) {
			return node;
		}
		
		return new BoundIfStatement(condition, thenStatement, elseClause);
	}

	protected BoundStatement rewriteWhileStatement(BoundWhileStatement node) {
		var condition = rewriteExpression(node.getCondition());
		var body = rewriteStatement(node.getBody());
		
		if (node.getBody().equals(body) && node.getCondition().equals(condition)) {
			return node;
		}
		
		return new BoundWhileStatement(condition, body);
	}
	
	protected BoundStatement rewriteForStatement(BoundForStatement node) {
		var lowerBound = rewriteExpression(node.getLowerBound());
		var upperBound = rewriteExpression(node.getUpperBound());
		var body = rewriteStatement(node.getBody());
		
		if (node.getLowerBound().equals(lowerBound) && node.getUpperBound().equals(upperBound) && node.getBody().equals(body)) {
			return node;
		}
		
		return new BoundForStatement(node.getVariable(), lowerBound, upperBound, body);
	}

	protected BoundStatement rewriteDoStatement(BoundDoStatement node) {
		var condition = rewriteExpression(node.getCondition());
		var body = rewriteStatement(node.getBody());

		if (condition.equals(node.getCondition()) && body.equals(node.getBody())) {
			return node;
		}

		return new BoundDoStatement(body, node.isContinueWhen(), condition);
	}

	protected BoundStatement rewriteUntilStatement(BoundUntilStatement node) {
		var condition = rewriteExpression(node.getCondition());
		var body = rewriteStatement(node.getBody());

		if (condition.equals(node.getCondition()) && body.equals(node.getBody())) {
			return node;
		}

		return new BoundUntilStatement(condition, body);
	}
	
	protected BoundStatement rewriteLabelStatement(BoundLabelStatement node) {
		return node;
	}
	
	protected BoundStatement rewriteGotoStatement(BoundGotoStatement node) {
		return node;
	}
	
	protected BoundStatement rewriteConditionalGotoStatement(BoundConditionalGotoStatement node) {
		var condition = rewriteExpression(node.getCondition());
		
		if (node.getCondition().equals(condition)) {
			return node;
		}
		
		return new BoundConditionalGotoStatement(node.getLabel(), condition, node.getJumpWhen());
	}
	
	protected BoundStatement rewriteExpressionStatement(BoundExpressionStatement node) {
		var expression = rewriteExpression(node.getExpression());
		
		if (node.getExpression().equals(expression)) {
			return node;
		}
		
		return new BoundExpressionStatement(expression);
	}
	
	public BoundExpression rewriteExpression(BoundExpression node) {
		return switch (node.getKind()) {
		case AssignmentExpression -> rewriteAssignmentExpression((BoundAssignmentExpression) node);
		case BinaryExpression -> rewriteBinaryExpression((BoundBinaryExpression) node);
		case LiteralExpression -> rewriteLiteralExpression((BoundLiteralExpression) node);
		case UnaryExpression -> rewriteUnaryExpression((BoundUnaryExpression) node);
		case VariableExpression -> rewriteVariableExpression((BoundVariableExpression) node);
		case ErrorExpression -> rewriteErrorExpression((BoundErrorExpression) node);
		case CallExpression -> rewriteCallExpression((BoundCallExpression) node);
		case ConversionExpression -> rewriteConversionExpression((BoundConversionExpression) node);
		default -> throw new IllegalArgumentException("Unexpected expression: " + node);
		};
	}

	private BoundExpression rewriteConversionExpression(BoundConversionExpression node) {
		var expression = rewriteExpression(node.getExpression());

		if (expression.equals(node.getExpression())) {
			return node;
		}

		return new BoundConversionExpression(node.getType(), node.getExpression());
	}

	// TODO: Avoid creating list if arguments do not change
	protected BoundExpression rewriteCallExpression(BoundCallExpression node) {
		var arguments = new ArrayList<BoundExpression>();
		var changed = false;

		for (var oldArgument : node.getArguments()) {
			var newArgument = rewriteExpression(oldArgument);

			arguments.add(newArgument);

			if (!oldArgument.equals(newArgument)) {
				changed = true;
			}
		}

		if (!changed) {
			return node;
		}

		return new BoundCallExpression(node.getFunction(), arguments);
	}

	protected BoundExpression rewriteAssignmentExpression(BoundAssignmentExpression node) {
		var expression = rewriteExpression(node.getExpression());
		
		if (node.getExpression().equals(expression)) {
			return node;
		}
		
		return new BoundAssignmentExpression(node.getVariable(), expression);
	}
	
	protected BoundExpression rewriteBinaryExpression(BoundBinaryExpression node) {
		var left = rewriteExpression(node.getLeft());
		var right = rewriteExpression(node.getRight());
		
		if (node.getLeft().equals(left) && node.getRight().equals(right)) {
			return node;
		}
		
		return new BoundBinaryExpression(left, node.getOperator(), right);
	}

	protected BoundExpression rewriteLiteralExpression(BoundLiteralExpression node) {
		return node;
	}

	protected BoundExpression rewriteUnaryExpression(BoundUnaryExpression node) {
		var operand = rewriteExpression(node.getOperand());
		
		if (node.getOperand().equals(operand)) {
			return node;
		}
		
		return new BoundUnaryExpression(node.getOperator(), operand);
	}

	protected BoundExpression rewriteVariableExpression(BoundVariableExpression node) {
		return node;
	}
	
	protected BoundExpression rewriteErrorExpression(BoundErrorExpression node) {
		return node;
	}
}
