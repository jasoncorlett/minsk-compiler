package minsk.codeanalysis;

public abstract class SyntaxNode {
	public final SyntaxKind kind;
	
	public SyntaxNode(SyntaxKind kind) {
		this.kind = kind;
	}
	
	public abstract Iterable<SyntaxNode> getChildren();
}