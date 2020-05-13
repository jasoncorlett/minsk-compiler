package minsk.codeanalysis;

import java.util.ArrayList;

public class SyntaxToken extends SyntaxNode {
	public final int position;
	public final String text;
	public final Object value;
	
	public SyntaxToken(SyntaxKind kind, int position, String text, Object value) {
		super(kind);
		this.position = position;
		this.text = text;
		this.value = value;
	}
	
	@Override
	public Iterable<SyntaxNode> getChildren() {
		return new ArrayList<SyntaxNode>();
	}
	
	@Override
	public String toString() {
		return "" + this.kind.toString() + ((this.value != null) ? " " + this.value : "");
	}
}