package minsk.codeanalysis;

import java.util.ArrayList;

public class SyntaxToken extends SyntaxNode {
	private final int position;
	private final String text;
	private final Object value;
	
	public SyntaxToken(SyntaxKind kind, int position, String text, Object value) {
		super(kind);
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
	public Iterable<SyntaxNode> getChildren() {
		return new ArrayList<SyntaxNode>();
	}
	
	@Override
	public String toString() {
		return "" + this.getKind().toString() + ((this.getValue() != null) ? " " + this.getValue() : "");
	}
}