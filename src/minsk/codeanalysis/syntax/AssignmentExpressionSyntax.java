package minsk.codeanalysis.syntax;

import java.util.List;

public class AssignmentExpressionSyntax extends ExpressionSyntax {
	private final SyntaxToken identifierToken;
	private final SyntaxToken equalsToken;
	private final ExpressionSyntax expression;

	public AssignmentExpressionSyntax(SyntaxToken identifierToken, SyntaxToken equalsToken, ExpressionSyntax expression) {
		this.identifierToken = identifierToken;
		this.equalsToken = equalsToken;
		this.expression = expression;
	}

	public SyntaxToken getIdentifierToken() {
		return identifierToken;
	}

	public SyntaxToken getEqualsToken() {
		return equalsToken;
	}

	public ExpressionSyntax getExpression() {
		return expression;
	}

	@Override
	public Iterable<SyntaxNode> getChildren() {
		// TODO Auto-generated method stub
		return List.of(identifierToken, equalsToken, expression);
	}

	@Override
	public SyntaxKind getKind() {
		// TODO Auto-generated method stub
		return SyntaxKind.AssignmentExpression;
	}
	
}