package minsk.codeanalysis.syntax;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import minsk.codeanalysis.syntax.lexer.Lexer;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;
import minsk.codeanalysis.syntax.parser.CompilationUnitSyntax;
import minsk.codeanalysis.syntax.parser.Parser;
import minsk.codeanalysis.text.SourceText;
import minsk.diagnostics.Diagnosable;
import minsk.diagnostics.DiagnosableList;
import minsk.diagnostics.DiagnosticsCollection;

public class SyntaxTree implements Diagnosable {
	private final SourceText source;
	private final CompilationUnitSyntax root;
	private final DiagnosticsCollection diagnostics;
	
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

	public DiagnosticsCollection getDiagnostics() {
		return diagnostics;
	}
	
	public static SyntaxTree parse(SourceText source) {
		return new SyntaxTree(source);
	}
	
	public static SyntaxTree parse(String text) {
		return parse(SourceText.from(text));
	}
	
	public static DiagnosableList<SyntaxToken> parseTokens(String text) {
		var source = SourceText.from(text);
		return parseTokens(source);
	}

	public static DiagnosableList<SyntaxToken> parseTokens(SourceText source) {
		var lexer = new Lexer(source);
		
		var result = Stream.generate(lexer::lex)
			.takeWhile(t -> t.getKind() != SyntaxKind.EndOfFileToken)
			.collect(Collectors.toCollection(DiagnosableList::new));

		result.getDiagnostics().addFrom(lexer);
		
		return result;
	}
}