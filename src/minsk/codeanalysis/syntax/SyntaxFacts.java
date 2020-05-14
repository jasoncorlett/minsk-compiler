package minsk.codeanalysis.syntax;

public class SyntaxFacts {
	static int getUnaryOperatorPrecedence(SyntaxKind kind) {
		switch (kind) {
		case PlusToken:
		case MinusToken:
			return 3;
		default:
			return 0;
		}
	}

	static int getBinaryOperatorPrecedence(SyntaxKind kind) {
		switch (kind) {
		case StarToken:
		case SlashToken:
			return 2;

		case PlusToken:
		case MinusToken:
			return 1;

		default:
			return 0; // Not a binary operator
		}
	}

	public static SyntaxKind getKeywordKind(String text) {
		switch (text) {
		case "true":
			return SyntaxKind.TrueKeyword;
		case "false":
			return SyntaxKind.FalseKeyword;
		default:
			return SyntaxKind.IdentifierToken;
		}
	}
}