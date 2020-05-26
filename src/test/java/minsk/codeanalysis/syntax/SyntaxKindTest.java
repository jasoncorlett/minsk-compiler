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
		
		assertEquals(text, token.getText());
		assertEquals(kind, token.getKind());
		
		if (token.getKind().getFixedText() != null)
			assertEquals(token.getText(), token.getKind().getFixedText());
	}
	
}
