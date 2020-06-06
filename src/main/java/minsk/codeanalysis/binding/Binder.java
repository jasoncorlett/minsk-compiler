package minsk.codeanalysis.binding;

import java.util.LinkedList;
import java.util.stream.Collectors;

import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.parser.AssignmentExpressionSyntax;
import minsk.codeanalysis.syntax.parser.BinaryExpressionSyntax;
import minsk.codeanalysis.syntax.parser.BlockStatementSyntax;
import minsk.codeanalysis.syntax.parser.CompilationUnitSyntax;
import minsk.codeanalysis.syntax.parser.ExpressionStatementSyntax;
import minsk.codeanalysis.syntax.parser.ExpressionSyntax;
import minsk.codeanalysis.syntax.parser.ForStatementSyntax;
import minsk.codeanalysis.syntax.parser.IfStatementSyntax;
import minsk.codeanalysis.syntax.parser.LiteralExpressionSyntax;
import minsk.codeanalysis.syntax.parser.NameExpressionSyntax;
import minsk.codeanalysis.syntax.parser.ParenthesizedExpressionSyntax;
import minsk.codeanalysis.syntax.parser.StatementSyntax;
import minsk.codeanalysis.syntax.parser.UnaryExpressionSyntax;
import minsk.codeanalysis.syntax.parser.VariableDeclarationSyntax;
import minsk.codeanalysis.syntax.parser.WhileStatementSyntax;
import minsk.diagnostics.Diagnosable;
import minsk.diagnostics.DiagnosticsCollection;

public class Binder implements Diagnosable {

	public static BoundGlobalScope bindGlobalScope(BoundGlobalScope previous, CompilationUnitSyntax syntax) {
		var parentScope = createParentScope(previous);
		var binder = new Binder(parentScope);
		var statement = binder.bindStatement(syntax.getStatement());
		var variables = binder.getScope().getDeclaredVariables();
		var diagnostics = binder.getDiagnostics();
		
		if (previous != null)
			diagnostics.addFrom(previous);
		
		return new BoundGlobalScope(previous, diagnostics, variables, statement);
	}
	
	private BoundStatement bindStatement(StatementSyntax syntax) {
		return switch (syntax.getKind()) {
			case BlockStatement -> bindBlockStatement((BlockStatementSyntax) syntax);
			case IfStatement -> bindIfStatement((IfStatementSyntax) syntax);
			case ForStatement -> bindForStatement((ForStatementSyntax) syntax);
			case WhileStatement -> bindWhileStatement((WhileStatementSyntax) syntax);
			case VariableDeclaration -> bindVariableDeclaration((VariableDeclarationSyntax) syntax);
			case ExpressionStatement -> bindExpressionStatement((ExpressionStatementSyntax) syntax);
			default -> throw new RuntimeException("Unexpected statement syntax: " + syntax.getKind());
		};
	}

	private BoundForStatement bindForStatement(ForStatementSyntax syntax) {
		var lowerBound = bindExpression(syntax.getLowerBound(), Integer.class);
		var upperBound = bindExpression(syntax.getUpperBound(), Integer.class);
		
		scope = new BoundScope(scope);

		var name = syntax.getIdentifier().getText();
		var variable = new VariableSymbol(name, true, Integer.class);
		
		if (!scope.declare(variable))
			diagnostics.reportVariableAlreadyDeclared(syntax.getIdentifier().getSpan(), name);
		
		var body = bindStatement(syntax.getBody());
		
		scope = scope.getParent();
			
		return new BoundForStatement(variable, lowerBound, upperBound, body);
	}

	private BoundWhileStatement bindWhileStatement(WhileStatementSyntax syntax) {
		var condition = bindExpression(syntax.getCondition(), Boolean.class);
		var body = bindStatement(syntax.getBody());
		
		return new BoundWhileStatement(condition, body);
	}

	private BoundBlockStatement bindBlockStatement(BlockStatementSyntax syntax) {
		scope = new BoundScope(scope);
		
		var statements = syntax.getStatements().stream()
				.map(this::bindStatement)
				.collect(Collectors.toList()); 
		
		scope = scope.getParent();
		
		return new BoundBlockStatement(statements);
	}

	private BoundIfStatement bindIfStatement(IfStatementSyntax syntax) {
		var condition = bindExpression(syntax.getCondition(), Boolean.class);
		var thenStatement = bindStatement(syntax.getThenStatement());
		var elseClause = syntax.getElseClause() == null ? null : bindStatement(syntax.getElseClause().getElseStatement());
		
		return new BoundIfStatement(condition, thenStatement, elseClause);
	}

	private BoundStatement bindVariableDeclaration(VariableDeclarationSyntax syntax) {
		var name = syntax.getIdentifier().getText();
		var isReadOnly = syntax.getKeyword().getKind() == SyntaxKind.LetKeyword;
		var initializer = bindExpression(syntax.getInitializer());
		var variable = new VariableSymbol(name, isReadOnly, initializer.getType());
		
		if (!scope.declare(variable)) {
			diagnostics.reportVariableAlreadyDeclared(syntax.getIdentifier().getSpan(), name);
		}
		
		return new BoundVariableDeclaration(variable, initializer);
	}
	
	private BoundExpressionStatement bindExpressionStatement(ExpressionStatementSyntax syntax) {
		var expression = bindExpression(syntax.getExpression());
		return new BoundExpressionStatement(expression);
	}

	private static BoundScope createParentScope(BoundGlobalScope previous) {
		var stack = new LinkedList<BoundGlobalScope>();

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

	private final DiagnosticsCollection diagnostics = new DiagnosticsCollection();
	private BoundScope scope;

	public Binder(BoundScope parent) {
		this.scope = new BoundScope(parent);
	}
	
	public BoundExpression bindExpression(ExpressionSyntax syntax, Class<?> targetType) {
		var bound = bindExpression(syntax);
		
		if (bound.getType() != targetType) {
			diagnostics.reportCannotConvert(syntax.getSpan(), bound.getType(), targetType);
		}
		
		return bound;
	}

	public BoundExpression bindExpression(ExpressionSyntax syntax) {
		return switch (syntax.getKind()) {
			case ParenthesizedExpression -> bindParenthesizedExpression(((ParenthesizedExpressionSyntax)syntax));
			case LiteralExpression -> bindLiteralExpression((LiteralExpressionSyntax) syntax);
			case NameExpression -> bindNameExpression((NameExpressionSyntax)syntax);
			case AssignmentExpression -> bindAssignmentExpression((AssignmentExpressionSyntax)syntax);
			case UnaryExpression -> bindUnaryExpression((UnaryExpressionSyntax) syntax);
			case BinaryExpression -> bindBinaryExpression((BinaryExpressionSyntax) syntax);
			default -> throw new RuntimeException("Unexpected syntax " + syntax.getKind());
		};
	}

	private BoundExpression bindNameExpression(NameExpressionSyntax syntax) {
		var name = syntax.getIdentifierToken().getText();
		
		// Error already reported during parsing, just fall through
		if (name == null) {
			return new BoundLiteralExpression(0);
		}
		
		var variable = scope.lookup(name);
		
		if (variable == null) {
			diagnostics.reportUndefinedName(syntax.getIdentifierToken().getSpan(), name);
			return new BoundLiteralExpression(0);
		}
		
		return new BoundVariableExpression(variable);
	}
	
	private BoundExpression bindAssignmentExpression(AssignmentExpressionSyntax syntax) {
		var name = syntax.getIdentifierToken().getText();
		var boundExpression = bindExpression(syntax.getExpression());
		
		VariableSymbol variable = scope.lookup(name);
		
		if (variable == null) {
			diagnostics.reportUndefinedName(syntax.getIdentifierToken().getSpan(), name);
			return boundExpression;
		}
		
		if (variable.isReadOnly()) {
			diagnostics.reportCannotAssign(syntax.getEqualsToken().getSpan(), name);
		}

		if (boundExpression.getType() != variable.getType()) {
			diagnostics.reportCannotConvert(syntax.getExpression().getSpan(), boundExpression.getType(), variable.getType());
		}
		
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
	public DiagnosticsCollection getDiagnostics() {
		return diagnostics;
	}
	
	public BoundScope getScope() {
		return scope;
	}
}
