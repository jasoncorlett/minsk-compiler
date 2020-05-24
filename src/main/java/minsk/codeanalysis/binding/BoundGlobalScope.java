package minsk.codeanalysis.binding;

import java.util.Collection;

import minsk.diagnostics.Diagnosable;
import minsk.diagnostics.DiagnosticsBag;

public class BoundGlobalScope implements Diagnosable {
	private final BoundGlobalScope previous;
	private final DiagnosticsBag diagnostics;
	private final Collection<VariableSymbol> variables;
	private final BoundExpression expression;

	public BoundGlobalScope(BoundGlobalScope previous, DiagnosticsBag diagnostics, Collection<VariableSymbol> variables, BoundExpression expression) {
		this.previous = previous;
		this.diagnostics = diagnostics;
		this.variables = variables;
		this.expression = expression;
	}

	@Override
	public DiagnosticsBag getDiagnostics() {
		return diagnostics;
	}

	public BoundExpression getExpression() {
		return expression;
	}

	public BoundGlobalScope getPrevious() {
		return previous;
	}

	public Collection<VariableSymbol> getVariables() {
		return variables;
	}
	
	
}
