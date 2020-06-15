package minsk;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public abstract class Repl {

    private final PrintStream out;
    private final PrintStream err;
    private final InputStream in;
    private final Scanner sc;
    
    protected final boolean isInteractive;

    private final List<String> document = new ArrayList<>();
    private boolean done = false;

    public Repl(InputStream in, PrintStream out, PrintStream err, boolean isInteractive) {
    	this.in = in;
        this.out = out;
        this.err = err;
        this.isInteractive = isInteractive;
        
    	this.sc = new Scanner(in);
    }

    public void run() {
        if (isInteractive) {
            println("Staring REPL, use Ctrl+Z or #quit to stop.");
        }

        while (!done) {
            var isFirstLine = document.isEmpty();

            if (isInteractive) {
                print(isFirstLine ? "> " : "| ");
            }

            var line = sc != null ? sc.nextLine() : IOHelper.readLine();

            if (line == null) {
                done = true;
            }
            else if (isFirstLine && line.startsWith("#")) {
                parseMetaCommand(line);
            }
            else if (isFirstLine && line.startsWith(">")) {
            	parseInput(line);
            }
            else {
                document.add(line);

                var text = String.join(System.lineSeparator(), document);

                if (isCompleteSubmission(text)
                    || (document.size() > 1 && document.get(document.size() - 1).isEmpty() &&  document.get(document.size() - 2).isEmpty())) {
                    evaluateSubmission(text);
                    document.clear();
                }
            }
        }
    }
    
    protected abstract boolean isCompleteSubmission(String line);

    protected abstract void evaluateSubmission(String line);

    protected void parseInput(String line) {
    	IOHelper.bufferLine(line.replaceFirst("^>", "").strip());
    }
    
    protected void parseMetaCommand(String line) {
        if ("#quit".equals(line)) {
            done = true;
        }
    }

    protected void print(Object... args) {
        for (var a : args) {
            out.print(a);
        }
        out.flush();
    }

    protected void println(Object... args) {
        for (var a : args) {
            out.print(a);
        }
        out.println();
        out.flush();
    }

    protected void errorf(String fmt, Object ...args) {
        err.printf(fmt, args);
        err.flush();
    }
}