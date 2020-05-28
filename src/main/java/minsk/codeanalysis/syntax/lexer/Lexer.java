package minsk.codeanalysis.syntax.lexer;

import minsk.codeanalysis.syntax.SyntaxFacts;
import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.text.SourceText;
import minsk.codeanalysis.text.TextSpan;
import minsk.diagnostics.Diagnosable;
import minsk.diagnostics.DiagnosticsBag;

public class Lexer implements Diagnosable {
	public static final char EOF = '\0';
	
	private final DiagnosticsBag diagnostics = new DiagnosticsBag();
	
	private final SourceText source;
	
	private int position;
	private Object value;
	
	public Lexer(SourceText source) {
		this.source = source;
	}

	private char current() {
		if (position >= source.length())
			return EOF;
		return source.charAt(position);
	}
	
	public SyntaxToken lex() {
		final var start = position;
		SyntaxKind kind = SyntaxKind.BadToken;
		value = null;
		
		switch (current()) {
		case EOF:
			kind = SyntaxKind.EndOfFileToken;
			position++;
			break;
		case '+':
			kind = SyntaxKind.PlusToken;
			position++;
			break;
		case '-':
			kind = SyntaxKind.MinusToken;
			position++;
			break;
		case '*':
			kind = SyntaxKind.StarToken;
			position++;
			break;
		case '/':
			kind = SyntaxKind.SlashToken;
			position++;
			break;
		case '%':
			kind = SyntaxKind.PercentToken;
			position++;
			break;
		case '(':
			kind = SyntaxKind.OpenParenthesisToken;
			position++;
			break;
		case ')':
			kind = SyntaxKind.CloseParenthesisToken;
			position++;
			break;
		case '{':
			kind = SyntaxKind.OpenBraceToken;
			position++;
			break;
		case '}':
			kind = SyntaxKind.CloseBraceToken;
			position++;
			break;
		case '!':
			position++;
			if (current() == '=') {
				kind = SyntaxKind.BangEqualsToken;
				position++;
			} else {
				kind = SyntaxKind.BangToken;
			}
			break;
		case '&':
			position++;
			if (current() == '&') {
				kind = SyntaxKind.AmpersandAmpersandToken;
				position++;
			}
			break;
		case '|':
			position++;
			if (current() == '|') {
				kind = SyntaxKind.PipePipeToken;
				position++;
			}
			break;
		case '=':
			position++;
			if (current() == '=') {
				kind = SyntaxKind.EqualsEqualsToken;
				position++;
			} else {
				kind = SyntaxKind.EqualsToken;
			}
			break;
		default:
			if (Character.isDigit(current())) {
				kind = readNumber(start);
				break;
			}
			else if (Character.isWhitespace(current())) {
				kind = readWhitespace();
				break;
			}
			else if (Character.isLetter(current())) {
				kind = readIdentifierOrKeyword(start);
				break;
			} else {
				diagnostics.reportBadCharacter(position, current());
				position++;
			}
		}

		var text = kind.getFixedText();
		
		if (text == null && start < source.length() && position <= source.length()) {
			text = source.substring(start, position);
		}
		
		return new SyntaxToken(kind, start, text, value);
	}

	private SyntaxKind readIdentifierOrKeyword(final int start) {
		while (Character.isLetter(current())) {
			position++;
		}
		
		return SyntaxFacts.lookupKeywordKind(source.substring(start, position));
	}

	private SyntaxKind readWhitespace() {
		while (Character.isWhitespace(current())) {
			position++;
		}
		
		return SyntaxKind.WhitespaceToken;
	}

	private SyntaxKind readNumber(final int start) {
		while (Character.isDigit(current())) {
			position++;
		}
		
		var text = source.substring(start, position);
		
		try {
			value = Integer.parseInt(text);
		} catch (NumberFormatException e) {
			diagnostics.reportInvalidNumber(new TextSpan(start, position), source, Integer.class);
		}
		
		return SyntaxKind.LiteralToken;
	}

	public DiagnosticsBag getDiagnostics() {
		return diagnostics;
	}
}