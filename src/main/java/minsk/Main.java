package minsk;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

// https://www.youtube.com/watch?v=wgHIkdUQbp0

public class Main {
	public static void main(String[] args) throws IOException {
		var inputStream = System.in;
		var isInteractive = true;

		if (args.length > 0) {
			inputStream = Files.newInputStream(Paths.get(args[0]));
			isInteractive = false;
		}

		new MinskRepl(inputStream, System.out, System.err, isInteractive).run();
	}
	

}
