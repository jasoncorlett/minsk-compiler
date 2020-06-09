package minsk;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public abstract class Repl {

    private final InputStream in;
    private final PrintStream out;
    private final PrintStream err;

    protected final boolean isInteractive;

    private final List<String> document = new ArrayList<>();
    private Scanner scanner;
    private boolean done = false;

    public Repl(InputStream in, PrintStream out, PrintStream err, boolean isInteractive) {
        this.in = in;
        this.out = out;
        this.err = err;
        this.isInteractive = isInteractive;
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

            var line = getLine();

            if (line == null) {
                done = true;
            }
            else if (isFirstLine && line.startsWith("#")) {
                parseMetaCommand(line);
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

        if (scanner != null) {
            scanner.close();
        }
    }

    protected abstract boolean isCompleteSubmission(String line);

    protected abstract void evaluateSubmission(String line);

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

    private String getLine() {
        if (scanner == null) {
            scanner = new Scanner(in);
        }

        try {
            return scanner.nextLine();
        } catch (NoSuchElementException e) {
        }

        return null;
    }
}