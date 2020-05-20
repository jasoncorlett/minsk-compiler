package minsk.codeanalysis.syntax;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import minsk.codeanalysis.Compilation;
import minsk.codeanalysis.binding.VariableSymbol;

class EvaluatorTest {

	@ParameterizedTest(name = "{0} = {1}")
	@MethodSource("getExpressionData")
	void TestIntegerExpression(String text, Object expectedResult) {
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
	
	private static Arguments a(String expr, Object result) {
		return Arguments.of(expr, result);
	}

	private static Stream<Arguments> getExpressionData() {
		return Stream.of(
			a("1", 1),
			a("+1", 1),
			a("-1", -1),
			a("1 + 2", 3),
			a("7 - 3", 4),
			a("4 /2", 2),
			a("5 * 3", 15),
			a("12 == 3", false),
			a("12 == 12", true),
			a("12 != 3", true),
			a("12 != 12", false),
			
			a("true", true),
			a("false", false),
			a("!true", false),
			a("!false", true),
			a("true == true", true),
			a("true == false", false),
			a("false == false", true),
			a("false == true", false),
			a("true && true", true),
			a("true && false", false),
			a("false && false", false),
			a("false && true", false),
			a("true || true", true),
			a("true || false", true),
			a("false || false", false),
			a("false || true", true),
			
			a("(a = 10) * a", 100)
		);
	}
}
