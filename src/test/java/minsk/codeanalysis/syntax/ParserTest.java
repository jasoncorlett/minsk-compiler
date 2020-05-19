package minsk.codeanalysis.syntax;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

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
	@MethodSource("getBinaryOperatorData")
	public void ParserBinaryExpressionHonoursPrecedencesTest(SyntaxKind op1, SyntaxKind op2) {
		var op1Precedence = SyntaxFacts.lookupBinaryOperatorPrecedence(op1);
		var op2Precedence = SyntaxFacts.lookupBinaryOperatorPrecedence(op2);
		
		var op1Text = SyntaxFacts.getFixedText(op1);
		var op2Text = SyntaxFacts.getFixedText(op2);
		
		var text = String.format("a %s b %s c", op1Text, op2Text);
		var tree = SyntaxTree.parse(text);
		var root = tree.getRoot();
		
		assertTrue(tree.getDiagnostics().isEmpty(), "No diagnostic messages should be found");
		
		
		
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

	public static Stream<Arguments> getBinaryOperatorData() {
		return SyntaxFacts.getBinaryOperatorKinds().stream()
				.flatMap(a -> SyntaxFacts.getBinaryOperatorKinds().stream().map(b -> Arguments.of(a, b)));
	}
	
	public static Stream<Arguments> getUnaryOperatorData() {
		return SyntaxFacts.getUnaryOperatorKinds().stream()
				.flatMap(a -> SyntaxFacts.getUnaryOperatorKinds().stream().map(b -> Arguments.of(a, b)));
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
