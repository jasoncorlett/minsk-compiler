package minsk.codeanalysis.syntax;

import static minsk.codeanalysis.Assertions.assertSingle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import minsk.codeanalysis.text.TextSpan;

class LexerTest {

	@Test
	public void TestLexesUnterminatedString() {
		var text = "\"";
		var tokens = SyntaxTree.parseTokens(text);

		var token = assertSingle(tokens);

		assertEquals(SyntaxKind.StringToken, token.getKind());
		assertEquals(text, token.getText());

		var diagnostic = assertSingle(tokens.getDiagnostics());

		assertEquals(new TextSpan(0, 1), diagnostic.getSpan());
		assertEquals("Found unterminated string.", diagnostic.getMessage());
	}

	/**
	 * SyntaxTuple - holds basic information about a token required to test
	 * Saves the hassle of passing values separately or constructing a token
	 */
	private static class SyntaxTuple {
		final SyntaxKind kind;
		final String text;
		
		SyntaxTuple(SyntaxKind kind, String text) {
			this.kind = kind;
			this.text = text;
		}
		
		@Override
		public String toString() {
			return String.format("%s '%s'", kind, text);
		}
	}
	
	/**
	 * Determine if two kinds of syntax must be separated by whitespace
	 * to produce valid language syntax
	 * 
	 * @param aKind
	 * @param bKind
	 * @return if these two kinds of syntax must be separated by whitespace
	 */
	private static boolean pairRequiresSeparator(SyntaxKind aKind, SyntaxKind bKind) {
		var aIsKeyword = aKind.isKeyword();
		var bIsKeyword = bKind.isKeyword();
		var aIsEquals  = aKind.toString().startsWith("Equals");
		var bIsEquals  = bKind.toString().startsWith("Equals");
		
		return (aKind == SyntaxKind.IdentifierToken && bKind == SyntaxKind.IdentifierToken)
				|| (aIsKeyword && bIsKeyword)
				|| (aIsKeyword && bKind == SyntaxKind.IdentifierToken)
				|| (aKind == SyntaxKind.IdentifierToken && bIsKeyword)
				|| (aKind == SyntaxKind.LiteralToken && bKind == SyntaxKind.LiteralToken)
				|| (aKind == SyntaxKind.BangToken && bIsEquals)
				|| (aKind == SyntaxKind.LessToken && bIsEquals)
				|| (aKind == SyntaxKind.LessToken && bIsEquals)
				|| (aKind == SyntaxKind.GreaterToken && bIsEquals)
				|| (aKind == SyntaxKind.GreaterToken && bIsEquals)
				|| (aKind == SyntaxKind.AmpersandToken && bKind == SyntaxKind.AmpersandToken)
				|| (aKind == SyntaxKind.AmpersandToken && bKind == SyntaxKind.AmpersandAmpersandToken)
				|| (aKind == SyntaxKind.PipeToken && bKind == SyntaxKind.PipeToken)
				|| (aKind == SyntaxKind.PipeToken && bKind == SyntaxKind.PipePipeToken)
				|| (aKind == SyntaxKind.StringToken && bKind == SyntaxKind.StringToken)
				|| (aIsEquals && bIsEquals);
	}	
	
	private static Stream<SyntaxTuple> getTokenData() {
		var fixedTokens = Arrays.stream(SyntaxKind.values())
				.map(k -> new SyntaxTuple(k, k.getFixedText()))
				.filter(t -> t.text != null);
		
		var dynamicTokens = Stream.of(
				new SyntaxTuple(SyntaxKind.IdentifierToken, "a"),
				new SyntaxTuple(SyntaxKind.IdentifierToken, "count"),
				new SyntaxTuple(SyntaxKind.LiteralToken, "2"),
				new SyntaxTuple(SyntaxKind.LiteralToken, "12"),
				new SyntaxTuple(SyntaxKind.StringToken, "\"wolf\""),
				new SyntaxTuple(SyntaxKind.StringToken, "\"wo\"\"lf\"")
		);
		
		return Stream.concat(fixedTokens, dynamicTokens);
				
	}
	
	public static Stream<SyntaxTuple> getSeparatorsData() {
		return Stream.of(" ", "  ", "\t", "\r", "\n", "\r\n")
				.map(s -> new SyntaxTuple(SyntaxKind.WhitespaceToken, s));
	}

	private static Stream<Arguments> getTokenPairsData() {
		return getTokenData().flatMap(
				a -> getTokenData()
				.filter(b -> !pairRequiresSeparator(a.kind, b.kind))
				.map(b -> Arguments.of(a, b))
		);
	}
	
	private static Stream<Arguments> getTokenPairsWithSeparator() {
		return getTokenData()
		.flatMap(
				a -> getTokenData()
				.filter(b -> pairRequiresSeparator(a.kind, b.kind))
				.flatMap(b -> getSeparatorsData().map(s -> Arguments.of(a, s, b)))
			);
	}

	/** 
	 * Theory - all individual syntax elements should produce a single, valid token
	 * 
	 * @param a
	 */
	@ParameterizedTest
	@MethodSource({"getTokenData", "getSeparatorsData"})
	void TestSingleTokens(SyntaxTuple a) {
		var tokens = SyntaxTree.parseTokens(a.text);

		assertEquals(1, tokens.size());
		var token = tokens.get(0);
		
		assertEquals(a.kind, token.getKind());
		assertEquals(a.text, token.getText());
	}
	
	/**
	 * Theory - A combination of two syntax tokens should produce two valid tokens
	 * The data method will filter out syntax that cannot be combined
	 * 
	 * @param a
	 * @param b
	 * @see #TestTokenPairsWithSeparator(SyntaxTuple, SyntaxTuple, SyntaxTuple)
	 */
	@ParameterizedTest
	@MethodSource("getTokenPairsData")
	void TestTokenPairs(SyntaxTuple a, SyntaxTuple b) {
		var text = a.text + b.text;
		var tokens = SyntaxTree.parseTokens(text);
		
		assertEquals(2, tokens.size());
		
		var aToken = tokens.get(0);
		var bToken = tokens.get(1);
		
		assertEquals(a.kind, aToken.getKind());
		assertEquals(a.text, aToken.getText());
		assertEquals(b.kind, bToken.getKind());
		assertEquals(b.text, bToken.getText());
	}
	
	/**
	 * Theory - two syntax elements separated by whitespace should produce three valid tokens
	 * 
	 * @param a
	 * @param sep
	 * @param b
	 */
	@ParameterizedTest
	@MethodSource("getTokenPairsWithSeparator")
	void TestTokenPairsWithSeparator(SyntaxTuple a, SyntaxTuple sep, SyntaxTuple b) {
		var text = a.text + sep.text + b.text;
		var tokens = SyntaxTree.parseTokens(text);
		
		assertEquals(3, tokens.size());
		
		assertEquals(a.kind, 	tokens.get(0).getKind());
		assertEquals(a.text, 	tokens.get(0).getText());
		assertEquals(sep.kind,	tokens.get(1).getKind());
		assertEquals(sep.text, 	tokens.get(1).getText());
		assertEquals(b.kind, 	tokens.get(2).getKind());
		assertEquals(b.text, 	tokens.get(2).getText());
	}
	
	@Nested
	@TestInstance(Lifecycle.PER_CLASS)
	class LexerTestTest {
		Set<SyntaxKind> excludes = Set.of(SyntaxKind.BadToken, SyntaxKind.EndOfFileToken);
		
		final Set<SyntaxKind> kindsTested = Stream.concat(getSeparatorsData(), getTokenData())
				.map(t -> t.kind).collect(Collectors.toSet());

		Stream<SyntaxKind> getKindsData() {
			return Arrays.stream(SyntaxKind.values())
					.filter(k -> k.toString().endsWith("Keyword") || k.toString().endsWith("Token"))
					.filter(Predicate.not(excludes::contains));
		}
		
		@ParameterizedTest
		@MethodSource("getKindsData")
		void EnsureKindsTested(SyntaxKind kind) {
			assertTrue(kindsTested.contains(kind), "Must test " + kind);
		}
	}
}
