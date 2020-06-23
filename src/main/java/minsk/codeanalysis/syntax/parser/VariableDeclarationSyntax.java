package minsk.codeanalysis.syntax.parser;

import minsk.codeanalysis.Nested;
import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;

public class VariableDeclarationSyntax extends StatementSyntax {

	private final SyntaxToken keyword;
	private final SyntaxToken identifier;
	private final TypeClauseSyntax typeClause;
	private final SyntaxToken equals;
	private final ExpressionSyntax initializer;

	public VariableDeclarationSyntax(SyntaxToken keyword, SyntaxToken identifier, TypeClauseSyntax typeClause,
			SyntaxToken equals, ExpressionSyntax initializer) {
		this.keyword = keyword;
		this.identifier = identifier;
		this.typeClause = typeClause;
		this.equals = equals;
		this.initializer = initializer;
	}

	@Override
	public SyntaxKind getKind() {
		return SyntaxKind.VariableDeclaration;
	}

	@Nested(1)
	public SyntaxToken getKeyword() {
		return keyword;
	}

	@Nested(2)
	public SyntaxToken getIdentifier() {
		return identifier;
	}

	@Nested(3)
	public TypeClauseSyntax getTypeClause() {
		return typeClause;
	}

	@Nested(4)
	public SyntaxToken getEquals() {
		return equals;
	}

	@Nested(5)
	public ExpressionSyntax getInitializer() {
		return initializer;
	}

}
