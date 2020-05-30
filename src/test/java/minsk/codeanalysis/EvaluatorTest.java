package minsk.codeanalysis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import minsk.diagnostics.Diagnosable;
import minsk.diagnostics.Diagnostic;
import static minsk.codeanalysis.Assertions.assertNoDiagnostics;
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
			
			arguments("{ var x = 10 (x+10)*x }", 200),
			arguments("if 5 < 10 1 else 0", 1),
			arguments("if 5 > 10 1 else 0", 0),
			arguments("if true  1 else 0", 1),
			arguments("if false 1 else 0", 0),
			arguments("{ var a = 5 var b = 0 if a - 5 == b { b = a } b}", 5)
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
		
		assertDiagnostics(text, "Cannot convert from 'Boolean' to 'Integer'.");
	}
	
	@Test
	public void UnaryOperatorReportsUndefined() {
		assertDiagnostics("[+]true", "Unary operator '+' is not defined for 'Boolean'.");
	}
	
	@Test
	public void BinaryOperatorReportsUndefined() {
		assertDiagnostics("10 [*] true", "Binary operator '*' is not defined for types 'Integer' and 'Boolean'.");
	}
	
	private static void assertDiagnostics(String program, String... expectedMessages) {
		var annotated = AnnotatedText.parse(program);
		var result = Evaluator.evaluateProgram(annotated.getText());

		var expectedSpans = annotated.getSpans();
		
		var actualSpans = makeList(result, Diagnostic::getSpan);
		var actualMessages = makeList(result, Diagnostic::getMessage);
		
		assertEquals(expectedSpans, actualSpans);
		assertEquals(Arrays.asList(expectedMessages), actualMessages);
	}
	
	private static <T> List<T> makeList(Diagnosable source, Function<Diagnostic, T> mapper) {
		return source.getDiagnostics().stream().map(mapper).collect(Collectors.toList());
	}
}
