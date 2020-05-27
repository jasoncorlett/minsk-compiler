package minsk.codeanalysis.syntax;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class SyntaxKindTest {

	public static SyntaxKind[] GetTextRoundTrips() {
		return SyntaxKind.values();		
	}
	
	@ParameterizedTest
	@MethodSource
	public void GetTextRoundTrips(SyntaxKind kind) {
		var text = kind.getFixedText();
		
		if (text == null)
			return;
		
		var tokens = SyntaxTree.parseTokens(text);
		assertEquals(1,  tokens.size());
		
		var token = tokens.get(0);
		
		assertEquals(text, token.text());
		assertEquals(kind, token.kind());
		
		if (token.kind().getFixedText() != null)
			assertEquals(token.text(), token.kind().getFixedText());
	}
	
}
