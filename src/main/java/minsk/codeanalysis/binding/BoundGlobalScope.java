package minsk.codeanalysis.binding;

import java.util.Collection;

import minsk.diagnostics.Diagnosable;
import minsk.diagnostics.DiagnosticsBag;

public class BoundGlobalScope implements Diagnosable {
	private final BoundGlobalScope previous;
	private final DiagnosticsBag diagnostics;
	private final Collection<VariableSymbol> variables;
	private final BoundStatement statement;

	public BoundGlobalScope(BoundGlobalScope previous, DiagnosticsBag diagnostics, Collection<VariableSymbol> variables, BoundStatement statement) {
		this.previous = previous;
		this.diagnostics = diagnostics;
		this.variables = variables;
		this.statement = statement;
	}

	@Override
	public DiagnosticsBag getDiagnostics() {
		return diagnostics;
	}

	public BoundStatement getStatement() {
		return statement;
	}

	public BoundGlobalScope getPrevious() {
		return previous;
	}

	public Collection<VariableSymbol> getVariables() {
		return variables;
	}
	
	
}
