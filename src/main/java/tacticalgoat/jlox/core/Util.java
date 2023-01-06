package tacticalgoat.jlox.core;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Util {
    public static String loadFile(final String path) throws IOException {
        return Files.readString(Paths.get(path), Charset.defaultCharset());
    }

    public static String error(final int line, final String message) {
        return String.format("At %d, %s.", line, message);
    }
}
