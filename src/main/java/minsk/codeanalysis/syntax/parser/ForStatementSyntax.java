package minsk.codeanalysis.syntax.parser;

import java.util.LinkedList;
import java.util.List;

import minsk.codeanalysis.syntax.SyntaxChild;
import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.syntax.SyntaxNode;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;

// for v = 1 to 5
public class ForStatementSyntax implements StatementSyntax {
	private final SyntaxToken forKeyword;
	private final SyntaxToken identifier;
	private final SyntaxToken equalsToken;
	private final ExpressionSyntax lowerBound;
	private final SyntaxToken toKeyword;
	private final ExpressionSyntax upperBound;
	private final StatementSyntax body;
	
	public ForStatementSyntax(SyntaxToken forKeyword, SyntaxToken identifier, SyntaxToken equalsToken,
			ExpressionSyntax lowerBound, SyntaxToken toKeyword, ExpressionSyntax upperBound, StatementSyntax body) {
		this.forKeyword = forKeyword;
		this.identifier = identifier;
		this.equalsToken = equalsToken;
		this.lowerBound = lowerBound;
		this.toKeyword = toKeyword;
		this.upperBound = upperBound;
		this.body = body;
	}

	@Override
	public SyntaxKind getKind() {
		return SyntaxKind.ForStatement;
	}

	@SyntaxChild(order = 1)
	public SyntaxToken getForKeyword() {
		return forKeyword;
	}

	@SyntaxChild(order = 2)
	public SyntaxToken getIdentifier() {
		return identifier;
	}
	@SyntaxChild(order = 3)
	public SyntaxToken getEqualsToken() {
		return equalsToken;
	}

	@SyntaxChild(order = 4)
	public ExpressionSyntax getLowerBound() {
		return lowerBound;
	}

	@SyntaxChild(order = 5)
	public SyntaxToken getToKeyword() {
		return toKeyword;
	}

	@SyntaxChild(order = 6)
	public ExpressionSyntax getUpperBound() {
		return upperBound;
	}

	@SyntaxChild(order = 7)
	public StatementSyntax getBody() {
		return body;
	}
}
