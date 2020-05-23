package minsk.codeanalysis.syntax;

public class CompilationUnitSyntax implements SyntaxNode {

	private final ExpressionSyntax expression;
	private final SyntaxToken endOfFileToken;

	public CompilationUnitSyntax(ExpressionSyntax expression, SyntaxToken endOfFileToken) {
		this.expression = expression;
		this.endOfFileToken = endOfFileToken;
	}
	
	@Override
	public SyntaxKind getKind() {
		return SyntaxKind.CompilationUnit;
	}

	@SyntaxChild
	public ExpressionSyntax getExpression() {
		return expression;
	}

	public SyntaxToken getEndOfFileToken() {
		return endOfFileToken;
	}

}
