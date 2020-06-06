package minsk.codeanalysis.binding;

import minsk.codeanalysis.TreeNode;

public abstract class BoundNode extends TreeNode {
	public abstract BoundNodeKind getKind();
	
	@Override
	public String toString() {
		return getKind().toString();
	}
}