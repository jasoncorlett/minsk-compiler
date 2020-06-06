package minsk.codeanalysis.syntax.parser;

import minsk.codeanalysis.Nested;
import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;

public class IfStatementSyntax extends StatementSyntax {

	private final SyntaxToken ifKeyword;
	private final ExpressionSyntax condition;
	private final StatementSyntax thenStatement;
	private final ElseClauseSyntax elseClause; 
	
	public IfStatementSyntax(SyntaxToken ifKeyword, ExpressionSyntax condition, StatementSyntax thenStatement,
			ElseClauseSyntax elseClause) {
		this.ifKeyword = ifKeyword;
		this.condition = condition;
		this.thenStatement = thenStatement;
		this.elseClause = elseClause;
	}

	@Override
	public SyntaxKind getKind() {
		return SyntaxKind.IfStatement;
	}

	@Nested(1)
	public SyntaxToken getIfKeyword() {
		return ifKeyword;
	}

	@Nested(2)
	public ExpressionSyntax getCondition() {
		return condition;
	}

	@Nested(3)
	public StatementSyntax getThenStatement() {
		return thenStatement;
	}

	@Nested(4)
	public ElseClauseSyntax getElseClause() {
		return elseClause;
	}

}
