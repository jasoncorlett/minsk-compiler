package minsk.codeanalysis.syntax;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class SyntaxFactsTest {

	@ParameterizedTest
	@MethodSource("GetTextRoutTripsData")
	public void GetTextRoundTrips(SyntaxKind kind) {
		var text = SyntaxFacts.getFixedText(kind);
		
		if (text == null)
			return;
		
		var tokens = SyntaxTree.parseTokens(text);
		assertEquals(1,  tokens.size());
		
		var token = tokens.get(0);
		
		assertEquals(text, token.getText());
		assertEquals(kind, token.getKind());
	}
	
	public static SyntaxKind[] GetTextRoutTripsData() {
		return SyntaxKind.values();		
	}
}
