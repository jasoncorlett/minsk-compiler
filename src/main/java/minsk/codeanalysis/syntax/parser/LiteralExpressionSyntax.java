package minsk.codeanalysis.syntax.parser;

import minsk.codeanalysis.syntax.SyntaxChild;
import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;

public class LiteralExpressionSyntax implements ExpressionSyntax {
	private final SyntaxToken literalToken;
	private final Object value;
	
	public LiteralExpressionSyntax(SyntaxToken literalToken) {
		this(literalToken, literalToken.getValue());
	}
	
	public LiteralExpressionSyntax(SyntaxToken literalToken, Object value) {
		this.literalToken = literalToken;
		this.value = value;
	}
	
	@SyntaxChild(order = 1)
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