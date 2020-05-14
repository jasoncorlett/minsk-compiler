package minsk.codeanalysis.syntax;

import java.util.List;

public class SyntaxTree {
	private final ExpressionSyntax root;
	private final SyntaxToken endOfFileToken;
	private final List<String> diagnostics;
	
	public static SyntaxTree parse(String text) {
		var parser = new Parser(text);
		return parser.parse();
	}

	public SyntaxTree(List<String> diagnostics, ExpressionSyntax root, SyntaxToken endOfFileToken) {
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

	public List<String> getDiagnostics() {
		return diagnostics;
	}
}