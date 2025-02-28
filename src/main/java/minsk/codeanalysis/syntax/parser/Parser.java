package minsk.codeanalysis.syntax.parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.SyntaxNode;
import minsk.codeanalysis.syntax.lexer.Lexer;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;
import minsk.codeanalysis.text.SourceText;
import minsk.diagnostics.Diagnosable;
import minsk.diagnostics.DiagnosticsCollection;

public class Parser implements Diagnosable {

	private final DiagnosticsCollection diagnostics = new DiagnosticsCollection();
	private final List<SyntaxToken> tokens;

	private int position = 0;
	
	public Parser(SourceText source) {
		tokens = new ArrayList<>();
		var lexer = new Lexer(source);
		
		SyntaxToken token;
		do {
			token = lexer.lex();

			if (token.getKind() == SyntaxKind.WhitespaceToken || token.getKind() == SyntaxKind.BadToken) {
				continue;
			}

			getTokens().add(token);
		} while (token.getKind() != SyntaxKind.EndOfFileToken);

		getDiagnostics().addFrom(lexer);
	}

	private SyntaxToken nextToken() {
		var current = current();
		position++;
		return current;
	}

	private SyntaxToken peek(int offset) {
		var index = position + offset;
		if (index >= getTokens().size()) {
			return getTokens().get(getTokens().size() - 1);
		}
		else {
			return getTokens().get(index);
		}
	}

	private SyntaxToken current() {
		return peek(0);
	}

	private SyntaxToken lookahead() {
		return peek(1);
	}

	private SyntaxToken matchToken(SyntaxKind kind) {
		if (current().getKind() == kind) {
			return nextToken();
		}

		getDiagnostics().reportUnexpectedToken(current().getSpan(), current().getKind(), kind);
		return new SyntaxToken(kind, current().getPosition(), null, null);
	}

	public CompilationUnitSyntax parseCompilationUnit() {
		var expression = parseStatement();
		var endOfFileToken = matchToken(SyntaxKind.EndOfFileToken);
		
		return new CompilationUnitSyntax(expression, endOfFileToken);
	}
	
	private StatementSyntax parseStatement() {
		return switch (current().getKind()) {
			case OpenBraceToken -> parseBlockStatement();
			case LetKeyword, VarKeyword -> parseVariableDeclaration();
			case IfKeyword -> parseIfStatement();
			case WhileKeyword -> parseWhileStatement();
			case UntilKeyword -> parseUntilStatement();
			case ForKeyword -> parseForStatement();
			case DoKeyword -> parseDoStatement();
			default -> parseExpressionStatement();
		};
	}

	private BlockStatementSyntax parseBlockStatement() {
		var statements = new LinkedList<StatementSyntax>();
		
		var openBraceToken = matchToken(SyntaxKind.OpenBraceToken);
		var startPosition = position;
		
		while (current().getKind() != SyntaxKind.CloseBraceToken
				&& current().getKind() != SyntaxKind.EndOfFileToken) {
			statements.add(parseStatement());
			
			// Infinite loop protection, consume a token if we did not move
			// forward. Should already have an error from the unexpected token
			if (position == startPosition)
				nextToken();
		}
		
		var closeBraceToken = matchToken(SyntaxKind.CloseBraceToken);
		
		return new BlockStatementSyntax(openBraceToken, statements, closeBraceToken);
	}
	
	private VariableDeclarationSyntax parseVariableDeclaration() {
		var expected = current().getKind() == SyntaxKind.LetKeyword ? SyntaxKind.LetKeyword : SyntaxKind.VarKeyword;
		var keyword = matchToken(expected);
		var identifier = matchToken(SyntaxKind.IdentifierToken);
		var typeClause = parseOptionalTypeClause();
		var equals = matchToken(SyntaxKind.EqualsToken);
		var initializer = parseExpression();
		
		return new VariableDeclarationSyntax(keyword, identifier, typeClause, equals, initializer);
	}

	private TypeClauseSyntax parseOptionalTypeClause() {
		if (!current().getKind().equals(SyntaxKind.ColonToken)) {
			return null;
		}

		return parseTypeClause();
	}

	private TypeClauseSyntax parseTypeClause() {
		var colon = matchToken(SyntaxKind.ColonToken);
		var identifier = matchToken(SyntaxKind.IdentifierToken);

		return new TypeClauseSyntax(colon, identifier);
	}

	private IfStatementSyntax parseIfStatement() {
		var ifKeyword = matchToken(SyntaxKind.IfKeyword);
		var condition = parseExpression();
		var thenStatement = parseStatement();
		var elseClause = parseElseClause();
		
		return new IfStatementSyntax(ifKeyword, condition, thenStatement, elseClause);
	}
	
	private ElseClauseSyntax parseElseClause() {
		if (current().getKind() != SyntaxKind.ElseKeyword)
			return null;
		
		var keyword = nextToken();
		var statement = parseStatement();
		
		return new ElseClauseSyntax(keyword, statement);
	}
	
	private ForStatementSyntax parseForStatement() {
		var forKeyword = matchToken(SyntaxKind.ForKeyword);
		var identifier = matchToken(SyntaxKind.IdentifierToken);
		var equalsToken= matchToken(SyntaxKind.EqualsToken);
		var lowerBound = parseExpression();
		var toKeyword = matchToken(SyntaxKind.ToKeyword);
		var upperBound = parseExpression();
		var body = parseStatement();
		
		return new ForStatementSyntax(forKeyword, identifier, equalsToken, lowerBound, toKeyword, upperBound, body);
	}

	private WhileStatementSyntax parseWhileStatement() {
		var whileKeyword = matchToken(SyntaxKind.WhileKeyword);
		var condition = parseExpression();
		var body = parseStatement();
		
		return new WhileStatementSyntax(whileKeyword, condition, body);
	}
	
	private DoStatementSyntax parseDoStatement() {
		var doKeyword = matchToken(SyntaxKind.DoKeyword);
		var body = parseStatement();
		var loopKeyword = current().getKind() == SyntaxKind.WhileKeyword ? matchToken(SyntaxKind.WhileKeyword)
			: matchToken(SyntaxKind.UntilKeyword);
		var condition = parseExpression();

		return new DoStatementSyntax(doKeyword, body, loopKeyword, condition);
	}

	private UntilStatementSyntax parseUntilStatement() {
		var untilKeyword = matchToken(SyntaxKind.UntilKeyword);
		var condition = parseExpression();
		var body = parseStatement();

		return new UntilStatementSyntax(untilKeyword, condition, body);
	}

	private ExpressionStatementSyntax parseExpressionStatement() {
		return new ExpressionStatementSyntax(parseExpression());
	}

	private ExpressionSyntax parseExpression() {
		return parseAssignmentExpression();
	}

	private ExpressionSyntax parseAssignmentExpression() {
		if (current().getKind() == SyntaxKind.IdentifierToken && lookahead().getKind() == SyntaxKind.EqualsToken) {
			var identifierToken = nextToken();
			var operatorToken = nextToken();
			var right = parseAssignmentExpression();
			return new AssignmentExpressionSyntax(identifierToken, operatorToken, right);
		}
		
		return parseBinaryExpression();
	}

	private ExpressionSyntax parseBinaryExpression() {
		return parseBinaryExpression(0);
	}

	private ExpressionSyntax parseBinaryExpression(int parentPrecedence) {
		ExpressionSyntax left;

		var unaryOperatorPrecedence = current().getKind().getUnaryPrecedence();

		if (unaryOperatorPrecedence != 0 && unaryOperatorPrecedence >= parentPrecedence) {
			var operatorToken = nextToken();
			var operand = parseBinaryExpression(unaryOperatorPrecedence);
			left = new UnaryExpressionSyntax(operatorToken, operand);
		} 
		else {
			left = parsePrimaryExpression();
		}

		while (true) {
			var precedence = current().getKind().getBinaryPrecedence();
			if (precedence == 0 || precedence <= parentPrecedence) {
				break;
			}

			var operatorToken = nextToken();
			var right = parseBinaryExpression(precedence);

			left = new BinaryExpressionSyntax(left, operatorToken, right);
		}

		return left;
	}

	private ExpressionSyntax parsePrimaryExpression() {
		switch (current().getKind()) {
		case OpenParenthesisToken:
			return parseParenthesizedExpression();
			
		case TrueKeyword:
		case FalseKeyword:
			return parseBooleanLiteral();

		case LiteralToken:
			return parseLiteralExpression();

		case StringToken:
			return parseStringLiteral();
			
		case IdentifierToken:
		default:
			return parseNameOrCallExpression();
		}
	}

	private ExpressionSyntax parseParenthesizedExpression() {
		var left = matchToken(SyntaxKind.OpenParenthesisToken);
		var expression = parseAssignmentExpression();
		var right = matchToken(SyntaxKind.CloseParenthesisToken);
		return new ParenthesizedExpressionSyntax(left, expression, right);
	}

	private ExpressionSyntax parseBooleanLiteral() {
		var isTrue = current().getKind() == SyntaxKind.TrueKeyword;
		
		var keywordToken = isTrue ? matchToken(SyntaxKind.TrueKeyword) : matchToken(SyntaxKind.FalseKeyword);
		
		return new LiteralExpressionSyntax(keywordToken, isTrue);
	}

	private ExpressionSyntax parseNameOrCallExpression() {
		if (current().getKind() == SyntaxKind.IdentifierToken && lookahead().getKind() == SyntaxKind.OpenParenthesisToken) {
			return parseCallExpression();
		}
		
		return parseNameExpression();
	}
	
	private ExpressionSyntax parseCallExpression() {
		var identifier = matchToken(SyntaxKind.IdentifierToken);
		var openParenthesisToken = matchToken(SyntaxKind.OpenParenthesisToken);
		var arguments = parseArguments();
		var closeParenthesisToken = matchToken(SyntaxKind.CloseParenthesisToken);
		
		return new CallExpressionSyntax(identifier, openParenthesisToken, arguments, closeParenthesisToken);
	}

	private SeparatedSyntaxList<ExpressionSyntax> parseArguments() {
		var nodesAndSeparators = new ArrayList<SyntaxNode>();
		
		while (current().getKind() != SyntaxKind.CloseParenthesisToken && current().getKind() != SyntaxKind.EndOfFileToken) {
			var expression = parseExpression();
			nodesAndSeparators.add(expression);
			
			if (current().getKind() != SyntaxKind.CloseParenthesisToken) {
				var comma = matchToken(SyntaxKind.CommaToken);
				nodesAndSeparators.add(comma);
			}			
		}

		return new SeparatedSyntaxList<>(nodesAndSeparators);
	}
	
	private ExpressionSyntax parseNameExpression() {
		var identifierToken = matchToken(SyntaxKind.IdentifierToken);
		return new NameExpressionSyntax(identifierToken);
	}

	private ExpressionSyntax parseLiteralExpression() {
		var numberToken = matchToken(SyntaxKind.LiteralToken);
		return new LiteralExpressionSyntax(numberToken);
	}


	private ExpressionSyntax parseStringLiteral() {
		var stringToken = matchToken(SyntaxKind.StringToken);
		return new LiteralExpressionSyntax(stringToken);
	}

	public List<SyntaxToken> getTokens() {
		return tokens;
	}

	public DiagnosticsCollection getDiagnostics() {
		return diagnostics;
	}
}