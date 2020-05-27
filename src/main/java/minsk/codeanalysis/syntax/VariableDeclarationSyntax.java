package minsk.codeanalysis.syntax;

public record VariableDeclarationSyntax(SyntaxToken keyword, SyntaxToken identifier, SyntaxToken equalsToken, ExpressionSyntax initializer) implements StatementSyntax {

	@Override
	public SyntaxKind kind() {
		return SyntaxKind.VariableDeclaration;
	}

}
