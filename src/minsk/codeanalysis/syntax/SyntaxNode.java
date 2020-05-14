package minsk.codeanalysis.syntax;

public abstract class SyntaxNode {
	private final SyntaxKind kind;
	
	public SyntaxNode(SyntaxKind kind) {
		this.kind = kind;
	}
	
	public SyntaxKind getKind() {
		return kind;
	}

	public abstract Iterable<SyntaxNode> getChildren();
}