package minsk.codeanalysis.syntax;

import java.util.ArrayList;
import java.util.List;

import minsk.diagnostics.Diagnosable;
import minsk.diagnostics.DiagnosticsBag;

public class Parser implements Diagnosable {

	private final DiagnosticsBag diagnostics = new DiagnosticsBag();
	private final List<SyntaxToken> tokens;

	private int position = 0;

	public Parser(String text) {
		tokens = new ArrayList<SyntaxToken>();
		var lexer = new Lexer(text);

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

	private SyntaxToken match(SyntaxKind kind) {
		if (current().getKind() == kind) {
			return nextToken();
		}

		getDiagnostics().reportUnexpectedToken(current().getSpan(), current().getKind(), kind);
		return new SyntaxToken(kind, current().getPosition(), null, null);
	}

	public SyntaxTree parse() {
		var expression = parseExpression();
		var endOfFileToken = match(SyntaxKind.EndOfFileToken);

		return new SyntaxTree(getDiagnostics(), expression, endOfFileToken);
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

	// TODO: Naming??
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
			var left = nextToken();
			var expression = parseAssignmentExpression();
			var right = match(SyntaxKind.CloseParenthesisToken);
			return new ParenthesizedExpressionSyntax(left, expression, right);
		case TrueKeyword:
		case FalseKeyword:
			var keywordToken = nextToken();
			var value = keywordToken.getKind() == SyntaxKind.TrueKeyword;
			return new LiteralExpressionSyntax(keywordToken, value);
		case IdentifierToken:
			var identifierToken = nextToken();
			return new NameExpressionSyntax(identifierToken);
		default:
			var numberToken = match(SyntaxKind.LiteralToken);
			return new LiteralExpressionSyntax(numberToken);
		}
	}

	public List<SyntaxToken> getTokens() {
		return tokens;
	}

	public DiagnosticsBag getDiagnostics() {
		return diagnostics;
	}
}