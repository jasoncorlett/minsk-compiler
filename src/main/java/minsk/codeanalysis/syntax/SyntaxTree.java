package minsk.codeanalysis.syntax;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import minsk.codeanalysis.text.SourceText;
import minsk.diagnostics.*;

/**
 * Helper stuff for parsing
 * 
 * @author Jason Corlett
 *
 */
public class SyntaxTree implements Diagnosable {
	private final SourceText source;
	private final ExpressionSyntax root;
	private final DiagnosticsBag diagnostics;
	private final SyntaxToken endOfFileToken;
	
	public static SyntaxTree parse(SourceText source) {
		var parser = new Parser(source);
		return parser.parse();
	}
	
	public static SyntaxTree parse(String text) {
		var source = SourceText.from(text);
		return parse(source);
	}
	
	public static List<SyntaxToken> parseTokens(String text) {
		var source = SourceText.from(text);
		return parseTokens(source);
	}
	
	public static List<SyntaxToken> parseTokens(SourceText source) {
		var lexer = new Lexer(source);
		return Stream
				.generate(lexer::lex)
				.takeWhile(t -> t.getKind() != SyntaxKind.EndOfFileToken)
				.collect(Collectors.toList());
		}

	public SyntaxTree(SourceText source, DiagnosticsBag diagnostics, ExpressionSyntax root, SyntaxToken endOfFileToken) {
		this.source = source;
		this.diagnostics = diagnostics;
		this.root = root;
		this.endOfFileToken = endOfFileToken;
	}

	/**
	 * @return the source
	 */
	public SourceText getSource() {
		return source;
	}

	public ExpressionSyntax getRoot() {
		return root;
	}

	public SyntaxToken getEndOfFileToken() {
		return endOfFileToken;
	}

	public DiagnosticsBag getDiagnostics() {
		return diagnostics;
	}
}