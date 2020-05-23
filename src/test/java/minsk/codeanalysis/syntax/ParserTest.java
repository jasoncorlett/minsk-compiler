package minsk.codeanalysis.syntax;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ParserTest {
	/**
	 * Theory - parsing trees should produce trees with valid precedence
	 * 
	 * @param op1
	 * @param op2
	 */
	@ParameterizedTest
	@MethodSource("getBinaryOperatorPairsData")
	public void ParserBinaryExpressionHonoursPrecedencesTest(SyntaxKind op1, SyntaxKind op2) {
		var op1Precedence = SyntaxFacts.lookupBinaryOperatorPrecedence(op1);
		var op2Precedence = SyntaxFacts.lookupBinaryOperatorPrecedence(op2);
		
		var op1Text = SyntaxFacts.getFixedText(op1);
		var op2Text = SyntaxFacts.getFixedText(op2);
		
		var text = String.format("a %s b %s c", op1Text, op2Text);
		var expression = SyntaxTree.parse(text);
		var root = expression.getRoot();
		
		if (!expression.getDiagnostics().isEmpty()) {
			fail(StreamSupport.stream(expression.getDiagnostics().spliterator(), false).map(d -> d.getMessage())
					.collect(Collectors.joining("\n")));
		}
		
		//     op1
		//    /   \
		//   op2   c
		//  /   \
		// a     b
		if (op1Precedence >= op2Precedence) {
			new TreeAsserter(root)
			.assertNode(SyntaxKind.BinaryExpression)
				.assertNode(SyntaxKind.BinaryExpression)
					.assertNode(SyntaxKind.NameExpression)
						.assertToken(SyntaxKind.IdentifierToken, "a")
					.assertToken(op1, op1Text)
					.assertNode(SyntaxKind.NameExpression)
						.assertToken(SyntaxKind.IdentifierToken, "b")
				.assertToken(op2, op2Text)
				.assertNode(SyntaxKind.NameExpression)
					.assertToken(SyntaxKind.IdentifierToken, "c")
			.assertEmtpy();
			
		//    op1
		//   /  \
		//  a   op2
		//      / \
		//     b   c
		} else {
			new TreeAsserter(root)
			.assertNode(SyntaxKind.BinaryExpression)
				.assertNode(SyntaxKind.NameExpression)
					.assertToken(SyntaxKind.IdentifierToken, "a")
				.assertToken(op1, op1Text)
				.assertNode(SyntaxKind.BinaryExpression)
					.assertNode(SyntaxKind.NameExpression)
						.assertToken(SyntaxKind.IdentifierToken, "b")
					.assertToken(op2, op2Text)
					.assertNode(SyntaxKind.NameExpression)
						.assertToken(SyntaxKind.IdentifierToken, "c")
			.assertEmtpy();					
		}
		
	}
	
	@ParameterizedTest
	@MethodSource("getUnaryOperatorPairsData")
	public void ParserUnaryExpressionHonoursPrecedencesTest(SyntaxKind unaryKind, SyntaxKind binaryKind) {
		
		var unaryPrecedence = SyntaxFacts.lookupUnaryOperatorPrecedence(unaryKind);
		var binaryPrecedence = SyntaxFacts.lookupBinaryOperatorPrecedence(binaryKind);
		
		var unaryText = SyntaxFacts.getFixedText(unaryKind);
		var binaryText = SyntaxFacts.getFixedText(binaryKind);
		
		var text = String.format("%s a %s b", unaryText, binaryText);
		
		var expression = SyntaxTree.parse(text);
		
		if (!expression.getDiagnostics().isEmpty()) {
			fail(StreamSupport.stream(expression.getDiagnostics().spliterator(), false).map(d -> d.getMessage())
					.collect(Collectors.joining("\n")));
		}
		
		var root = expression.getRoot();
		
		//       binary
		//      /      \
		//   unary      b
		//     |
		//     a
		if (unaryPrecedence >= binaryPrecedence) {
			new TreeAsserter(root)
			.assertNode(SyntaxKind.BinaryExpression)
				.assertNode(SyntaxKind.UnaryExpression)
					.assertToken(unaryKind, unaryText)
					.assertNode(SyntaxKind.NameExpression)
						.assertToken(SyntaxKind.IdentifierToken, "a")
				.assertToken(binaryKind, binaryText)
				.assertNode(SyntaxKind.NameExpression)
					.assertToken(SyntaxKind.IdentifierToken, "b")
			.assertEmtpy();
			
		//    unary
		//      |
		//    binary
		//   /      \
		//  a        b
		} else {
			fail("Unary operator should always have higher precedence");
//			new TreeAsserter(root)
//			.assertNode(SyntaxKind.UnaryExpression)
//				.assertToken(unaryKind, unaryText)
//				.assertNode(SyntaxKind.BinaryExpression)
//					.assertNode(SyntaxKind.NameExpression)
//						.assertToken(SyntaxKind.IdentifierToken, "a")
//					.assertToken(binaryKind, binaryText)
//					.assertNode(SyntaxKind.NameExpression)
//						.assertToken(SyntaxKind.IdentifierToken, "b")
//			.assertEmtpy();
		}
	}

	public static Stream<Arguments> getBinaryOperatorPairsData() {
		return SyntaxFacts.getBinaryOperatorKinds().stream()
				.flatMap(a -> SyntaxFacts.getBinaryOperatorKinds().stream().map(b -> Arguments.of(a, b)));
	}
	
	public static Stream<Arguments> getUnaryOperatorPairsData() {
		return SyntaxFacts.getUnaryOperatorKinds().stream()
				.flatMap(a -> SyntaxFacts.getBinaryOperatorKinds().stream().map(b -> Arguments.of(a, b)));
	}
	
	public static List<SyntaxNode> flatten(SyntaxNode node) {
		var list = new LinkedList<SyntaxNode>();
		list.add(node);
		
		for (var child : node.getChildren()) {
			list.addAll(flatten(child));
		}
		
		return list;
	}
	
	class TreeAsserter {
		final Iterator<SyntaxNode> iter;
		
		TreeAsserter(SyntaxNode node) {
			iter = flatten(node).iterator();
		}
		
		public TreeAsserter assertToken(SyntaxKind expectedKind, String expectedText) {
			assertTrue(iter.hasNext(), "Ran out of nodes looking for " + expectedKind + " " + expectedText);
			
			var actual = iter.next();
			assertEquals(expectedKind, actual.getKind());
			assertTrue(actual instanceof SyntaxToken, "" + actual.getKind() + " is not a SyntaxToken.");
			
			var token = (SyntaxToken) actual;
			assertEquals(expectedText, token.getText());
			
			return this;
		}
		
		public TreeAsserter assertNode(SyntaxKind expectedKind) {
			assertTrue(iter.hasNext(), "Ran out of nodes looking for " + expectedKind);
			
			var actual = iter.next();
			assertEquals(expectedKind, actual.getKind());
			
			return this;
		}
		
		public void assertEmtpy() {
			assertFalse(iter.hasNext(), "Iterator should be emtpy");
		}
	}	
}
