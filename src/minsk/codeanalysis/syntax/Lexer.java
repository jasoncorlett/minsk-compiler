package minsk.codeanalysis.syntax;

import java.util.function.Function;

import minsk.codeanalysis.TextSpan;
import minsk.diagnostics.*;

public class Lexer implements Diagnosable {
	public static final char EOF = '\0';
	
	private final DiagnosticsBag diagnostics = new DiagnosticsBag();
	
	private final String text;
	private int position;
	
	public Lexer(String text) {
		this.text = text;
	}

	private char current() {
		return peek(0);
	}
	
	private char lookahead() {
		return peek(1);
	}
	
	private char peek(int offset) {
		var index = position + offset;
		
		if (index >= text.length()) {
			return EOF;
		}
		
		return text.charAt(index);
	}
	
	private boolean match(Function<Character, Boolean> fn) {
		return fn.apply(current());
	}
	
	private void next() {
		position++;
	}
	
	public SyntaxToken lex() {
		if (position >= text.length()) {
			return SyntaxKind.EndOfFileToken.newToken(position, "" + EOF, null);
		}
		
		final var start = position;
		
		if (match(Character::isDigit)) {
			while (match(Character::isDigit)) {
				next();
			}
			
			var t = text.substring(start, position);
			Integer value = null;
			
			try {
				value = Integer.parseInt(t);
			} catch (NumberFormatException e) {
				diagnostics.reportInvalidNumber(new TextSpan(start, position), text, Integer.class);
			}
			
			return SyntaxKind.LiteralToken.newToken(start, t, value);
		}
		
		if (match(Character::isWhitespace)) {
			while (match(Character::isWhitespace)) {
				next();
			}
			
			var t = text.substring(start, position);
			
			return SyntaxKind.WhitespaceToken.newToken(position, t, t);
		}
		
		if (match(Character::isLetter)) {
			while (match(Character::isLetter)) {
				next();
			}
			
			var t = text.substring(start, position);
			var kind = SyntaxFacts.lookupKeywordKind(t);
			
			return kind.newToken(start, t, null);
		}
		
		switch (current()) {
		case '+':
			return SyntaxKind.PlusToken.newToken(position++, "+", null);
		case '-':
			return SyntaxKind.MinusToken.newToken(position++, "-", null);
		case '*':
			return SyntaxKind.StarToken.newToken(position++, "*", null);
		case '/':
			return SyntaxKind.SlashToken.newToken(position++, "/", null);
		case '(':
			return SyntaxKind.OpenParenthesisToken.newToken(position++, "(", null);
		case ')':
			return SyntaxKind.CloseParenthesisToken.newToken(position++, ")", null);
		case '!':
			if (lookahead() == '=') {
				position += 2;
				return SyntaxKind.BangEqualsToken.newToken(start, "!=", null);
			}
			return SyntaxKind.BangToken.newToken(position++, "!", null);
		case '&':
			if (lookahead() == '&') {
				position += 2;
				return SyntaxKind.AmpersandAmpersandToken.newToken(start, "&&", null);
			}
			break;
		case '|':
			if (lookahead() == '|') {
				position += 2;
				return SyntaxKind.PipePipeToken.newToken(start, "||", null);
			}
			break;
		case '=':
			if (lookahead() == '=') {
				position += 2;
				return SyntaxKind.EqualsEqualsToken.newToken(start, "==", null);
			}
			break;
		}
		
		diagnostics.reportBadCharacter(position, current());
		position++;
		return SyntaxKind.BadToken.newToken(position - 1, text.substring(position - 1, position), null);	
	}

	public DiagnosticsBag getDiagnostics() {
		return diagnostics;
	}
}