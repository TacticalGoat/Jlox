package tacticalgoat.jlox.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Interpreter {
    public static void run(final String src) {
        System.out.println(src);
    }

    public static void prompt() throws IOException {
        final InputStreamReader input = new InputStreamReader(System.in);
        final BufferedReader reader = new BufferedReader(input);

        while (true) {
            System.out.print("> ");
            final String line = reader.readLine();
            if (line == null) {
                break;
            }
            run(line);
        }
    }
}
