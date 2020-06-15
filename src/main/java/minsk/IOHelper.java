package minsk;

import java.util.Deque;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class IOHelper {
    private static Scanner scanner;
    private static Deque<String> buffer;

    public static String readLine() {
    	if (buffer != null && !buffer.isEmpty()) {
    		return buffer.pop();
    	}
    	
        if (scanner == null) {
            scanner = new Scanner(System.in);
        }

        try {
            return scanner.nextLine();
        }
        catch (NoSuchElementException e) {
            return null;
        }
    }
    
    public static void bufferLine(String line) {
    	if (buffer == null) {
    		buffer = new LinkedList<>();
    	}

    	buffer.add(line);
    }

}