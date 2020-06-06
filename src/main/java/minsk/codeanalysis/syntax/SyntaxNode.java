package minsk.codeanalysis.syntax;

import java.util.stream.Collectors;

import minsk.codeanalysis.TreeNode;
import minsk.codeanalysis.text.TextSpan;

public abstract class SyntaxNode extends TreeNode {
	public abstract SyntaxKind getKind();

	public TextSpan getSpan() {
		var children = getChildren().stream().filter(SyntaxNode.class::isInstance).map(SyntaxNode.class::cast).collect(Collectors.toList());
		
		if (children.isEmpty()) {
			return null;
		}
		
		var first = children.get(0).getSpan();
		var last = children.get(children.size() - 1).getSpan();
		
		return new TextSpan(first.getStart(), last.getEnd());
	}
	
	@Override
	public String toString() {
		return getKind().toString();
	}
}