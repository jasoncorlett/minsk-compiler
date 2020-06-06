package minsk.codeanalysis;

import java.util.Map;

import minsk.codeanalysis.binding.Binder;
import minsk.codeanalysis.binding.BoundBlockStatement;
import minsk.codeanalysis.binding.BoundGlobalScope;
import minsk.codeanalysis.binding.BoundNode;
import minsk.codeanalysis.binding.BoundStatement;
import minsk.codeanalysis.binding.VariableSymbol;
import minsk.codeanalysis.lowering.Lowerer;
import minsk.codeanalysis.syntax.SyntaxTree;
import minsk.diagnostics.DiagnosticsCollection;

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
		var diagnostics = new DiagnosticsCollection();
		diagnostics.addFrom(syntax, globalScope);
		
		if (!diagnostics.isEmpty()) {
			return new EvaluationResult(diagnostics, null);
		}
		
		var statement = getStatement();
		var evaluator = new Evaluator(statement, variables);
		var value = evaluator.evaluate();
		
		return new EvaluationResult(diagnostics, value);
	}
	
	private BoundBlockStatement getStatement() {
		return Lowerer.lower(globalScope.getStatement());
	}
	
	public BoundBlockStatement getBoundNode() {
		return getStatement();
	}
	
	public SyntaxTree getSyntax() {
		return syntax;
	}

	public Compilation continueWith(SyntaxTree syntaxTree) {
		return new Compilation(this, syntaxTree);
	}
	
}
