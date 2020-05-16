package minsk.codeanalysis.syntax;

public abstract class SyntaxNode {
	public abstract SyntaxKind getKind();
	public abstract Iterable<SyntaxNode> getChildren();
}