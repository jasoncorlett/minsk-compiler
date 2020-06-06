package minsk.codeanalysis.syntax.parser;

import java.util.List;

import minsk.codeanalysis.Nested;
import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;

public class BlockStatementSyntax extends StatementSyntax {

	private final SyntaxToken openBraceToken;
	private final List<StatementSyntax> statements;
	private final SyntaxToken closeBraceToken;

	public BlockStatementSyntax(SyntaxToken openBraceToken, List<StatementSyntax> statements, SyntaxToken closeBraceToken) {
		this.openBraceToken = openBraceToken;
		this.statements = statements;
		this.closeBraceToken = closeBraceToken;
		
	}
	
	@Nested(1)
	public SyntaxToken getOpenBraceToken() {
		return openBraceToken;
	}

	@Nested(2)
	public List<StatementSyntax> getStatements() {
		return statements;
	}

	@Nested(3)
	public SyntaxToken getCloseBraceToken() {
		return closeBraceToken;
	}

	@Override
	public SyntaxKind getKind() {
		return SyntaxKind.BlockStatement;
	}

}
