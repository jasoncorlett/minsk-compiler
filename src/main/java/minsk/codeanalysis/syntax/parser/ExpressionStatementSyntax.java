package minsk.codeanalysis.syntax.parser;

import minsk.codeanalysis.syntax.SyntaxKind;

public class ExpressionStatementSyntax implements StatementSyntax {
	private final ExpressionSyntax expression;

	public ExpressionStatementSyntax(ExpressionSyntax expression) {
		this.expression = expression;
	}
	
	@Override
	public SyntaxKind getKind() {
		return SyntaxKind.ExpressionStatement;
	}

	public ExpressionSyntax getExpression() {
		return expression;
	}
}
