package minsk.codeanalysis.syntax.parser;

import minsk.codeanalysis.Nested;
import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;

public class LiteralExpressionSyntax extends ExpressionSyntax {
	private final SyntaxToken literalToken;
	private final Object value;
	
	public LiteralExpressionSyntax(SyntaxToken literalToken) {
		this(literalToken, literalToken.getValue());
	}
	
	public LiteralExpressionSyntax(SyntaxToken literalToken, Object value) {
		this.literalToken = literalToken;
		this.value = value;
	}
	
	@Nested(1)
	public SyntaxToken getLiteralToken() {
		return literalToken;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public SyntaxKind getKind() {
		return SyntaxKind.LiteralExpression;
	}
}