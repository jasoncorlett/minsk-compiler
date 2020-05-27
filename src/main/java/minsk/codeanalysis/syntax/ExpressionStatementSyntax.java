package minsk.codeanalysis.syntax;

public class ExpressionStatementSyntax implements StatementSyntax {
	private final ExpressionSyntax expression;

	public ExpressionStatementSyntax(ExpressionSyntax expression) {
		this.expression = expression;
	}
	
	@Override
	public SyntaxKind kind() {
		return SyntaxKind.ExpressionStatement;
	}

	public ExpressionSyntax getExpression() {
		return expression;
	}
}
