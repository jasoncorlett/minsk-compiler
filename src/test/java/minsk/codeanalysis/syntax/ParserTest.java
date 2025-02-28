package minsk.codeanalysis.syntax;

import static minsk.codeanalysis.Assertions.assertNoDiagnostics;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import minsk.codeanalysis.AnnotatedText;
import minsk.codeanalysis.TreeNode;
import minsk.codeanalysis.syntax.lexer.SyntaxToken;
import minsk.codeanalysis.syntax.parser.ExpressionStatementSyntax;
import minsk.codeanalysis.syntax.parser.ExpressionSyntax;
import minsk.diagnostics.Diagnosable;
import minsk.diagnostics.Diagnostic;

class ParserTest {

	/**
	 * Test infinite loop protection in block statement parsing
	 */
	@Test
	public void TestInfiniteLoopProtection() {
		assertTimeoutPreemptively(
				Duration.ofMillis(250),
				() -> assertDiagnostics(
						"{[)][]", 
						"Unexpected token 'CloseParenthesisToken' expected 'IdentifierToken'.",
						"Unexpected token 'EndOfFileToken' expected 'CloseBraceToken'."),
				() -> "Suspected infinite loop in parser");
	}
	
	public static Stream<Arguments> BinaryPrecedenceTest() {
		return SyntaxFacts.getBinaryOperatorStream()
				.flatMap(a -> SyntaxFacts.getBinaryOperatorStream().map(b -> Arguments.of(a, b)));
	}
	
	/**
	 * Theory - parsing trees should produce trees with valid precedence
	 * 
	 * @param op1
	 * @param op2
	 */
	@ParameterizedTest
	@MethodSource
	public void BinaryPrecedenceTest(SyntaxKind op1, SyntaxKind op2) {
		var op1Precedence = op1.getBinaryPrecedence();
		var op2Precedence = op2.getBinaryPrecedence(); 
		
		var op1Text = op1.getFixedText();
		var op2Text = op2.getFixedText();
		
		var root = assertParses("a %s b %s c", op1Text, op2Text);

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
	
	public static Stream<Arguments> UnaryPrecedenceTest() {
		return SyntaxFacts.getUnaryOperatorStream()
				.flatMap(a -> SyntaxFacts.getBinaryOperatorStream().map(b -> Arguments.of(a, b)));
	}
	
	@ParameterizedTest
	@MethodSource
	public void UnaryPrecedenceTest(SyntaxKind unaryKind, SyntaxKind binaryKind) {
		
		var unaryPrecedence = unaryKind.getUnaryPrecedence();
		var binaryPrecedence = binaryKind.getBinaryPrecedence();
		
		var unaryText = unaryKind.getFixedText();
		var binaryText = binaryKind.getFixedText();
		
		var root = assertParses("%s a %s b", unaryText, binaryText);
		
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
	
	private static ExpressionSyntax assertParses(String fmt, Object ...args) {
		var text = String.format(fmt, args);
		var tree = SyntaxTree.parse(text);
		
		assertNoDiagnostics(tree);

		var statement = (ExpressionStatementSyntax) tree.getRoot().getStatement();
		
		return statement.getExpression();
	}
	
	private static void assertDiagnostics(String program, String... expectedMessages) {
		var annotated = AnnotatedText.parse(program);
		var result = SyntaxTree.parse(annotated.getText());

		var expectedSpans = annotated.getSpans();
		
		var actualSpans = Diagnosable.asList(result, Diagnostic::getSpan);
		var actualMessages = Diagnosable.asList(result, Diagnostic::getMessage);
		
		assertEquals(expectedSpans, actualSpans);
		assertEquals(Arrays.asList(expectedMessages), actualMessages);
	}
	
	private static List<TreeNode> flatten(TreeNode node) {
		var list = new LinkedList<TreeNode>();
		list.add(node);
		
		for (var child : node.getChildren()) {
			list.addAll(flatten(child));
		}
		
		return list;
	}
	
	private class TreeAsserter {
		final Iterator<SyntaxNode> iter;
		
		TreeAsserter(SyntaxNode node) {
			iter = flatten(node).stream().filter(SyntaxNode.class::isInstance).map(SyntaxNode.class::cast).iterator();
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
