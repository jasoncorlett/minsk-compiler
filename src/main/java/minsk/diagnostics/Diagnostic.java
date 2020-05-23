package minsk.diagnostics;

import minsk.codeanalysis.text.TextSpan;

public class Diagnostic {
	private final TextSpan span;
	private final String message;

	public Diagnostic(TextSpan span, String message) {
		if (message == null) {
			throw new NullPointerException("Message must not be null");
		}

		this.span = span;
		this.message = message;
	}

	@Override
	public String toString() {
		if (span != null) {
			return String.format("[%d] %s", span.getStart(), message);
		}

		return message;
	}

	public TextSpan getSpan() {
		return span;
	}

	public String getMessage() {
		return message;
	}

}
