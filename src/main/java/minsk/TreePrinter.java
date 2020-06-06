package minsk;

import minsk.codeanalysis.TreeNode;

// https://en.wikipedia.org/wiki/Tree_(command)
class TreePrinter {
	private static final String INDENT_BLANK = "    ";
	private static final String INDENT_TREE  = "│   ";
	private static final String INDENT_CHILD = "├── ";
	private static final String INDENT_LAST  = "└── ";
	
	public static <T> void prettyPrint(TreeNode node) {
		prettyPrint(node, "", true);
	}

	private static <T> void prettyPrint(TreeNode node, String indent, boolean isLast) {
		var marker = isLast ? INDENT_LAST : INDENT_CHILD;
		
		System.out.print(indent);
		System.out.print(marker);
		System.out.print(node);
		
		System.out.println();
		
		indent += isLast ? INDENT_BLANK : INDENT_TREE;
		
		var iter = node.getChildren().iterator();
		
		while (iter.hasNext()) {
			var child = iter.next();
			prettyPrint(child, indent, !iter.hasNext());
		}
	}
}
