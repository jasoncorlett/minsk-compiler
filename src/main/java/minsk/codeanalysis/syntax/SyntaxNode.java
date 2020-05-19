package minsk.codeanalysis.syntax;

import java.util.List;

public abstract class SyntaxNode {
	public abstract SyntaxKind getKind();
	public abstract List<SyntaxNode> getChildren();
	
	public SyntaxNode appendChild(SyntaxNode child) {
		getChildren().add(child);
		return this;
	}
}