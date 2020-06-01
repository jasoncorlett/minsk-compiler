package minsk;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import minsk.codeanalysis.Compilation;
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
	private static final String RESET_CMD = "#reset";
	private static final String COMMENT = "#";
	
	public static void main(String[] args) throws IOException {
		var inputStream = System.in;
		var isRepl = true;
		
		if (args.length > 0) {
			inputStream = Files.newInputStream(Paths.get(args[0]));
			isRepl = false;
		}
		
		run(inputStream, isRepl);
	}
	
	public static void run(InputStream inputStream, boolean isRepl) {
		var showTree = false;
		var showVars = false;
		
		Map<VariableSymbol, Object> variables = new HashMap<>(); 
		var stringBuilder = new StringBuilder();
		Compilation previous = null;
		
		try (Scanner sc = new Scanner(inputStream, StandardCharsets.UTF_8)) {
			while (true) {
				var isFirstLine = stringBuilder.length() == 0;
				
				if (isRepl)
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
					
					if (RESET_CMD.equalsIgnoreCase(line)) {
						previous = null;
						variables.clear();
						continue;
					}
					
					if (line.startsWith(COMMENT)) {
						continue;
					}
				}
				
				stringBuilder.append(line);
				stringBuilder.append("\n");
				
				var syntaxTree = SyntaxTree.parse(stringBuilder.toString());
				
				if (syntaxTree.getDiagnostics().isEmpty() || (isBlank && isRepl)) {
					var compilation = previous == null
							? new Compilation(syntaxTree)
							: previous.continueWith(syntaxTree);
							
					var result = compilation.evaluate(variables);
					
					if (showTree) {
						TreePrinter.prettyPrint(syntaxTree.getRoot().getStatement());
					}
					
					if (showVars) {
						variables.entrySet().forEach(e -> System.out.println(e.getKey() + " = " + e.getValue()));
					}
					
					if (result.getDiagnostics().isEmpty()) {
						System.out.println(result.getValue());
						previous = compilation;
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
