package minsk.codeanalysis.syntax;

public enum SyntaxKind {
	BadToken, EndOfFileToken,
	LiteralToken, WhitespaceToken,
	PlusToken, SlashToken, StarToken, MinusToken,
	OpenParenthesisToken, CloseParenthesisToken,
	UnaryExpression, BinaryExpression,
	LiteralExpression, ParenthesizedExpression, 
}