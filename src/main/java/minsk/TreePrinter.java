package minsk;

import java.util.List;

import minsk.codeanalysis.syntax.SyntaxNode;
import minsk.codeanalysis.syntax.SyntaxToken;

class TreePrinter {
	private static final String INDENT_BLANK = "    ";
	private static final String INDENT_TREE  = "│   ";
	private static final String INDENT_CHILD = "├── ";
	private static final String INDENT_LAST  = "└── ";
	
	public static void prettyPrint(SyntaxNode node) {
		prettyPrint(node, "", true);
	}

	private static void prettyPrint(SyntaxNode node, String indent, boolean isLast) {
		var marker = isLast ? INDENT_LAST : INDENT_CHILD;
		
		List.of((Object)indent, marker, node.getKind()).forEach(System.out::print);
		
		if (node instanceof SyntaxToken) {
			var token = (SyntaxToken) node;
			
			if (token.getValue() != null) {
				System.out.print(" ");
				System.out.print(token.getValue());
			}
		}
		
		System.out.println();
		
		indent += isLast ? INDENT_BLANK : INDENT_TREE;
		
		var iter = node.getChildren().iterator();
		
		while (iter.hasNext()) {
			var child = iter.next();
			prettyPrint(child, indent, !iter.hasNext());
		}
	}
}
