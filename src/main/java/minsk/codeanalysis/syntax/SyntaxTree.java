package minsk.codeanalysis.syntax;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import minsk.diagnostics.*;

/**
 * Helper stuff for parsing
 * 
 * @author Jason Corlett
 *
 */
public class SyntaxTree implements Diagnosable {
	private final ExpressionSyntax root;
	private final SyntaxToken endOfFileToken;
	private final DiagnosticsBag diagnostics;
	
	public static SyntaxTree parse(String text) {
		var parser = new Parser(text);
		return parser.parse();
	}
	
	public static List<SyntaxToken> parseTokens(String text) {
		var lexer = new Lexer(text);
		return Stream
				.generate(lexer::lex)
				.takeWhile(t -> t.getKind() != SyntaxKind.EndOfFileToken)
				.collect(Collectors.toList());
		}

	public SyntaxTree(DiagnosticsBag diagnostics, ExpressionSyntax root, SyntaxToken endOfFileToken) {
		this.diagnostics = diagnostics;
		this.root = root;
		this.endOfFileToken = endOfFileToken;
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