package minsk.codeanalysis.syntax.parser;

import minsk.codeanalysis.syntax.SyntaxChild;
import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;

public class VariableDeclarationSyntax implements StatementSyntax {

	private final SyntaxToken keyword;
	private final SyntaxToken identifier;
	private final SyntaxToken equals;
	private final ExpressionSyntax initializer;

	public VariableDeclarationSyntax(SyntaxToken keyword, SyntaxToken identifier, SyntaxToken equals,
			ExpressionSyntax initializer) {
		this.keyword = keyword;
		this.identifier = identifier;
		this.equals = equals;
		this.initializer = initializer;
	}

	@Override
	public SyntaxKind getKind() {
		return SyntaxKind.VariableDeclaration;
	}

	@SyntaxChild(order = 1)
	public SyntaxToken getKeyword() {
		return keyword;
	}

	@SyntaxChild(order = 2)
	public SyntaxToken getIdentifier() {
		return identifier;
	}

	@SyntaxChild(order = 3)
	public SyntaxToken getEquals() {
		return equals;
	}

	@SyntaxChild(order = 4)
	public ExpressionSyntax getInitializer() {
		return initializer;
	}

}
