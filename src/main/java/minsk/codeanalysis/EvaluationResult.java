package minsk.codeanalysis;

import minsk.diagnostics.Diagnosable;
import minsk.diagnostics.DiagnosticsCollection;

public class EvaluationResult implements Diagnosable {
	
	private final DiagnosticsCollection diagnostics;
	private final Object value;

	public EvaluationResult(DiagnosticsCollection diagnostics, Object value) {
		this.diagnostics = diagnostics;
		this.value = value;
	}

	@Override
	public DiagnosticsCollection getDiagnostics() {
		return diagnostics;
	}

	public Object getValue() {
		return value;
	}

}
