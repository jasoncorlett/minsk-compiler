package minsk.codeanalysis.syntax.parser;

import java.util.LinkedList;
import java.util.List;

import minsk.codeanalysis.syntax.SyntaxChild;
import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.SyntaxNode;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;

public class BlockStatementSyntax implements StatementSyntax {

	private final SyntaxToken openBraceToken;
	private final List<StatementSyntax> statements;
	private final SyntaxToken closeBraceToken;

	public BlockStatementSyntax(SyntaxToken openBraceToken, List<StatementSyntax> statements, SyntaxToken closeBraceToken) {
		this.openBraceToken = openBraceToken;
		this.statements = statements;
		this.closeBraceToken = closeBraceToken;
		
	}
	
	@SyntaxChild(order = 1)
	public SyntaxToken getOpenBraceToken() {
		return openBraceToken;
	}

	@SyntaxChild(order = 2)
	public List<StatementSyntax> getStatements() {
		return statements;
	}

	@SyntaxChild(order = 3)
	public SyntaxToken getCloseBraceToken() {
		return closeBraceToken;
	}

	@Override
	public SyntaxKind getKind() {
		return SyntaxKind.BlockStatement;
	}

}
