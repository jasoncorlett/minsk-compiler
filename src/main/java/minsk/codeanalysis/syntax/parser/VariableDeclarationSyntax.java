package minsk.codeanalysis.syntax.parser;

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

	public SyntaxToken getKeyword() {
		return keyword;
	}

	public SyntaxToken getIdentifier() {
		return identifier;
	}

	public SyntaxToken getEquals() {
		return equals;
	}

	public ExpressionSyntax getInitializer() {
		return initializer;
	}

}
