package minsk.codeanalysis.syntax.parser;

import minsk.codeanalysis.Nested;
import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;

public class ElseClauseSyntax extends StatementSyntax {
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

	@Nested(1)
	public SyntaxToken getElseKeyword() {
		return elseKeyword;
	}

	@Nested(2)
	public StatementSyntax getElseStatement() {
		return elseStatement;
	}
}
