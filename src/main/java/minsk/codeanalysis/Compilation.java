package minsk.codeanalysis;

import java.util.Map;

import minsk.codeanalysis.binding.Binder;
import minsk.codeanalysis.binding.VariableSymbol;
import minsk.codeanalysis.syntax.SyntaxTree;
import minsk.diagnostics.DiagnosticsBag;

public class Compilation {

	private final SyntaxTree syntax;

	public Compilation(SyntaxTree syntax) {
		this.syntax = syntax;
	}

	public EvaluationResult evaluate(Map<VariableSymbol, Object> variables) {
		var binder = new Binder(variables);
		var boundExpression = binder.bindExpression(syntax.getRoot());
		
		var diagnostics = new DiagnosticsBag();
		diagnostics.addFrom(syntax, binder);
		
		if (!diagnostics.isEmpty()) {
			return new EvaluationResult(diagnostics, null);
		}
		
		var evaluator = new Evaluator(boundExpression, variables);
		var value = evaluator.evaluate();
		
		return new EvaluationResult(diagnostics, value);
	}
	
	public SyntaxTree getSyntax() {
		return syntax;
	}
	
}
