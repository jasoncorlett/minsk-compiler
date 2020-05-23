package minsk.codeanalysis.syntax;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import minsk.codeanalysis.text.SourceText;
import minsk.diagnostics.*;

public class SyntaxTree implements Diagnosable {
	private final SourceText source;
	private final CompilationUnitSyntax root;
	private final DiagnosticsBag diagnostics;
	
	private SyntaxTree(SourceText source) {
		var parser = new Parser(source);
		var root = parser.parseCompilationUnit();
		var diagnostics = parser.getDiagnostics();
		
		this.source = source;
		this.diagnostics = diagnostics;
		this.root = root;
	}

	public SourceText getSource() {
		return source;
	}

	public CompilationUnitSyntax getRoot() {
		return root;
	}

	public DiagnosticsBag getDiagnostics() {
		return diagnostics;
	}
	
	public static SyntaxTree parse(SourceText source) {
		return new SyntaxTree(source);
	}
	
	public static SyntaxTree parse(String text) {
		return parse(SourceText.from(text));
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
}