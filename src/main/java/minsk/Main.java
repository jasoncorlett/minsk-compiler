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
import minsk.codeanalysis.text.SourceText;
import minsk.diagnostics.Diagnosable;

// https://www.youtube.com/watch?v=wgHIkdUQbp0

public class Main {
	private static final String SHOW_VARS_CMD = "#showvars";
	private static final String SHOW_TREE_CMD = "#showtree";
	private static final String CLEAR_SCREEN_CMD = "#clear";
	private static final String QUIT_CMD = "#quit";
	
	public static void main(String[] args) {
		var showTree = false;
		var showVars = false;
		
		Map<VariableSymbol, Object> variables = new HashMap<>(); 
		var stringBuilder = new StringBuilder();
		
		try (Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8)) {
			while (true) {
				var isFirstLine = stringBuilder.length() == 0;
				
				System.out.print(isFirstLine ? "> " : "| ");
				
				String line;
				
				try {
					line = sc.nextLine();
				} catch (NoSuchElementException e) {
					break;
				}
				
				var isBlank = line == null || line.isEmpty();

				if (isFirstLine) {
					if (isBlank) {
						continue;
					}
					
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
				}
				
				stringBuilder.append(line);
				stringBuilder.append("\n");
				
				var syntaxTree = SyntaxTree.parse(stringBuilder.toString());
				
				if (syntaxTree.getDiagnostics().isEmpty() || isBlank) {
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
						printDiagnostics(result, syntaxTree.getSource());
						
						// Hack to prevent the next iteration's prompt from printing concurrently
						// with the error messages
						try {
							Thread.sleep(25);
						} catch (InterruptedException uncaught) {
						}
					}
					
					stringBuilder = new StringBuilder();
				}
			}
		}
	}
	
	private static void printDiagnostics(Diagnosable diagnosable, SourceText source) {
		for (var diagnostic : diagnosable.getDiagnostics()) {
			var lineNumber = source.getLineIndex(diagnostic.getSpan().getStart());
			var line = source.getLine(lineNumber);
			var position = diagnostic.getSpan().getStart() - line.getStart();
			
			System.err.printf("%n");
			System.err.printf("[%d:%d] %s%n", lineNumber, position, diagnostic.getMessage());
			System.err.printf("    %s%n", line.getText());
			System.err.printf("    %s%s%n", " ".repeat(position), "^".repeat(diagnostic.getSpan().getLength()));
		}
	}

	
}
