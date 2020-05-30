package minsk.codeanalysis.syntax.parser;

import minsk.codeanalysis.syntax.SyntaxChild;
import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;

public class IfStatementSyntax implements StatementSyntax {

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

	@SyntaxChild(order = 1)
	public SyntaxToken getIfKeyword() {
		return ifKeyword;
	}

	@SyntaxChild(order = 2)
	public ExpressionSyntax getCondition() {
		return condition;
	}

	@SyntaxChild(order = 3)
	public StatementSyntax getThenStatement() {
		return thenStatement;
	}

	@SyntaxChild(order = 4)
	public ElseClauseSyntax getElseClause() {
		return elseClause;
	}

}
