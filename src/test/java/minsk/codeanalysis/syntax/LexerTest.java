package minsk.codeanalysis.syntax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LexerTest {
	
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
	
	private static boolean pairRequiresSeparator(SyntaxKind aKind, SyntaxKind bKind) {
		var aIsKeyword = aKind.toString().endsWith("Keyword");
		var bIsKeyword = bKind.toString().endsWith("Keyword");
		var aIsEquals  = aKind.toString().startsWith("Equals");
		var bIsEquals  = bKind.toString().startsWith("Equals");
		
		return (aKind == SyntaxKind.IdentifierToken && bKind == SyntaxKind.IdentifierToken)
				|| (aIsKeyword && bIsKeyword)
				|| (aIsKeyword && bKind == SyntaxKind.IdentifierToken)
				|| (aKind == SyntaxKind.IdentifierToken && bIsKeyword)
				|| (aKind == SyntaxKind.LiteralToken && bKind == SyntaxKind.LiteralToken)
				|| (aKind == SyntaxKind.BangToken && bIsEquals)
				|| (aIsEquals && bIsEquals);
	}
	
	private static Stream<Arguments> getTokenPairsWithSeparator() {
		var tokens = getTokenData().toArray(SyntaxTuple[]::new);
		var separators = getSeparatorsData().toArray(SyntaxTuple[]::new);
		
		Stream.Builder<Arguments> result = Stream.builder();
		
		for (var a : tokens) {
			for (var b : tokens) {
				if (pairRequiresSeparator(a.kind, b.kind)) {
					for (var sep : separators) {
						result.accept(Arguments.of(a, sep, b));
					}
				}
			}
		}
		
		return result.build();
	}

	private static Stream<Arguments> getTokenPairsData() {
		var tokens = getTokenData().toArray(SyntaxTuple[]::new);
		
		Stream.Builder<Arguments> result = Stream.builder();

		for (var a : tokens) {
			for (var b : tokens) {
				if (!pairRequiresSeparator(a.kind, b.kind)) {
					result.accept(Arguments.of(a, b));
				}
			}
		}
		
		return result.build();
	}
	
	private static Stream<SyntaxTuple> getTokenData() {
		return Stream.concat(
				Stream.of(
						SyntaxKind.PlusToken,
						SyntaxKind.SlashToken, 
						SyntaxKind.StarToken, 
						SyntaxKind.MinusToken,
						SyntaxKind.OpenParenthesisToken, 
						SyntaxKind.CloseParenthesisToken, 
						SyntaxKind.BangToken,
						SyntaxKind.AmpersandAmpersandToken, 
						SyntaxKind.PipePipeToken, 
						SyntaxKind.EqualsEqualsToken,
						SyntaxKind.BangEqualsToken, 
						SyntaxKind.TrueKeyword, 
						SyntaxKind.FalseKeyword,
						SyntaxKind.EqualsToken)
					.map(kind -> new SyntaxTuple(kind, SyntaxFacts.getFixedText(kind))),
				Stream.of(
						new SyntaxTuple(SyntaxKind.IdentifierToken, "a"),
						new SyntaxTuple(SyntaxKind.IdentifierToken, "count"),
						new SyntaxTuple(SyntaxKind.LiteralToken, "2"),
						new SyntaxTuple(SyntaxKind.LiteralToken, "12")
				));
	}
	
	public static Stream<SyntaxTuple> getSeparatorsData() {
		return Stream.of(" ", "  ", "\t", "\r", "\n", "\r\n")
				.map(s -> new SyntaxTuple(SyntaxKind.WhitespaceToken, s));
	}

	@ParameterizedTest
	@MethodSource("getTokenData")
	void TestSingleTokens(SyntaxTuple a) {
		var tokens = SyntaxTree.parseTokens(a.text);
		
		assertEquals(1, tokens.size());
		var token = tokens.get(0);
		
		assertEquals(a.kind, token.getKind());
		assertEquals(a.text, token.getText());
	}
	
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
