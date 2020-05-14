package minsk.codeanalysis.syntax;

import java.util.function.Function;

import minsk.diagnostics.*;

public class Lexer implements Diagnosable {
	public static final char EOF = '\0';
	
	private final Diagnostics diagnostics = new Diagnostics();
	
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
		
		if (match(Character::isDigit)) {
			var start = position;
			
			while (match(Character::isDigit)) {
				next();
			}
			
			var t = text.substring(start, position);
			// TODO: Handle parseInt exceptions in a useful way
			return SyntaxKind.LiteralToken.newToken(start, t, Integer.parseInt(t));
		}
		
		if (match(Character::isWhitespace)) {
			var start = position;
			while (match(Character::isWhitespace)) {
				next();
			}
			
			var t = text.substring(start, position);
			
			return SyntaxKind.WhitespaceToken.newToken(position, t, t);
		}
		
		if (match(Character::isLetter)) {
			var start = position;
			
			while (match(Character::isLetter)) {
				next();
			}
			
			var t = text.substring(start, position);
			var kind = SyntaxFacts.getKeywordKind(t);
			
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
			return SyntaxKind.BangToken.newToken(position++, "!", null);
		case '&':
			if (lookahead() == '&') {
				return SyntaxKind.AmpersandAmpersandToken.newToken(position += 2, "&&", null);
			}
			break;
		case '|':
			if (lookahead() == '|') {
				return SyntaxKind.PipePipeToken.newToken(position += 2, "||", null);
			}
		}
		
		getDiagnostics().add("ERROR: Bad character input: '" + current() + "'");
		position++;
		return SyntaxKind.BadToken.newToken(position - 1, text.substring(position - 1, position), null);	
	}

	public Diagnostics getDiagnostics() {
		return diagnostics;
	}
}