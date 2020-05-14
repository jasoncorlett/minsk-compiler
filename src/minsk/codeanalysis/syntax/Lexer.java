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
		if (position >= text.length()) {
			return EOF;
		}
		return text.charAt(position);
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
		}
		
		getDiagnostics().add("ERROR: Bad character input: '" + current() + "'");
		position++;
		return SyntaxKind.BadToken.newToken(position - 1, text.substring(position - 1, position), null);	
	}

	public Diagnostics getDiagnostics() {
		return diagnostics;
	}
}