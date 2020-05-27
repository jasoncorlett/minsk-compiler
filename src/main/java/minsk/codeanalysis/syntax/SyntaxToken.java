package minsk.codeanalysis.syntax;

import minsk.codeanalysis.text.TextSpan;

public record SyntaxToken(SyntaxKind kind, int position, String text, Object value) implements SyntaxNode {
	@Override
	public TextSpan getSpan() {
		var length = (text != null) ? text.length() : 0;
		return new TextSpan(position, position + length);
	}
	
	@Override
	public String toString() {
		return "" + this.kind().toString() + ((this.value() != null) ? " " + this.value() : "");
	}
}