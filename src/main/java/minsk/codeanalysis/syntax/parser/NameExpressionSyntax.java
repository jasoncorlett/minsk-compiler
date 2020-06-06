package minsk.codeanalysis.syntax.parser;

import minsk.codeanalysis.Nested;
import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;

public class NameExpressionSyntax extends ExpressionSyntax {
	private final SyntaxToken identifierToken;

	public NameExpressionSyntax(SyntaxToken identifierToken) {
		this.identifierToken = identifierToken;
	}

	@Nested
	public SyntaxToken getIdentifierToken() {
		return identifierToken;
	}
	
	@Override
	public SyntaxKind getKind() {
		return SyntaxKind.NameExpression;
	}
}