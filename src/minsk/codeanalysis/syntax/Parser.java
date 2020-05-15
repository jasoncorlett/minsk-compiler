package minsk.codeanalysis.syntax;

import java.util.ArrayList;
import java.util.List;

import minsk.diagnostics.Diagnosable;
import minsk.diagnostics.Diagnostics;

public class Parser implements Diagnosable {

	private final Diagnostics diagnostics = new Diagnostics();
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

		getDiagnostics().addAll(lexer.getDiagnostics());
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

	private SyntaxToken match(SyntaxKind kind) {
		if (current().getKind() == kind) {
			return nextToken();
		}

		getDiagnostics().add("ERROR: Unexpected token: " + current().getKind() + " expected " + kind);
		return new SyntaxToken(kind, current().getPosition(), null, null);
	}

	public SyntaxTree parse() {
		var expression = parseExpression();
		var endOfFileToken = match(SyntaxKind.EndOfFileToken);

		return new SyntaxTree(getDiagnostics(), expression, endOfFileToken);
	}

	private ExpressionSyntax parseExpression() {
		return parseExpression(0);
	}

	private ExpressionSyntax parseExpression(int parentPrecedence) {
		ExpressionSyntax left;

		var unaryOperatorPrecedence = SyntaxFacts.lookupUnaryOperatorPrecedence(current().getKind());

		if (unaryOperatorPrecedence != 0 && unaryOperatorPrecedence >= parentPrecedence) {
			var operatorToken = nextToken();
			var operand = parseExpression(unaryOperatorPrecedence);
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
			var right = parseExpression(precedence);

			left = new BinaryExpressionSyntax(left, operatorToken, right);
		}

		return left;
	}

	private ExpressionSyntax parsePrimaryExpression() {
		switch (current().getKind()) {
		case OpenParenthesisToken:
			var left = nextToken();
			var expression = parseExpression();
			var right = match(SyntaxKind.CloseParenthesisToken);
			return new ParenthesizedExpressionSyntax(left, expression, right);
		case TrueKeyword:
		case FalseKeyword:
			var keywordToken = nextToken();
			var value = keywordToken.getKind() == SyntaxKind.TrueKeyword;
			return new LiteralExpressionSyntax(keywordToken, value);
		default:
			var numberToken = match(SyntaxKind.LiteralToken);
			return new LiteralExpressionSyntax(numberToken);
		}
	}

	public List<SyntaxToken> getTokens() {
		return tokens;
	}

	public Diagnostics getDiagnostics() {
		return diagnostics;
	}
}