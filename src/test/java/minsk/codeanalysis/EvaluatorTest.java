package minsk.codeanalysis;

import static minsk.codeanalysis.Assertions.assertNoDiagnostics;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import minsk.diagnostics.Diagnosable;
import minsk.diagnostics.Diagnostic;

class EvaluatorTest {

	private static Stream<Arguments> TestExpressionResults() {
		return Stream.of(
			arguments("1", 1),
			arguments("+1", 1),
			arguments("-1", -1),
			arguments("1 + 2", 3),
			arguments("7 - 3", 4),
			arguments("4 /2", 2),
			arguments("5 * 3", 15),
			arguments("22 % 5", 2),
			arguments("10 % 2", 0),
			arguments("12 == 3", false),
			arguments("12 == 12", true),
			arguments("12 != 3", true),
			arguments("12 != 12", false),
			
			arguments("15 > 7", true),
			arguments("15 > 30", false),
			arguments("15 > 15", false),
			arguments("15 >= 7", true),
			arguments("15 >= 15", true),
			arguments("15 >= 30", false),
			arguments("7 < 15", true),
			arguments("7 < 5", false),
			arguments("7 < 7", false),
			arguments("7 <= 15", true),
			arguments("7 <= 5", false),
			arguments("7 <= 7", true),
			
			arguments("1 | 2", 3),
			arguments("1 | 0", 1),
			arguments("1 & 3", 1),
			arguments("1 & 0", 0),
			arguments("1 ^ 0", 1),
			arguments("0 ^ 1", 1),
			arguments("1 ^ 3", 2),
			
			arguments("true", true),
			arguments("false", false),
			arguments("!true", false),
			arguments("!false", true),
			arguments("true == true", true),
			arguments("true == false", false),
			arguments("false == false", true),
			arguments("false == true", false),
			arguments("true && true", true),
			arguments("true && false", false),
			arguments("false && false", false),
			arguments("false && true", false),
			arguments("true || true", true),
			arguments("true || false", true),
			arguments("false || false", false),
			arguments("false || true", true),
			arguments("false | false", false),
			arguments("false | true", true),
			arguments("true | false", true),
			arguments("true | true", true),
			arguments("false & false", false),
			arguments("false & true", false),
			arguments("true & false", false),
			arguments("true & true", true),
			arguments("false ^ false", false),
			arguments("false ^ true", true),
			arguments("true ^ false", true),
			arguments("true ^ true", false),
			
			arguments("{ var x = 10 (x+10)*x }", 200),
			arguments("if 5 < 10 1 else 0", 1),
			arguments("if 5 > 10 1 else 0", 0),
			arguments("if true  1 else 0", 1),
			arguments("if false 1 else 0", 0),
			arguments("{ var a = 5 var b = 0 if a - 5 == b { b = a } b}", 5),
			
			arguments("{ var res = 0 var i = 0 while i < 3 { i = i + 1 res = res + i } }", 6),
			arguments("{var res = 0 for i = 1 to 3 res = res + i res}", 6),
			arguments("{var a = 10 for i = 1 to (a = a - 1) {} a}", 9), // subtraction only happens once
			arguments("{var a = \"apple\" a}", "apple"),
			arguments("{var p = \"pine\" var a = \"apple\" if p + a == \"pineapple\" \"yes\" else \"no\"}", "yes"),
			arguments("{var a = \"apple\" if a != \"banana\" \"yes\" else \"no\"}", "yes"),
			arguments("{var p = \"Password\" var n = 123 p + n}", "Password123")
		);
	}
	
	@ParameterizedTest(name = "{0} = {1}")
	@MethodSource
	void TestExpressionResults(String text, Object expectedResult) {
		var actualResult = Evaluator.evaluateProgram(text);
		
		assertNoDiagnostics(actualResult);
		
		assertEquals(expectedResult, actualResult.getValue());
	}
	
	@Test
	public void TestEmptyProgram() {
		assertDiagnostics("[]", "Unexpected token 'EndOfFileToken' expected 'IdentifierToken'.");
	}
	
	@Test
	public void VariableAlreadyDeclared() {
		var text = """
				{
					let a = 5
					var b = 7
					{
						var a = 2
					}
					var [a] = 3
				}
				""";
		
		assertDiagnostics(text, "Variable 'a' is already declared.");
	}
	
	@Test
	public void NameReportsUndefined() {
		assertDiagnostics("[x] * 10", "Variable 'x' is not defined.");
	}
	
	@Test
	public void AssignReportsUndefined() {
		assertDiagnostics("[x] = 10", "Variable 'x' is not defined.");
	}
	
	@Test
	public void AssignReportsCannotAssign() {
		var text = """
				{
					let x = 10
					x [=] 0
				}
				""";
		
		assertDiagnostics(text, "Cannot assign variable 'x'.");
	}
	
	@Test
	public void AssignReportsCannotConvert() {
		var text = """
				{
					var z = 5
					z = [true]
				}
				""";
		
		assertDiagnostics(text, "Cannot convert from 'bool' to 'int'.");
	}
	
	@Test
	public void UnaryOperatorReportsUndefined() {
		assertDiagnostics("[+]true", "Unary operator '+' is not defined for 'bool'.");
	}
	
	@Test
	public void BinaryOperatorReportsUndefined() {
		assertDiagnostics("10 [*] true", "Binary operator '*' is not defined for types 'int' and 'bool'.");
	}
	
	private static void assertDiagnostics(String program, String... expectedMessages) {
		var annotated = AnnotatedText.parse(program);
		var result = Evaluator.evaluateProgram(annotated.getText());

		var expectedSpans = annotated.getSpans();
		
		var actualSpans = Diagnosable.asList(result, Diagnostic::getSpan);
		var actualMessages = Diagnosable.asList(result, Diagnostic::getMessage);
		
		assertEquals(expectedSpans, actualSpans, String.join("\n", actualMessages));
		assertEquals(Arrays.asList(expectedMessages), actualMessages);
	}
}
