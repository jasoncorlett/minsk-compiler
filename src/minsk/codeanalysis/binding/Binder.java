package minsk.codeanalysis.binding;

import minsk.codeanalysis.syntax.BinaryExpressionSyntax;
import minsk.codeanalysis.syntax.ExpressionSyntax;
import minsk.codeanalysis.syntax.LiteralExpressionSyntax;
import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.UnaryExpressionSyntax;
import minsk.diagnostics.*;

public class Binder implements Diagnosable {
	
	private final Diagnostics diagnostics = new Diagnostics();

	public BoundExpression bindExpression(ExpressionSyntax syntax) {
		switch (syntax.getKind()) {
		case LiteralExpression:
			return bindLiteralExpression((LiteralExpressionSyntax) syntax);
		case UnaryExpression:
			return bindUnaryExpression((UnaryExpressionSyntax) syntax);
		case BinaryExpression:
			return bindBinaryExpression((BinaryExpressionSyntax) syntax);
		default:
			throw new RuntimeException("Unexpected syntax " + syntax.getKind());
		}
	}

	private BoundExpression bindLiteralExpression(LiteralExpressionSyntax syntax) {
		var value = syntax.getValue();
		return new BoundLiteralExpression(value);
	}

	private BoundUnaryOperatorKind bindUnaryOperatorKind(SyntaxKind kind, Class<?> operandType) {
		if (operandType != Integer.class) {
			return null;
		}
		
		switch (kind) {
		case PlusToken:
			return BoundUnaryOperatorKind.Identity;
		case MinusToken:
			return BoundUnaryOperatorKind.Negation;
		default:
			throw new RuntimeException("Unexpected unary operator: " + kind);
		}
	}

	private BoundExpression bindUnaryExpression(UnaryExpressionSyntax syntax) {
		var boundOperand = bindExpression(syntax.getOperand());
		var boundOperatorKind = bindUnaryOperatorKind(syntax.getOperatorToken().getKind(), boundOperand.getType());

		if (boundOperatorKind == null) {
			diagnostics.add("Unary operator " + syntax.getOperatorToken().getText() + " is not defined for " + boundOperand.getType());
			return boundOperand;
		}
		
		return new BoundUnaryExpression(boundOperatorKind, boundOperand);
	}

	private BoundBinaryOperatorKind bindBinaryOperatorKind(SyntaxKind kind, Class<?> leftType, Class<?> rightType) {
		if (leftType != Integer.class || rightType != Integer.class) {
			return null;
		}
	
		switch (kind) {
		case PlusToken:
			return BoundBinaryOperatorKind.Addition;
		case MinusToken:
			return BoundBinaryOperatorKind.Subtraction;
		case StarToken:
			return BoundBinaryOperatorKind.Multiplication;
		case SlashToken:
			return BoundBinaryOperatorKind.Division;
		default:
			throw new RuntimeException("Unexpected binary operator " + kind);
		}
	}

	private BoundExpression bindBinaryExpression(BinaryExpressionSyntax syntax) {
		var boundLeft = bindExpression(syntax.getLeft());
		var boundRight = bindExpression(syntax.getRight());
		var boundOperatorKind = bindBinaryOperatorKind(syntax.getOperatorToken().getKind(), boundLeft.getType(), boundRight.getType());
		
		if (boundOperatorKind == null) {
			diagnostics.add(String.format("Binary operator %s is not defined for types %s and %s", syntax.getOperatorToken().getText(), boundLeft.getType(), boundRight.getType()));
			return boundLeft;
		}
		
		return new BoundBinaryExpression(boundLeft, boundOperatorKind, boundRight);
	}

	@Override
	public Diagnostics getDiagnostics() {
		return diagnostics;
	}
}
