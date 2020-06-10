package minsk.codeanalysis.syntax.parser;

import java.util.List;

import minsk.codeanalysis.Nested;
import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;

public class CallExpressionSyntax extends ExpressionSyntax {

	private final SyntaxToken identifier;
	private final SyntaxToken openParenthesisToken;
	private final SeparatedSyntaxList<ExpressionSyntax> arguments;
	private final SyntaxToken closeParenthesisToken;

	public CallExpressionSyntax(SyntaxToken identifier, SyntaxToken openParenthesisToken, SeparatedSyntaxList<ExpressionSyntax> arguments, SyntaxToken closeParenthesisToken) {
		this.identifier = identifier;
		this.openParenthesisToken = openParenthesisToken;
		this.arguments = arguments;
		this.closeParenthesisToken = closeParenthesisToken;
	}
	
	@Override
	public SyntaxKind getKind() {
		return SyntaxKind.CallExpression;
	}

	@Nested(1)
	public SyntaxToken getIdentifier() {
		return identifier;
	}

	@Nested(2)
	public SyntaxToken getOpenParenthesisToken() {
		return openParenthesisToken;
	}

	@Nested(3)
	public SeparatedSyntaxList<ExpressionSyntax> getArguments() {
		return arguments;
	}

	@Nested(4)
	public SyntaxToken getCloseParenthesisToken() {
		return closeParenthesisToken;
	}
	
	

}
