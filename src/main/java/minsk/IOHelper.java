package minsk;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class IOHelper {
    private static Scanner scanner;

    public static String readLine() {
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

}