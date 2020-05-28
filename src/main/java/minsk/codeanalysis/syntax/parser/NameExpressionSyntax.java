package minsk.codeanalysis.syntax.parser;

import minsk.codeanalysis.syntax.SyntaxChild;
import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;

public class NameExpressionSyntax implements ExpressionSyntax {
	private final SyntaxToken identifierToken;

	public NameExpressionSyntax(SyntaxToken identifierToken) {
		this.identifierToken = identifierToken;
	}

	@SyntaxChild
	public SyntaxToken getIdentifierToken() {
		return identifierToken;
	}
	
	@Override
	public SyntaxKind getKind() {
		return SyntaxKind.NameExpression;
	}
}