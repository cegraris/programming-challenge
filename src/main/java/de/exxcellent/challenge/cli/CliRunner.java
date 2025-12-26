package de.exxcellent.challenge.cli;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

/**
 * Spring Boot runner that bridges application startup arguments to picocli.
 */
@Component
@ConditionalOnProperty(name = "cli.autorun", havingValue = "true", matchIfMissing = true)
class CliRunner implements CommandLineRunner, ExitCodeGenerator {

    private final CommandLine commandLine;
    private int exitCode = 0;

    CliRunner(CommandLine commandLine) {
        this.commandLine = commandLine;
    }

    @Override
    public void run(String... args) {
        this.exitCode = commandLine.execute(args);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }

}
