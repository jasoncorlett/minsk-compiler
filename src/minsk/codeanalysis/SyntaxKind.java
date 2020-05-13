package minsk.codeanalysis;

public enum SyntaxKind {
	NumberToken, WhitespaceToken,
	PlusToken, SlashToken, StarToken, MinusToken,
	OpenParenthesisToken, CloseParenthesisToken,
	BadToken, EndOfFileToken,
	NumberExpression, BinaryExpression, ParenthesizedExpression
}