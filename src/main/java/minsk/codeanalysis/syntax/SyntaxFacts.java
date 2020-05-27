package minsk.codeanalysis.syntax;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static minsk.codeanalysis.syntax.SyntaxKind.*;

public class SyntaxFacts {
	public static List<SyntaxKind> getBinaryOperatorKinds() {
		return Arrays.stream(SyntaxKind.values())
				.filter(k -> k.getBinaryPrecedence() > 0)
				.collect(Collectors.toList());
	}
	
	public static List<SyntaxKind> getUnaryOperatorKinds() {
		return Arrays.stream(SyntaxKind.values())
				.filter(k -> k.getUnaryPrecedence() > 0)
				.collect(Collectors.toList());
	}

	public static SyntaxKind lookupKeywordKind(String text) {
		var kind = SyntaxKind.valueOf(text);
		return kind != null && kind.isKeyword() ? kind : IdentifierToken;
	}
}