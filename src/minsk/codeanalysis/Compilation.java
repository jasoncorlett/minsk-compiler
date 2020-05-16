package minsk.codeanalysis;

import minsk.codeanalysis.binding.Binder;
import minsk.codeanalysis.syntax.SyntaxTree;
import minsk.diagnostics.DiagnosticsBag;

public class Compilation {

	private final SyntaxTree syntax;

	public Compilation(SyntaxTree syntax) {
		this.syntax = syntax;
	}

	public EvaluationResult evaluate() {
		var binder = new Binder();
		var boundExpression = binder.bindExpression(syntax.getRoot());
		
		
		var diagnostics = new DiagnosticsBag();
		diagnostics.addFrom(syntax, binder);
		
		if (!diagnostics.isEmpty()) {
			return new EvaluationResult(diagnostics, null);
		}
		
		var evaluator = new Evaluator(boundExpression);
		var value = evaluator.evaluate();
		
		return new EvaluationResult(diagnostics, value);
	}
	
	public SyntaxTree getSyntax() {
		return syntax;
	}
	
}
