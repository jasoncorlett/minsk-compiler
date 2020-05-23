package minsk.diagnostics;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import minsk.codeanalysis.syntax.SyntaxKind;
import minsk.codeanalysis.text.SourceText;
import minsk.codeanalysis.text.TextSpan;

public class DiagnosticsBag implements Iterable<Diagnostic> {
	private final List<Diagnostic> diagnostics = new LinkedList<>();

	private void report(TextSpan span, String message, Object... args) {
		var diagnostic = new Diagnostic(span, String.format(message, args));
		diagnostics.add(diagnostic);
	}

	public void addFrom(Diagnosable ...diagnosables) {
		for (var d : diagnosables) {
			diagnostics.addAll(d.getDiagnostics().diagnostics);
		}
	}

	public boolean isEmpty() {
		return diagnostics.isEmpty();
	}
	
	@Override
	public Iterator<Diagnostic> iterator() {
		return diagnostics.iterator();
	}

	public void reportInvalidNumber(TextSpan span, SourceText source, Class<?> type) {
		report(span, "The number '%s' is not a valid %s.", source, type);
	}

	public void reportBadCharacter(int position, char bad) {
		report(new TextSpan(position, position + 1), "Bad character input: '%c'.", bad);
	}


	public void reportUnexpectedToken(TextSpan span, SyntaxKind actual, SyntaxKind expected) {
		report(span, "Unexpected token %s expected %s.", actual, expected);
	}

	public void reportUndefinedUnaryOperator(TextSpan operatorSpan, String operatorText, Class<?> operandType) {
		report(operatorSpan, "Unary operator '%s' is not defined for %s.", operatorText, operandType);
	}

	public void reportUndefinedBinaryOperator(TextSpan operatorSpan, String operatorText, Class<?> leftType,
			Class<?> rightType) {
		report(operatorSpan, "Binary operator '%s' is not defined for types %s and %s", operatorText, leftType, rightType);
	}

	public void reportUndefinedName(TextSpan span, String name) {
		report(span, "Variable '%s' is not defined.", name);
	}
}
