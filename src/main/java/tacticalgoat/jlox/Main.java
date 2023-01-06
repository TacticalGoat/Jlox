package tacticalgoat.jlox;

import lombok.extern.log4j.Log4j2;
import picocli.CommandLine;
import tacticalgoat.jlox.cli.Cli;

@Log4j2
public class Main {
    public static void main(final String... args) {
        log.debug("Log4j is working!");
        final int exitCode = new CommandLine(new Cli()).execute(args);
        System.exit(exitCode);
    }
}