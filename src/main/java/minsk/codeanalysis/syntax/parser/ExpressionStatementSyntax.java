package minsk.codeanalysis.syntax.parser;

import minsk.codeanalysis.Nested;
import minsk.codeanalysis.syntax.SyntaxKind;

public class ExpressionStatementSyntax extends StatementSyntax {
	private final ExpressionSyntax expression;

	public ExpressionStatementSyntax(ExpressionSyntax expression) {
		this.expression = expression;
	}
	
	@Override
	public SyntaxKind getKind() {
		return SyntaxKind.ExpressionStatement;
	}

	@Nested
	public ExpressionSyntax getExpression() {
		return expression;
	}
}
