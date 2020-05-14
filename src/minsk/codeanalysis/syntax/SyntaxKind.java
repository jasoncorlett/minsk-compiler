package minsk.codeanalysis.syntax;

public enum SyntaxKind {
	BadToken, EndOfFileToken,
	NumberToken, WhitespaceToken,
	PlusToken, SlashToken, StarToken, MinusToken,
	OpenParenthesisToken, CloseParenthesisToken,
	UnaryExpression, BinaryExpression,
	NumberExpression, ParenthesizedExpression, 
}