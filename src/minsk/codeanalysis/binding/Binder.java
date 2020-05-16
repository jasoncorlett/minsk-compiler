package minsk.codeanalysis.binding;

import java.util.Map;

import minsk.codeanalysis.syntax.AssignmentExpressionSyntax;
import minsk.codeanalysis.syntax.BinaryExpressionSyntax;
import minsk.codeanalysis.syntax.ExpressionSyntax;
import minsk.codeanalysis.syntax.LiteralExpressionSyntax;
import minsk.codeanalysis.syntax.NameExpressionSyntax;
import minsk.codeanalysis.syntax.ParenthesizedExpressionSyntax;
import minsk.codeanalysis.syntax.UnaryExpressionSyntax;
import minsk.diagnostics.*;

public class Binder implements Diagnosable {

	private final DiagnosticsBag diagnostics = new DiagnosticsBag();
	private final Map<VariableSymbol, Object> variables;

	public Binder(Map<VariableSymbol, Object> variables) {
		this.variables = variables;
	}

	public BoundExpression bindExpression(ExpressionSyntax syntax) {
		switch (syntax.getKind()) {
		case ParenthesizedExpression:
			return bindParenthesizedExpression(((ParenthesizedExpressionSyntax)syntax));
		case LiteralExpression:
			return bindLiteralExpression((LiteralExpressionSyntax) syntax);
		case NameExpression:
			return bindNameExpression((NameExpressionSyntax)syntax);
		case AssignmentExpression:
			return bindAssignmentExpression((AssignmentExpressionSyntax)syntax);
		case UnaryExpression:
			return bindUnaryExpression((UnaryExpressionSyntax) syntax);
		case BinaryExpression:
			return bindBinaryExpression((BinaryExpressionSyntax) syntax);
		default:
			throw new RuntimeException("Unexpected syntax " + syntax.getKind());
		}
	}

	private BoundExpression bindNameExpression(NameExpressionSyntax syntax) {
		var name = syntax.getIdentifierToken().getText();
		
		var variable = variables.keySet().stream().filter(v -> name.equals(v.getName())).findFirst();
		
		if (variable.isEmpty()) {
			diagnostics.reportUndefinedName(syntax.getIdentifierToken().getSpan(), name);
			return new BoundLiteralExpression(0);
		}
		
		return new BoundVariableExpression(variable.get());
	}
	
	private BoundExpression bindAssignmentExpression(AssignmentExpressionSyntax syntax) {
		var name = syntax.getIdentifierToken().getText();
		var boundExpression = bindExpression(syntax.getExpression());
		var variable = new VariableSymbol(name, boundExpression.getType()); 
		
		var existingVariable = variables.keySet().stream().filter(v -> name.equals(v.getName())).findFirst();
		
		if (existingVariable.isPresent()) {
			variables.remove(existingVariable.get());
		}
		
		variables.put(variable, null);
		
		return new BoundAssignmentExpression(variable, boundExpression);
	}

	private BoundExpression bindParenthesizedExpression(ParenthesizedExpressionSyntax parenthesizedExpressionSyntax) {
		return bindExpression(parenthesizedExpressionSyntax.getExpression());
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
	public Map<VariableSymbol, Object> getVariables() {
		return variables;
	}
}
