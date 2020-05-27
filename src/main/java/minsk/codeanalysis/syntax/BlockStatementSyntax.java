package minsk.codeanalysis.syntax;

import java.util.List;

public record BlockStatementSyntax(
		@SyntaxChild(1) SyntaxToken openBraceToken,
		@SyntaxChild(2) List<StatementSyntax> statements,
		@SyntaxChild(3) SyntaxToken closeBraceToken
	) implements StatementSyntax {

	@Override
	public SyntaxKind kind() {
		return SyntaxKind.BlockStatement;
	}

}
