package minsk.codeanalysis.syntax;

import java.util.ArrayList;
import java.util.List;

import minsk.codeanalysis.text.SourceText;
import minsk.diagnostics.Diagnosable;
import minsk.diagnostics.DiagnosticsBag;

public class Parser implements Diagnosable {

	private final DiagnosticsBag diagnostics = new DiagnosticsBag();
	private final List<SyntaxToken> tokens;
	private final SourceText source;

	private int position = 0;
	
	public Parser(SourceText source) {
		this.source = source;
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
		} else {
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

	public SyntaxTree parse() {
		var expression = parseExpression();
		var endOfFileToken = matchToken(SyntaxKind.EndOfFileToken);

		return new SyntaxTree(source, getDiagnostics(), expression, endOfFileToken);
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

		var unaryOperatorPrecedence = SyntaxFacts.lookupUnaryOperatorPrecedence(current().getKind());

		if (unaryOperatorPrecedence != 0 && unaryOperatorPrecedence >= parentPrecedence) {
			var operatorToken = nextToken();
			var operand = parseBinaryExpression(unaryOperatorPrecedence);
			left = new UnaryExpressionSyntax(operatorToken, operand);
		} else {
			left = parsePrimaryExpression();
		}

		while (true) {
			var precedence = SyntaxFacts.lookupBinaryOperatorPrecedence(current().getKind());
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
			
		case IdentifierToken:
		default:
			return parseNameExpression();
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

	private ExpressionSyntax parseNameExpression() {
		var identifierToken = matchToken(SyntaxKind.IdentifierToken);
		return new NameExpressionSyntax(identifierToken);
	}

	private ExpressionSyntax parseLiteralExpression() {
		var numberToken = matchToken(SyntaxKind.LiteralToken);
		return new LiteralExpressionSyntax(numberToken);
	}

	public List<SyntaxToken> getTokens() {
		return tokens;
	}

	public DiagnosticsBag getDiagnostics() {
		return diagnostics;
	}
}