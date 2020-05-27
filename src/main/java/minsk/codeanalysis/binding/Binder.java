package minsk.codeanalysis.binding;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import minsk.codeanalysis.syntax.AssignmentExpressionSyntax;
import minsk.codeanalysis.syntax.BinaryExpressionSyntax;
import minsk.codeanalysis.syntax.BlockStatementSyntax;
import minsk.codeanalysis.syntax.CompilationUnitSyntax;
import minsk.codeanalysis.syntax.ExpressionStatementSyntax;
import minsk.codeanalysis.syntax.ExpressionSyntax;
import minsk.codeanalysis.syntax.LiteralExpressionSyntax;
import minsk.codeanalysis.syntax.NameExpressionSyntax;
import minsk.codeanalysis.syntax.ParenthesizedExpressionSyntax;
import minsk.codeanalysis.syntax.StatementSyntax;
import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.UnaryExpressionSyntax;
import minsk.codeanalysis.syntax.VariableDeclarationSyntax;
import minsk.diagnostics.*;

public class Binder implements Diagnosable {

	public static BoundGlobalScope bindGlobalScope(BoundGlobalScope previous, CompilationUnitSyntax syntax) {
		var parentScope = createParentScope(previous);
		var binder = new Binder(parentScope);
		var statement = binder.bindStatement(syntax.getStatement());
		var<VariableSymbol> variables = binder.getScope().getDeclaredVariables();
		var diagnostics = binder.getDiagnostics();
		
		if (previous != null)
			diagnostics.addFrom(previous);
		
		return new BoundGlobalScope(previous, diagnostics, variables, statement);
	}
	
	private BoundStatement bindStatement(StatementSyntax syntax) {
		switch (syntax.kind()) {
		case BlockStatement:
			return bindBlockStatement((BlockStatementSyntax) syntax);
		case VariableDeclaration:
			return bindVariableDeclaration((VariableDeclarationSyntax) syntax);
		case ExpressionStatement:
			return bindExpressionStatement((ExpressionStatementSyntax) syntax);
		default:
			throw new RuntimeException("Unexpected statement syntax: " + syntax.kind());
		}
	}

	private BoundBlockStatement bindBlockStatement(BlockStatementSyntax syntax) {
		scope = new BoundScope(scope);
		
		List<BoundStatement> statements = (List<BoundStatement>) syntax.statements().<StatementSyntax>stream()
				.map(s -> (BoundStatement) bindStatement((StatementSyntax)s))
				.collect(Collectors.toList());
		
//		var<BoundStatement> statements = syntax.statements().stream()
//				.map(s -> bindStatement(s))
//				.collect(Collectors.toList()); 
		
		scope = scope.getParent();
		
		return new BoundBlockStatement(statements);
	}

	private BoundStatement bindVariableDeclaration(VariableDeclarationSyntax syntax) {
		var name = syntax.identifier().text();
		var isReadOnly = syntax.keyword().kind() == SyntaxKind.LetKeyword;
		var initializer = bindExpression(syntax.initializer());
		var variable = new VariableSymbol(name, isReadOnly, initializer.getType());
		
		if (!scope.declare(variable)) {
			diagnostics.reportVariableAlreadyDeclared(syntax.identifier().getSpan(), name);
		}
		
		return new BoundVariableDeclaration(variable, initializer);
	}
	
	private BoundExpressionStatement bindExpressionStatement(ExpressionStatementSyntax syntax) {
		var expression = bindExpression(syntax.getExpression());
		return new BoundExpressionStatement(expression);
	}

	private static BoundScope createParentScope(BoundGlobalScope previous) {
		var<BoundGlobalScope> stack = new LinkedList<BoundGlobalScope>();

		while (previous != null) {
			stack.push(previous);
			previous = previous.getPrevious();
		}
		
		BoundScope parent = null;
		
		while (!stack.isEmpty()) {
			var scope = new BoundScope(parent);
			stack.pop().getVariables().forEach(scope::declare);
			parent = scope;
		}
		
		return parent;
	}

	private final DiagnosticsBag diagnostics = new DiagnosticsBag();
	private BoundScope scope;

	public Binder(BoundScope parent) {
		this.scope = new BoundScope(parent);
	}

	public BoundExpression bindExpression(ExpressionSyntax syntax) {
		switch (syntax.kind()) {
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
			throw new RuntimeException("Unexpected syntax " + syntax.kind());
		}
	}

	private BoundExpression bindNameExpression(NameExpressionSyntax syntax) {
		var name = syntax.getIdentifierToken().text();
		
		var variable = scope.lookup(name);
		
		if (variable == null) {
			diagnostics.reportUndefinedName(syntax.getIdentifierToken().getSpan(), name);
			return new BoundLiteralExpression(0);
		}
		
		return new BoundVariableExpression(variable);
	}
	
	private BoundExpression bindAssignmentExpression(AssignmentExpressionSyntax syntax) {
		var name = syntax.identifierToken().text();
		var boundExpression = bindExpression(syntax.expression());
		
		VariableSymbol variable = scope.lookup(name);
		
		if (variable == null) {
			diagnostics.reportUndefinedName(syntax.identifierToken().getSpan(), name);
			return boundExpression;
		}
		
		if (variable.isReadOnly()) {
			diagnostics.reportCannotAssign(syntax.equalsToken().getSpan(), name);
		}

		if (boundExpression.getType() != variable.type()) {
			diagnostics.reportCannotConvert(syntax.expression().getSpan(), boundExpression.getType(), variable.type());
		}
		
		return new BoundAssignmentExpression(variable, boundExpression);
	}

	private BoundExpression bindParenthesizedExpression(ParenthesizedExpressionSyntax parenthesizedExpressionSyntax) {
		return bindExpression(parenthesizedExpressionSyntax.expression());
	}

	private BoundExpression bindLiteralExpression(LiteralExpressionSyntax syntax) {
		var value = syntax.getValue();
		return new BoundLiteralExpression(value);
	}

	private BoundExpression bindUnaryExpression(UnaryExpressionSyntax syntax) {
		var boundOperand = bindExpression(syntax.operand());
		var boundOperator = BoundUnaryOperator.bind(syntax.operatorToken().kind(), boundOperand.getType());

		if (boundOperator == null) {
			diagnostics.reportUndefinedUnaryOperator(syntax.operatorToken().getSpan(), syntax.operatorToken().text(), boundOperand.getType());
			return boundOperand;
		}

		return new BoundUnaryExpression(boundOperator, boundOperand);
	}

	private BoundExpression bindBinaryExpression(BinaryExpressionSyntax syntax) {
		var boundLeft = bindExpression(syntax.left());
		var boundRight = bindExpression(syntax.right());
		var boundOperator = BoundBinaryOperator.bind(syntax.operatorToken().kind(), boundLeft.getType(), boundRight.getType());

		if (boundOperator == null) {
			diagnostics.reportUndefinedBinaryOperator(syntax.operatorToken().getSpan(), syntax.operatorToken().text(), boundLeft.getType(), boundRight.getType());
			return boundLeft;
		}

		return new BoundBinaryExpression(boundLeft, boundOperator, boundRight);
	}

	@Override
	public DiagnosticsBag getDiagnostics() {
		return diagnostics;
	}
	
	public BoundScope getScope() {
		return scope;
	}
}
