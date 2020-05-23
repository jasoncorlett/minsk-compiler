package minsk.codeanalysis.syntax;

import minsk.codeanalysis.text.TextSpan;

public class SyntaxToken implements SyntaxNode {
	private final int position;
	private final String text;
	private final Object value;
	private final SyntaxKind kind;
	
	public SyntaxToken(SyntaxKind kind, int position, String text, Object value) {
		this.kind = kind;
		this.position = position;
		this.text = text;
		this.value = value;
	}
	
	public int getPosition() {
		return position;
	}

	public String getText() {
		return text;
	}

	public Object getValue() {
		return value;
	}
	
	@Override
	public TextSpan getSpan() {
		return new TextSpan(position, position + text.length());
	}
	
	@Override
	public String toString() {
		return "" + this.getKind().toString() + ((this.getValue() != null) ? " " + this.getValue() : "");
	}

	@Override
	public SyntaxKind getKind() {
		return kind;
	}
}