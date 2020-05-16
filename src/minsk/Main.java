package minsk;

import java.io.IOException;
import java.util.Scanner;
import minsk.codeanalysis.*;
import minsk.codeanalysis.syntax.Parser;
import minsk.codeanalysis.syntax.SyntaxNode;
import minsk.codeanalysis.syntax.SyntaxToken;

// https://www.youtube.com/watch?v=wgHIkdUQbp0

public class Main {
	// https://en.wikipedia.org/wiki/Tree_(command)
	private static final String INDENT_BLANK = "    ";
	private static final String INDENT_TREE  = "│   ";
	private static final String INDENT_CHILD = "├── ";
	private static final String INDENT_LAST  = "└── ";
	
	private static final String SHOW_TREE_CMD = "#showtree";
	private static final String CLEAR_SCREEN_CMD = "#clear";
	private static final String QUIT_CMD = "#quit";
	
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
				
				if (line.isEmpty())
					continue;

				if (QUIT_CMD.equalsIgnoreCase(line)) {
					break;
				}
				
				if (SHOW_TREE_CMD.equalsIgnoreCase(line)) {
					showTree = !showTree;
					System.out.println((showTree ? "" : "Not ") + "Showing Parse Trees");
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
				
				var syntaxTree = new Parser(line).parse();
				var comp = new Compilation(syntaxTree);
				var result = comp.evaluate();
				
				if (showTree) {
					prettyPrint(syntaxTree.getRoot());	
				}
	
				
				if (result.getDiagnostics().isEmpty()) {
					System.out.println(result.getValue());	
				} else {
					result.getDiagnostics().forEach(d -> {
						System.err.println();
						System.err.println(d.getMessage());
						System.err.println("    " + line);
						System.err.println("    " + " ".repeat(d.getSpan().getStart()) + "^".repeat(d.getSpan().getLength()));
					});
					
					// Hack to prevent the next iteration's prompt from printing concurrently
					// with the error messages
					try {
						Thread.sleep(25);
					} catch (InterruptedException uncaught) {
					}
				}
			}
		}
	}
}
