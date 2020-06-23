package minsk.codeanalysis.syntax.lexer;

import minsk.codeanalysis.syntax.SyntaxFacts;
import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.text.SourceText;
import minsk.codeanalysis.text.TextSpan;
import minsk.diagnostics.Diagnosable;
import minsk.diagnostics.DiagnosticsCollection;

public class Lexer implements Diagnosable {
	public static final char EOF = '\0';
	
	private final DiagnosticsCollection diagnostics = new DiagnosticsCollection();
	
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
		case '~':
			kind = SyntaxKind.TildeToken;
			position++;
			break;
		case '^':
			kind = SyntaxKind.CaretToken;
			position++;
			break;
		case ',':
			kind = SyntaxKind.CommaToken;
			position++;
			break;
		case ':':
			kind = SyntaxKind.ColonToken;
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
			else {
				kind = SyntaxKind.AmpersandToken;
			}
			break;
		case '|':
			position++;
			if (current() == '|') {
				kind = SyntaxKind.PipePipeToken;
				position++;
			} 
			else {
				kind = SyntaxKind.PipeToken;
			}
			break;
		case '=':
			position++;
			if (current() == '=') {
				kind = SyntaxKind.EqualsEqualsToken;
				position++;
			} 
			else {
				kind = SyntaxKind.EqualsToken;
			}
			break;
		case '>':
			position++;
			if (current() == '=') {
				kind = SyntaxKind.GreaterEqualsToken;
				position++;
			} 
			else {
				kind = SyntaxKind.GreaterToken;
			}
			break;
		case '<':
			position++;
			if (current() == '=') {
				kind = SyntaxKind.LessEqualsToken;
				position++;
			} 
			else {
				kind = SyntaxKind.LessToken;
			}
			break;
		case '"':
			kind = readString();
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
			} 
			else {
				diagnostics.reportBadCharacter(position, current());
				position++;
			}
		}

		if (start == position) {
			throw new RuntimeException("Lexer failed to consume character: " + source.charAt(position));
		}

		var text = kind.getFixedText();

		if (text == null && start < source.length() && position <= source.length()) {
			text = source.substring(start, position);
		}

		return new SyntaxToken(kind, start, text, value);
	}

	private SyntaxKind readString() {
		var sb = new StringBuilder();
		var done = false;
		var start = position;
		
		position++; // opening quote

		while (!done) {
			switch (current()) {
				case EOF:
				case '\n':
				case '\r':
					diagnostics.reportUnterminatedString(new TextSpan(start, position));
					done = true;
					break;
				case '"':
					position++;
					if (current() == '"') {
						sb.append(current());
						position++;
					}
					else {
						done = true;
					}
					break;
				default:
					sb.append(current());
					position++;
			}
		}

		value = sb.toString();
		return SyntaxKind.StringToken;
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
		}
		catch (NumberFormatException e) {
			diagnostics.reportInvalidNumber(new TextSpan(start, position), source, Integer.class);
		}
		
		return SyntaxKind.LiteralToken;
	}

	public DiagnosticsCollection getDiagnostics() {
		return diagnostics;
	}
}