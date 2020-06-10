package minsk;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import minsk.codeanalysis.Compilation;
import minsk.codeanalysis.symbols.VariableSymbol;
import minsk.codeanalysis.syntax.SyntaxTree;
import minsk.codeanalysis.text.SourceText;
import minsk.diagnostics.Diagnosable;

public class MinskRepl extends Repl {

    public MinskRepl(InputStream in, PrintStream out, PrintStream err, boolean isInteractive) {
        super(out, err, isInteractive);
    }

    private boolean showTree = false;
    private boolean showProgram = false;
    private boolean showVars = false;

    private Compilation previous = null;
    private Map<VariableSymbol, Object> variables = new HashMap<>();

    @Override
    protected boolean isCompleteSubmission(String line) {
        return SyntaxTree.parse(line).getDiagnostics().isEmpty();
    }

    @Override
    protected void evaluateSubmission(String line) {
        var syntaxTree = SyntaxTree.parse(line);

        var compilation = previous == null
                ? new Compilation(syntaxTree)
                : previous.continueWith(syntaxTree);

        if (showProgram) {
            TreePrinter.prettyPrint(compilation.getBoundNode());
        }

        if (showTree) {
            TreePrinter.prettyPrint(syntaxTree.getRoot().getStatement());
        }

        var result = compilation.evaluate(variables);

        if (showVars) {
            variables.entrySet().forEach(e -> println(e.getKey() + " = " + e.getValue()));
        }

        if (result.getDiagnostics().isEmpty()) {
            println(result.getValue());
            previous = compilation;
        }
        else {
            printDiagnostics(result, syntaxTree.getSource());

            // Hack to prevent the next iteration's prompt from printing concurrently
            // with the error messages
            try {
                Thread.sleep(25);
            } catch (InterruptedException uncaught) {
            }
        }
    }

    @Override
    protected void parseMetaCommand(String line) {
        String msg = null;

        switch (line) {
            case "#showtree":
                showTree = !showTree;
                msg = (showTree ? "" : "Not ") + "Showing Parse Tree";
                break;
            case "#showvars":
                showVars = !showVars;
                msg = (showTree ? "" : "Not ") + "Showing Variables";
                break;
            case "#showprogram":
                showProgram = !showProgram;
                msg = (showProgram ? "" : "Not ") + "Showing Program";
                break;
            case "#reset":
                previous = null;
                variables.clear();
                break;
            default:
                super.parseMetaCommand(line);
        }

        if (isInteractive && msg != null)
            println(msg);
    }

    private void printDiagnostics(Diagnosable diagnosable, SourceText source) {
        for (var diagnostic : diagnosable.getDiagnostics()) {
            var lineNumber = source.getLineIndex(diagnostic.getSpan().getStart());
            var line = source.getLine(lineNumber);
            var position = diagnostic.getSpan().getStart() - line.getStart();

            errorf("%n");
            errorf("[%d:%d] %s%n", lineNumber, position, diagnostic.getMessage());
            errorf("    %s%n", line.getText());
            errorf("    %s%s%n", " ".repeat(position), "^".repeat(diagnostic.getSpan().getLength()));
        }
    }
}