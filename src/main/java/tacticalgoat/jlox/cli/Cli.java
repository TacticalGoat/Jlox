package tacticalgoat.jlox.cli;

import jdk.jshell.spi.ExecutionControl;
import picocli.CommandLine;
import tacticalgoat.jlox.core.Interpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "jlox", mixinStandardHelpOptions = true, description = "The Jlox interpreter")
public class Cli implements Callable<Integer> {
    @CommandLine.Option(names = {"-f", "--file"}, description = "The file to be executed")
    private String path;


    @Override
    public Integer call() throws Exception {
        if (path == null) {
            Interpreter.prompt();
        } else {
            throw new ExecutionControl.NotImplementedException("Running files not yet implemented");
        }
        return 0;
    }
}
