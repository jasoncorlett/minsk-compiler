package minsk;

import java.io.IOException;
import java.util.Scanner;
import minsk.codeanalysis.*;

// https://www.youtube.com/watch?v=wgHIkdUQbp0

public class Main {
	
	// https://en.wikipedia.org/wiki/Tree_(command)
	private static final String INDENT_BLANK = "    ";
	private static final String INDENT_TREE  = "│   ";
	private static final String INDENT_CHILD = "├── ";
	private static final String INDENT_LAST  = "└── ";
	
	private static final String SHOW_TREE_CMD = "#showtree";
	private static final String SHOW_TOKENS_CMD = "#showtokens";
	private static final String CLEAR_SCREEN_CMD = "#clear";
	
	private boolean showTokens = false;
	private boolean showTree = false;
	
	static void prettyIndent(final int level, final boolean isLast, final boolean isParentLast) {
		for (int i = level; i > 2; i--) {
			System.out.print(INDENT_TREE);
		}

		if (level > 1) {
			System.out.print(isParentLast ? INDENT_BLANK : INDENT_TREE);
		}
		
		if (level > 0) {
			System.out.print(isLast ? INDENT_LAST : INDENT_CHILD);
		}
	}
	
	static void prettyPrint(SyntaxNode node) {
		prettyPrint(node, 0, false, false);
	}
	
	static void prettyPrint(SyntaxNode node, final int indent, final boolean isLast, final boolean isParentLast) {
		var children = node.getChildren().iterator();

		prettyIndent(indent, isLast, isParentLast);
		System.out.print(node.getKind());
		
		if (node instanceof SyntaxToken) {
			SyntaxToken token = (SyntaxToken) node;
			
			if (token.getValue() != null)
				System.out.print(" " + token.getValue());
		}
		
		System.out.println();
		
		while (children.hasNext()) {
			var child = children.next();
			prettyPrint(child, indent + 1, !children.hasNext(), isLast);
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
	
	public void run() {
		try (Scanner sc = new Scanner(System.in)) {
			while (true) {
				System.out.print("> ");
				
				var line = sc.nextLine();
				
				if (SHOW_TREE_CMD.equalsIgnoreCase(line)) {
					showTree = !showTree;
					System.out.println((showTree ? "" : "Not ") + "Showing Parse Trees");
					continue;
				}
				
				if (SHOW_TOKENS_CMD.equalsIgnoreCase(line) ) {
					showTokens = !showTokens;
					System.out.println((showTokens ? "" : "Not ") + "Showing Tokens");
					continue;
				}
				
				// TODO: Doesn't really work in eclipse terminal
				// TODO: Linux
				if (CLEAR_SCREEN_CMD.equalsIgnoreCase(line)) {
					try {
						Runtime.getRuntime().exec("cmd /c cls");
					} catch (IOException e) {
						e.printStackTrace();
					}
					continue;
				}
				
				var parser = new Parser(line);
				
				if (showTokens) {
					System.out.print("[ ");
					parser.tokens.forEach(a -> System.out.print("<" + a + "> "));
					System.out.println("]");	
				}
				
				var expr = parser.parse();
				
				if (showTree) {
					prettyPrint(expr.getRoot());	
				}
				
				parser.diagnostics.forEach(System.err::println);
				
				System.out.println(new Evaluator(expr.getRoot()).evaluate());
			}
		}
	}
}
