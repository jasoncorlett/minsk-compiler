package minsk.codeanalysis;

import java.util.List;

public class SyntaxTree {
	public ExpressionSyntax root;
	public SyntaxToken endOfFileToken;
	public List<String> diagnostics;

	public SyntaxTree(List<String> diagnostics, ExpressionSyntax root, SyntaxToken endOfFileToken) {
		this.diagnostics = diagnostics;
		this.root = root;
		this.endOfFileToken = endOfFileToken;
	}
}