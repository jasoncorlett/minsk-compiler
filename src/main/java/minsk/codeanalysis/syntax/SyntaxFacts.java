package minsk.codeanalysis.syntax;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static minsk.codeanalysis.syntax.SyntaxKind.*;

public class SyntaxFacts {
	
	private static Map<String, SyntaxKind> keywords = Arrays.stream(SyntaxKind.values())
			.filter(SyntaxKind::isKeyword)
			.collect(Collectors.toMap(SyntaxKind::getFixedText, Function.identity()));
	
	public static Stream<SyntaxKind> getBinaryOperatorStream() {
		return Arrays.stream(SyntaxKind.values())
				.filter(k -> k.getBinaryPrecedence() > 0);
	}
	
	public static Stream<SyntaxKind> getUnaryOperatorStream() {
		return Arrays.stream(SyntaxKind.values())
				.filter(k -> k.getUnaryPrecedence() > 0);
	}

	public static SyntaxKind lookupKeywordKind(String text) {
		return keywords.getOrDefault(text, IdentifierToken);
	}
}