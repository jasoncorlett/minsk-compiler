package minsk.codeanalysis.syntax;

public enum SyntaxKind {
	// Tokens
	BadToken, EndOfFileToken,
	LiteralToken, WhitespaceToken,
	PlusToken, SlashToken, StarToken, MinusToken,
	OpenParenthesisToken, CloseParenthesisToken,
	IdentifierToken,
	BangToken, AmpersandAmpersandToken, PipePipeToken,
	EqualsEqualsToken, BangEqualsToken, EqualsToken,
	
	// Keywords
	TrueKeyword, FalseKeyword,
	
	// Expressions
	UnaryExpression, BinaryExpression,
	LiteralExpression, ParenthesizedExpression, NameExpression, AssignmentExpression;
}
