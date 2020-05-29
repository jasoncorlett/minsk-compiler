package minsk.codeanalysis;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.stream.Collectors;

import minsk.diagnostics.Diagnosable;
import minsk.diagnostics.Diagnostic;

public class Assertions {

	private static String combineDiagnostics(Diagnosable source) {
		return source.getDiagnostics().stream().map(Diagnostic::getMessage).collect(Collectors.joining("\n"));
	}
	
	public static void assertNoDiagnostics(Diagnosable source) {
		assertNoDiagnostics(source, "Expected no diagnostics on [" + source.getClass().getSimpleName() + "]");
	}
	
	public static void assertNoDiagnostics(Diagnosable source, String message) {
		if (!source.getDiagnostics().isEmpty())
			fail(message + " " + combineDiagnostics(source));
	}
	
}
