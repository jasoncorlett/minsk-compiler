package minsk.codeanalysis.binding;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Collectors;

import minsk.codeanalysis.symbols.BuiltinFunctions;
import minsk.codeanalysis.symbols.TypeSymbol;
import minsk.codeanalysis.symbols.VariableSymbol;
import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;
import minsk.codeanalysis.syntax.parser.AssignmentExpressionSyntax;
import minsk.codeanalysis.syntax.parser.BinaryExpressionSyntax;
import minsk.codeanalysis.syntax.parser.BlockStatementSyntax;
import minsk.codeanalysis.syntax.parser.CallExpressionSyntax;
import minsk.codeanalysis.syntax.parser.CompilationUnitSyntax;
import minsk.codeanalysis.syntax.parser.DoStatementSyntax;
import minsk.codeanalysis.syntax.parser.ExpressionStatementSyntax;
import minsk.codeanalysis.syntax.parser.ExpressionSyntax;
import minsk.codeanalysis.syntax.parser.ForStatementSyntax;
import minsk.codeanalysis.syntax.parser.IfStatementSyntax;
import minsk.codeanalysis.syntax.parser.LiteralExpressionSyntax;
import minsk.codeanalysis.syntax.parser.NameExpressionSyntax;
import minsk.codeanalysis.syntax.parser.ParenthesizedExpressionSyntax;
import minsk.codeanalysis.syntax.parser.StatementSyntax;
import minsk.codeanalysis.syntax.parser.UnaryExpressionSyntax;
import minsk.codeanalysis.syntax.parser.UntilStatementSyntax;
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
			case UntilStatement -> bindUntilStatement((UntilStatementSyntax) syntax);
			case DoStatement -> bindDoStatement((DoStatementSyntax) syntax);
			case VariableDeclaration -> bindVariableDeclaration((VariableDeclarationSyntax) syntax);
			case ExpressionStatement -> bindExpressionStatement((ExpressionStatementSyntax) syntax);
			default -> throw new RuntimeException("Unexpected statement syntax: " + syntax.getKind());
		};
	}

	private BoundDoStatement bindDoStatement(DoStatementSyntax syntax) {
		var body = bindStatement(syntax.getBody());
		var continueWhen = syntax.getLoopKeyword().getKind() == SyntaxKind.WhileKeyword;
		var condition = bindExpression(syntax.getCondition());

		return new BoundDoStatement(body, continueWhen, condition);
	}

	private BoundUntilStatement bindUntilStatement(UntilStatementSyntax syntax) {
		var condition = bindExpression(syntax.getCondition(), TypeSymbol.Bool);
		var body = bindStatement(syntax.getBody());

		return new BoundUntilStatement(condition, body);
	}

	private BoundForStatement bindForStatement(ForStatementSyntax syntax) {
		var lowerBound = bindExpression(syntax.getLowerBound(), TypeSymbol.Int);
		var upperBound = bindExpression(syntax.getUpperBound(), TypeSymbol.Int);
		
		scope = new BoundScope(scope);

		var variable = bindVariable(syntax.getIdentifier(), true, lowerBound.getType());
		var body = bindStatement(syntax.getBody());
		
		scope = scope.getParent();
			
		return new BoundForStatement(variable, lowerBound, upperBound, body);
	}

	private BoundWhileStatement bindWhileStatement(WhileStatementSyntax syntax) {
		var condition = bindExpression(syntax.getCondition(), TypeSymbol.Bool);
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
		var condition = bindExpression(syntax.getCondition(), TypeSymbol.Bool);
		var thenStatement = bindStatement(syntax.getThenStatement());
		var elseClause = syntax.getElseClause() == null ? null : bindStatement(syntax.getElseClause().getElseStatement());
		
		return new BoundIfStatement(condition, thenStatement, elseClause);
	}

	private BoundStatement bindVariableDeclaration(VariableDeclarationSyntax syntax) {
		var isReadOnly = syntax.getKeyword().getKind() == SyntaxKind.LetKeyword;
		var initializer = bindExpression(syntax.getInitializer());
		var variable = bindVariable(syntax.getIdentifier(), isReadOnly, initializer.getType());
		
		return new BoundVariableDeclaration(variable, initializer);
	}
	
	private BoundExpressionStatement bindExpressionStatement(ExpressionStatementSyntax syntax) {
		var expression = bindExpression(syntax.getExpression(), true);
		return new BoundExpressionStatement(expression);
	}

	private static BoundScope createParentScope(BoundGlobalScope previous) {
		var stack = new LinkedList<BoundGlobalScope>();

		while (previous != null) {
			stack.push(previous);
			previous = previous.getPrevious();
		}
		
		var parent = createRootScope();
		
		while (!stack.isEmpty()) {
			var scope = new BoundScope(parent);
			stack.pop().getVariables().forEach(scope::declareVariable);
			parent = scope;
		}
		
		return parent;
	}
	
	private static BoundScope createRootScope() {
		var result = new BoundScope(null);

		for (var fn : BuiltinFunctions.getAll()) {
			result.declareFunction(fn);
		}
		
		return result;
	}

	private final DiagnosticsCollection diagnostics = new DiagnosticsCollection();
	private BoundScope scope;

	public Binder(BoundScope parent) {
		this.scope = new BoundScope(parent);
	}
	
	public BoundExpression bindExpression(ExpressionSyntax syntax, TypeSymbol targetType) {
		var bound = bindExpression(syntax);
		
		// If an error type is present, the error has already been reported
		if (!bound.getType().equals(TypeSymbol.Error) && !targetType.equals(TypeSymbol.Error) && !bound.getType().equals(targetType)) {
			diagnostics.reportCannotConvert(syntax.getSpan(), bound.getType(), targetType);
		}
		
		return bound;
	}

	public BoundExpression bindExpression(ExpressionSyntax syntax, boolean canBeVoid) {
		var expression = bindExpressionInternal(syntax);

		if (!canBeVoid && expression.getType().equals(TypeSymbol.Void)) {
			diagnostics.reportExpressionMustHaveValue(syntax.getSpan());
			return new BoundErrorExpression();
		}

		return expression;
	}

	public BoundExpression bindExpression(ExpressionSyntax syntax) {
		return bindExpression(syntax, false);
	}

	public BoundExpression bindExpressionInternal(ExpressionSyntax syntax) {
		return switch (syntax.getKind()) {
			case ParenthesizedExpression -> bindParenthesizedExpression(((ParenthesizedExpressionSyntax)syntax));
			case LiteralExpression -> bindLiteralExpression((LiteralExpressionSyntax) syntax);
			case NameExpression -> bindNameExpression((NameExpressionSyntax)syntax);
			case AssignmentExpression -> bindAssignmentExpression((AssignmentExpressionSyntax)syntax);
			case UnaryExpression -> bindUnaryExpression((UnaryExpressionSyntax) syntax);
			case BinaryExpression -> bindBinaryExpression((BinaryExpressionSyntax) syntax);
			case CallExpression -> bindCallExpression((CallExpressionSyntax) syntax);
			default -> throw new RuntimeException("Unexpected syntax " + syntax.getKind());
		};
	}

	private BoundExpression bindCallExpression(CallExpressionSyntax syntax) {
		var name = syntax.getIdentifier().getText();
		var boundArguments = new ArrayList<BoundExpression>();

		for (var argument : syntax.getArguments()) {
			boundArguments.add(bindExpression(argument));
		}

		var function = scope.lookupFunction(name); 
		
		if (function == null) {
			diagnostics.reportUndefinedFunction(syntax.getSpan(), syntax.getIdentifier().getText());
			return new BoundErrorExpression();
		}

		if (function.getArguments().size() != boundArguments.size()) {
			diagnostics.reportWrongArgumentCount(syntax.getSpan(), function.getName(), function.getArguments().size(), boundArguments.size());
			return new BoundErrorExpression();
		}

		for (var i = 0; i < boundArguments.size(); i++) {
			var expected = function.getArguments().get(i);
			var actual = boundArguments.get(i);

			if (!expected.getType().equals(actual.getType())) {
				diagnostics.reportWrongArgumentType(syntax.getSpan(), expected.getName(), expected.getType(), actual.getType());
				return new BoundErrorExpression();
			}
		}

		return new BoundCallExpression(function, boundArguments);
	}

	private BoundExpression bindNameExpression(NameExpressionSyntax syntax) {
		var name = syntax.getIdentifierToken().getText();
		
		// Error already reported during parsing, just fall through
		if (syntax.getIdentifierToken().isMissing()) {
			return new BoundErrorExpression();
		}
		
		var variable = scope.lookupVariable(name);
		
		if (variable == null) {
			diagnostics.reportUndefinedName(syntax.getIdentifierToken().getSpan(), name);
			return new BoundErrorExpression();
		}
		
		return new BoundVariableExpression(variable);
	}
	
	private BoundExpression bindAssignmentExpression(AssignmentExpressionSyntax syntax) {
		var name = syntax.getIdentifierToken().getText();
		var boundExpression = bindExpression(syntax.getExpression());
		
		VariableSymbol variable = scope.lookupVariable(name);
		
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
		
		if (boundOperand.getType().equals(TypeSymbol.Error)) {
			return new BoundErrorExpression();
		}
		
		var boundOperator = BoundUnaryOperator.bind(syntax.getOperatorToken().getKind(), boundOperand.getType());

		if (boundOperator == null) {
			diagnostics.reportUndefinedUnaryOperator(syntax.getOperatorToken().getSpan(), syntax.getOperatorToken().getText(), boundOperand.getType());
			return new BoundErrorExpression();
		}

		return new BoundUnaryExpression(boundOperator, boundOperand);
	}

	private BoundExpression bindBinaryExpression(BinaryExpressionSyntax syntax) {
		var boundLeft = bindExpression(syntax.getLeft());
		var boundRight = bindExpression(syntax.getRight());
		
		if (boundRight.getType().equals(TypeSymbol.Error) || boundLeft.getType().equals(TypeSymbol.Error)) {
			return new BoundErrorExpression();
		}
		
		var boundOperator = BoundBinaryOperator.bind(syntax.getOperatorToken().getKind(), boundLeft.getType(), boundRight.getType());

		if (boundOperator == null) {
			diagnostics.reportUndefinedBinaryOperator(syntax.getOperatorToken().getSpan(), syntax.getOperatorToken().getText(), boundLeft.getType(), boundRight.getType());
			return new BoundErrorExpression();
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
	
	private VariableSymbol bindVariable(SyntaxToken identifier, boolean isReadOnly, TypeSymbol type) {
		var name = identifier.getText() != null ? identifier.getText() : "?";
		var declare = !identifier.isMissing();
		var variable = new VariableSymbol(name, isReadOnly, type);
		
		if (declare && !scope.declareVariable(variable)) {
			diagnostics.reportVariableAlreadyDeclared(identifier.getSpan(), name);
		}
		
		return variable;
	}
}
