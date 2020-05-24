package minsk.codeanalysis;

import java.util.Map;

import minsk.codeanalysis.binding.Binder;
import minsk.codeanalysis.binding.BoundGlobalScope;
import minsk.codeanalysis.binding.VariableSymbol;
import minsk.codeanalysis.syntax.SyntaxTree;
import minsk.diagnostics.DiagnosticsBag;

public class Compilation {

	private Compilation previous;
	private SyntaxTree syntax;
	private BoundGlobalScope globalScope;

	public Compilation(SyntaxTree syntax) {
		this(null, syntax);
	}

	public Compilation(Compilation previous, SyntaxTree syntax) {
		this.previous = previous;
		this.syntax = syntax;
		
		this.globalScope = Binder.bindGlobalScope(previous != null ? previous.globalScope : null, syntax.getRoot());
	}

	public EvaluationResult evaluate(Map<VariableSymbol, Object> variables) {
		var diagnostics = new DiagnosticsBag();
		diagnostics.addFrom(syntax, globalScope);
		
		if (!diagnostics.isEmpty()) {
			return new EvaluationResult(diagnostics, null);
		}
		
		var evaluator = new Evaluator(globalScope.getExpression(), variables);
		var value = evaluator.evaluate();
		
		return new EvaluationResult(diagnostics, value);
	}
	
	public SyntaxTree getSyntax() {
		return syntax;
	}

	public Compilation continueWith(SyntaxTree syntaxTree) {
		return new Compilation(this, syntaxTree);
	}
	
}
