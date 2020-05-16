package minsk.codeanalysis.binding;

import minsk.codeanalysis.syntax.BinaryExpressionSyntax;
import minsk.codeanalysis.syntax.ExpressionSyntax;
import minsk.codeanalysis.syntax.LiteralExpressionSyntax;
import minsk.codeanalysis.syntax.ParenthesizedExpressionSyntax;
import minsk.codeanalysis.syntax.UnaryExpressionSyntax;
import minsk.diagnostics.*;

public class Binder implements Diagnosable {

	private final DiagnosticsBag diagnostics = new DiagnosticsBag();

	public BoundExpression bindExpression(ExpressionSyntax syntax) {
		switch (syntax.getKind()) {
		case LiteralExpression:
			return bindLiteralExpression((LiteralExpressionSyntax) syntax);
		case UnaryExpression:
			return bindUnaryExpression((UnaryExpressionSyntax) syntax);
		case BinaryExpression:
			return bindBinaryExpression((BinaryExpressionSyntax) syntax);
		case ParenthesizedExpression:
			return bindExpression(((ParenthesizedExpressionSyntax)syntax).getExpression());
		default:
			throw new RuntimeException("Unexpected syntax " + syntax.getKind());
		}
	}

	private BoundExpression bindLiteralExpression(LiteralExpressionSyntax syntax) {
		var value = syntax.getValue();
		return new BoundLiteralExpression(value);
	}

	private BoundExpression bindUnaryExpression(UnaryExpressionSyntax syntax) {
		var boundOperand = bindExpression(syntax.getOperand());
		var boundOperator = BoundUnaryOperator.bind(syntax.getOperatorToken().getKind(), boundOperand.getType());

		if (boundOperator == null) {
			diagnostics.reportUndefinedUnaryOperator(syntax.getOperatorToken().getSpan(), syntax.getOperatorToken().getText(), boundOperand.getType());
			return boundOperand;
		}

		return new BoundUnaryExpression(boundOperator, boundOperand);
	}

	private BoundExpression bindBinaryExpression(BinaryExpressionSyntax syntax) {
		var boundLeft = bindExpression(syntax.getLeft());
		var boundRight = bindExpression(syntax.getRight());
		var boundOperator = BoundBinaryOperator.bind(syntax.getOperatorToken().getKind(), boundLeft.getType(), boundRight.getType());

		if (boundOperator == null) {
			diagnostics.reportUndefinedBinaryOperator(syntax.getOperatorToken().getSpan(), syntax.getOperatorToken().getText(), boundLeft.getType(), boundRight.getType());
			return boundLeft;
		}

		return new BoundBinaryExpression(boundLeft, boundOperator, boundRight);
	}

	@Override
	public DiagnosticsBag getDiagnostics() {
		return diagnostics;
	}
}
