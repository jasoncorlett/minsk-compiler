package minsk;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import minsk.codeanalysis.*;
import minsk.codeanalysis.binding.VariableSymbol;
import minsk.codeanalysis.syntax.SyntaxTree;

// https://www.youtube.com/watch?v=wgHIkdUQbp0

public class Main {
	private static final String SHOW_VARS_CMD = "#showvars";
	private static final String SHOW_TREE_CMD = "#showtree";
	private static final String CLEAR_SCREEN_CMD = "#clear";
	private static final String QUIT_CMD = "#quit";
	
	public static void main(String[] args) {
		new Main().run();
	}
	
	public void run() {
		var showTree = false;
		var showVars = false;
		
		Map<VariableSymbol, Object> variables = new HashMap<>(); 
		
		try (Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8)) {
			while (true) {
				System.out.print("> ");
				
				String line;
				
				try {
					line = sc.nextLine();
				} catch (NoSuchElementException e) {
					break;
				}

				if (line == null || line.isEmpty())
					continue;

				if (QUIT_CMD.equalsIgnoreCase(line)) {
					break;
				}
				
				if (SHOW_TREE_CMD.equalsIgnoreCase(line)) {
					showTree = !showTree;
					System.out.println((showTree ? "" : "Not ") + "Showing Parse Trees");
					continue;
				}
				
				if (SHOW_VARS_CMD.equalsIgnoreCase(line)) {
					showVars = !showVars;
					System.out.println((showVars ? "" : "Not ") + "Showing Variables");
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
				
				var syntaxTree = SyntaxTree.parse(line);
				var comp = new Compilation(syntaxTree);
				var result = comp.evaluate(variables);
				
				if (showTree) {
					TreePrinter.prettyPrint(syntaxTree.getRoot());
				}
				
				if (showVars) {
					variables.entrySet().forEach(e -> System.out.println(e.getKey() + " = " + e.getValue()));
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
