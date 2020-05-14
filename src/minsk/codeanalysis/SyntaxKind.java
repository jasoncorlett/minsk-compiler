package minsk.codeanalysis;

public enum SyntaxKind {
	BadToken, EndOfFileToken,
	NumberToken, WhitespaceToken,
	PlusToken, SlashToken, StarToken, MinusToken,
	OpenParenthesisToken, CloseParenthesisToken,
	NumberExpression, BinaryExpression, ParenthesizedExpression
}