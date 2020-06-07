package minsk;

import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;

public abstract class Repl {

    private final InputStream in;
    private final PrintStream out;
    private final PrintStream err;
    protected final boolean isInteractive;

    private final StringBuilder textBuilder = new StringBuilder();
    private boolean done = false;

    public Repl(InputStream in, PrintStream out, PrintStream err, boolean isInteractive) {
        this.in = in;
        this.out = out;
        this.err = err;
        this.isInteractive = isInteractive;
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

    public void run() {
        if (isInteractive) {
            println("Staring REPL, use Ctrl+Z or #quit to stop.");
        }

        try (Scanner sc = new Scanner(in, StandardCharsets.UTF_8)) {
            while (!done) {
                var isFirstLine = textBuilder.length() == 0;

                if (isInteractive) {
                    print(isFirstLine ? "> " : "| ");
                }

                String line = null;

                try {
                    line = sc.nextLine();
                } catch (NoSuchElementException e) {
                }

                if (line == null) {
                    done = true;
                }
                else if (isFirstLine && line.startsWith("#")) {
                    parseMetaCommand(line);
                }
                else {
                    textBuilder.append(line);
                    textBuilder.append(System.lineSeparator());
    
                    if (isCompleteSubmission(textBuilder.toString())) {
                        evaluateSubmission(textBuilder.toString());
                        textBuilder.setLength(0);
                    }
                }
            }
        }
    }

    protected abstract boolean isCompleteSubmission(String line);

    protected abstract void evaluateSubmission(String line);

    protected void parseMetaCommand(String line) {
        if ("#quit".equals(line)) {
            done = true;
        }
    }
}