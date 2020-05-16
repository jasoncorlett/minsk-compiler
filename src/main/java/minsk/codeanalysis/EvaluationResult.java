package minsk.codeanalysis;

import minsk.diagnostics.Diagnosable;
import minsk.diagnostics.DiagnosticsBag;

public class EvaluationResult implements Diagnosable {
	
	private final DiagnosticsBag diagnostics;
	private final Object value;

	public EvaluationResult(DiagnosticsBag diagnostics, Object value) {
		this.diagnostics = diagnostics;
		this.value = value;
	}

	@Override
	public DiagnosticsBag getDiagnostics() {
		return diagnostics;
	}

	public Object getValue() {
		return value;
	}

}
