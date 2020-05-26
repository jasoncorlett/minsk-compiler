package minsk.codeanalysis.syntax;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static minsk.codeanalysis.syntax.SyntaxKind.*;

public class SyntaxFacts {
	private static final Map<SyntaxKind, Integer> unaryOperatorPrecedence = Arrays.stream(SyntaxKind.values())
		.filter(k -> k.getUnaryPrecedence() > 0)
		.collect(Collectors.toMap(Function.identity(), SyntaxKind::getUnaryPrecedence));

	private static final Map<SyntaxKind, Integer> binaryOperatorPrecedence = Arrays.stream(SyntaxKind.values())
			.filter(k -> k.getBinaryPrecedence() > 0)
			.collect(Collectors.toMap(Function.identity(), SyntaxKind::getBinaryPrecedence));
	
	private static final Map<String, SyntaxKind> keywords = Arrays.stream(SyntaxKind.values())
			.filter(SyntaxKind::isKeyword)
			.collect(Collectors.toMap(SyntaxKind::getFixedText, Function.identity()));

	private static final Map<SyntaxKind, String> fixedText = Arrays.stream(SyntaxKind.values())
			.filter(k -> k.getFixedText() != null)
			.collect(Collectors.toMap(Function.identity(), SyntaxKind::getFixedText));
	
	public static int lookupUnaryOperatorPrecedence(SyntaxKind kind) {
		return unaryOperatorPrecedence.getOrDefault(kind, 0);
	}

	public static int lookupBinaryOperatorPrecedence(SyntaxKind kind) {
		return binaryOperatorPrecedence.getOrDefault(kind, 0);
	}
	
	public static List<SyntaxKind> getBinaryOperatorKinds() {
		return Arrays.stream(SyntaxKind.values())
				.filter(k -> SyntaxFacts.lookupBinaryOperatorPrecedence(k) > 0)
				.collect(Collectors.toList());
	}
	
	public static List<SyntaxKind> getUnaryOperatorKinds() {
		return Arrays.stream(SyntaxKind.values())
				.filter(k -> lookupUnaryOperatorPrecedence(k) > 0)
				.collect(Collectors.toList());
	}

	public static SyntaxKind lookupKeywordKind(String text) {
		return keywords.getOrDefault(text, IdentifierToken);
	}
	
	public static String getFixedText(SyntaxKind kind) {
		return fixedText.get(kind);
	}
}