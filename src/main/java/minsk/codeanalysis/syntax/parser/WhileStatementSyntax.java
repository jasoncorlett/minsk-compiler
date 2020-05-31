package minsk.codeanalysis.syntax.parser;

import minsk.codeanalysis.syntax.SyntaxChild;
import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;

public class WhileStatementSyntax implements StatementSyntax {

	private final SyntaxToken whileKeyword;
	private final ExpressionSyntax condition;
	private final StatementSyntax body;

	public WhileStatementSyntax(SyntaxToken whileKeyword, ExpressionSyntax condition, StatementSyntax body) {
		this.whileKeyword = whileKeyword;
		this.condition = condition;
		this.body = body;
	}

	@Override
	public SyntaxKind getKind() {
		return SyntaxKind.WhileStatement;
	}
	
	@SyntaxChild(order = 1)
	public SyntaxToken getWhileKeyword() {
		return whileKeyword;
	}

	@SyntaxChild(order = 2)
	public ExpressionSyntax getCondition() {
		return condition;
	}

	@SyntaxChild(order = 3)
	public StatementSyntax getBody() {
		return body;
	}
}
