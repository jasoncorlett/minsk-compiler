package minsk.codeanalysis.syntax;

public enum SyntaxKind {
	// Tokens
	BadToken, EndOfFileToken,
	LiteralToken, WhitespaceToken,
	PlusToken, SlashToken, StarToken, MinusToken,
	OpenParenthesisToken, CloseParenthesisToken,
	IdentifierToken,
	
	// Keywords
	TrueKeyword, FalseKeyword,
	
	// Expressions
	UnaryExpression, BinaryExpression,
	LiteralExpression, ParenthesizedExpression;
	
	public SyntaxToken newToken(int position, String text, Object value) {
		return new SyntaxToken(this, position, text, value);		
	}
}
