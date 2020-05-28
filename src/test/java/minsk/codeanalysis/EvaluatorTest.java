package minsk.codeanalysis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import minsk.codeanalysis.binding.VariableSymbol;
import minsk.codeanalysis.syntax.SyntaxTree;

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
			arguments("12 == 3", false),
			arguments("12 == 12", true),
			arguments("12 != 3", true),
			arguments("12 != 12", false),
			
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
			
			arguments("{ var x = 10 (x+10)*x }", 200)
		);
	}
	
	@ParameterizedTest(name = "{0} = {1}")
	@MethodSource
	void TestExpressionResults(String text, Object expectedResult) {
		var tree = SyntaxTree.parse(text);
		var compilation = new Compilation(tree);
		var variables = new HashMap<VariableSymbol, Object>();
		var actualResult = compilation.evaluate(variables);
		
		if (!actualResult.getDiagnostics().isEmpty()) {
			fail(StreamSupport.stream(actualResult.getDiagnostics().spliterator(), false).map(d -> d.getMessage())
					.collect(Collectors.joining("\n")));
		}
		assertEquals(expectedResult, actualResult.getValue());
	}
}
