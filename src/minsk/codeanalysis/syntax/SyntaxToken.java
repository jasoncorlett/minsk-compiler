package minsk.codeanalysis.syntax;

import java.util.ArrayList;

import minsk.codeanalysis.TextSpan;

public class SyntaxToken extends SyntaxNode {
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
	
	public TextSpan getSpan() {
		return new TextSpan(position, position + text.length());
	}
	
	@Override
	public Iterable<SyntaxNode> getChildren() {
		return new ArrayList<SyntaxNode>();
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