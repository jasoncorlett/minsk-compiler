package minsk.diagnostics;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface Diagnosable {
	public DiagnosticsCollection getDiagnostics();
	
	public static List<Diagnostic> asList(Diagnosable diagnosable) {
		return diagnosable.getDiagnostics().stream().collect(Collectors.toList());
	}
	
	public static <T> List<T> asList(Diagnosable diagnosable, Function<Diagnostic, T> transform) {
		return diagnosable.getDiagnostics().stream().map(transform).collect(Collectors.toList());
	}
}
