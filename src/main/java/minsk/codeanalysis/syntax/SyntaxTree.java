package minsk.codeanalysis.syntax;

import minsk.diagnostics.*;

public class SyntaxTree implements Diagnosable {
	private final ExpressionSyntax root;
	private final SyntaxToken endOfFileToken;
	private final DiagnosticsBag diagnostics;
	
	public static SyntaxTree parse(String text) {
		var parser = new Parser(text);
		return parser.parse();
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