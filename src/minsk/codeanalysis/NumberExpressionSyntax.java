package minsk.codeanalysis;

import java.util.List;

public class NumberExpressionSyntax extends ExpressionSyntax {
	public final SyntaxToken numberToken;
	public NumberExpressionSyntax(SyntaxToken numberToken) {
		super(SyntaxKind.NumberExpression);
		this.numberToken = numberToken;
	}
	
	@Override
	public Iterable<SyntaxNode> getChildren() {
		return List.of(numberToken);
	}
}