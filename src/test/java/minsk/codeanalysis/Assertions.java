package minsk.codeanalysis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Collection;
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

	public static <T> T assertSingle(Collection<T> coll) {
		assertEquals(1, coll.size(), "Collection should have exactly one item");
		return coll.stream().findFirst().get();
	}

	public static <T> T assertSingle(Iterable<T> iterable) {
		var iter = iterable.iterator();

		assertTrue(iter.hasNext(), "Iterable requires exactly one element, has none");

		var element = iter.next();

		assertFalse(iter.hasNext(), "Iterable requires exactly one element, has at least two");

		return element;
	}
}
