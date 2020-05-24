package minsk.codeanalysis.syntax;

public class CompilationUnitSyntax implements SyntaxNode {

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

	@SyntaxChild
	public StatementSyntax getStatement() {
		return statement;
	}

	public SyntaxToken getEndOfFileToken() {
		return endOfFileToken;
	}

}
