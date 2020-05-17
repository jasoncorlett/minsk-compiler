package minsk.codeanalysis.syntax;

import java.util.Map;

import static minsk.codeanalysis.syntax.SyntaxKind.*;

public class SyntaxFacts {
	private static final Map<SyntaxKind, Integer> unaryOperatorPrecedence = Map.of(
			PlusToken, 6,
			MinusToken, 6,
			BangToken, 6
	);
	
	private static final Map<SyntaxKind, Integer> binaryOperatorPrecedence = Map.of(
			StarToken, 5,
			SlashToken, 5,
			PlusToken, 4,
			MinusToken, 4,
			EqualsEqualsToken, 3,
			BangEqualsToken, 3,
			AmpersandAmpersandToken, 2,
			PipePipeToken, 1
	);
	
	public static final Map<String, SyntaxKind> keywords = Map.of(
			"true", TrueKeyword,
			"false", FalseKeyword
	);
	
	static int lookupUnaryOperatorPrecedence(SyntaxKind kind) {
		return unaryOperatorPrecedence.getOrDefault(kind, 0);
	}

	static int lookupBinaryOperatorPrecedence(SyntaxKind kind) {
		return binaryOperatorPrecedence.getOrDefault(kind, 0);
	}

	public static SyntaxKind lookupKeywordKind(String text) {
		return keywords.getOrDefault(text, IdentifierToken);
	}
	
	public static String getFixedText(SyntaxKind kind) {
		switch(kind) {
		case PlusToken:
			return "+";
		case SlashToken:
			return "/";
		case StarToken:
			return "*";
		case MinusToken:
			return "-";
		case OpenParenthesisToken:
			return "(";
		case CloseParenthesisToken:
			return ")";
		case BangToken:
			return "!";
		case AmpersandAmpersandToken:
			return "&&";
		case PipePipeToken:
			return "||";
		case EqualsEqualsToken:
			return "==";
		case BangEqualsToken:
			return "!=";
		case TrueKeyword:
			return "true";
		case FalseKeyword:
			return "false";
		case EqualsToken:
			return "=";
		default:
			return null;
		}
	}
	
}