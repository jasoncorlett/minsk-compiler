package minsk.codeanalysis.syntax.parser;

import minsk.codeanalysis.Nested;
import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.SyntaxNode;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;

public class CompilationUnitSyntax extends SyntaxNode {

	private final StatementSyntax statement;
	private final SyntaxToken endOfFileToken;

	public CompilationUnitSyntax(StatementSyntax statement, SyntaxToken endOfFileToken) {
		this.statement = statement;
		this.endOfFileToken = endOfFileToken;
	}
	
	@Override
	public SyntaxKind getKind() {
		return SyntaxKind.CompilationUnit;
	}

	@Nested
	public StatementSyntax getStatement() {
		return statement;
	}

	public SyntaxToken getEndOfFileToken() {
		return endOfFileToken;
	}

}
