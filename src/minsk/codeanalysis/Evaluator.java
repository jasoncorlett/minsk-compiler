package minsk.codeanalysis;

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
			return (int) n.literalToken.value;
		} else if (expr instanceof UnaryExpressionSyntax) {
			var u = (UnaryExpressionSyntax) expr;
			var operand = evaluateExpression(u.operand);
			
			if (u.operatorToken.kind == SyntaxKind.PlusToken) {
				return operand;
			} else if (u.operatorToken.kind == SyntaxKind.MinusToken) {
				return -operand;
			} else {
				throw new RuntimeException("Unexpected unary operator: " + u.operatorToken.kind);
			}
		} else if (expr instanceof BinaryExpressionSyntax) {
			var binaryExpression = (BinaryExpressionSyntax) expr;
			
			var left = evaluateExpression(binaryExpression.left);
			var right = evaluateExpression(binaryExpression.right);
			
			switch (binaryExpression.operatorToken.kind) {
			case PlusToken:
				return left + right;
			case MinusToken:
				return left - right;
			case StarToken:
				return left * right;
			case SlashToken:
				return left / right;
			default:
				throw new RuntimeException("Unexpected operator: " + binaryExpression.operatorToken.kind);
			}
		} else if (expr instanceof ParenthesizedExpressionSyntax) {
			var paren = (ParenthesizedExpressionSyntax) expr;
			return evaluateExpression(paren.expression);
		}
		
		throw new RuntimeException("Invalid syntax node: " + expr.kind);
	}
}