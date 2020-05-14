package minsk.codeanalysis;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
	public static final char EOF = '\0';
	
	private final List<String> diagnostics = new ArrayList<>();
	
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
	
	public SyntaxToken nextToken() {
		if (position >= text.length()) {
			return new SyntaxToken(SyntaxKind.EndOfFileToken, position, "" + EOF, null);
		}
		
		else if (Character.isDigit(current())) {
			var start = position;
			while (Character.isDigit(current())) {
				position++;
			}
			
			var t = text.substring(start, position);
			// TODO: Handle parseInt exceptions in a useful way
			return new SyntaxToken(SyntaxKind.NumberToken, start, t, Integer.parseInt(t));
		}
		
		else if (Character.isWhitespace(current())) {
			var start = position;
			while (Character.isWhitespace(current())) {
				position++;
			}
			
			var t = text.substring(start, position);
			
			return new SyntaxToken(SyntaxKind.WhitespaceToken, start, t, t);
		}
		
		else if (current() == '+') {
			return new SyntaxToken(SyntaxKind.PlusToken, position++, "+", null);
		}
		
		else if (current() == '-') {
			return new SyntaxToken(SyntaxKind.MinusToken, position++, "-", null);
		}
		
		else if (current() == '*') {
			return new SyntaxToken(SyntaxKind.StarToken, position++, "*", null);
		}
		
		else if (current() == '/') {
			return new SyntaxToken(SyntaxKind.SlashToken, position++, "/", null);
		}
		
		else if (current() == '(') {
			return new SyntaxToken(SyntaxKind.OpenParenthesisToken, position++, "(", null);
		}
		
		else if (current() == ')') {
			return new SyntaxToken(SyntaxKind.CloseParenthesisToken, position++, ")", null);
		}
		
		getDiagnostics().add("ERROR: Bad character input: '" + current() + "'");
		position++;
		return new SyntaxToken(SyntaxKind.BadToken, position -1, text.substring(position - 1, position), null);	
	}

	public List<String> getDiagnostics() {
		return diagnostics;
	}
}