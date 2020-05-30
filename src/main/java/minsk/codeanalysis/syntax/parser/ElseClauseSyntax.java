package minsk.codeanalysis.syntax.parser;

import minsk.codeanalysis.syntax.SyntaxChild;
import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;

public class ElseClauseSyntax implements StatementSyntax {
	private final SyntaxToken elseKeyword;
	private final StatementSyntax elseStatement;
	
	public ElseClauseSyntax(SyntaxToken elseKeyword, StatementSyntax elseStatement) {
		this.elseKeyword = elseKeyword;
		this.elseStatement = elseStatement;
	}
	
	@Override
	public SyntaxKind getKind() {
		return SyntaxKind.ElseClause;
	}

	@SyntaxChild(order = 1)
	public SyntaxToken getElseKeyword() {
		return elseKeyword;
	}

	@SyntaxChild(order = 2)
	public StatementSyntax getElseStatement() {
		return elseStatement;
	}
}
