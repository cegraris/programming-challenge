package de.exxcellent.challenge.configuration;

import de.exxcellent.challenge.cli.AppExecutionExceptionHandler;
import de.exxcellent.challenge.cli.AppParameterExceptionHandler;
import de.exxcellent.challenge.cli.MainCommand;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import picocli.CommandLine;

/**
 * Spring configuration for picocli integration.
 */
@Configuration
public class PicocliConfig {
    /**
     * Creates and configures the picocli {@link CommandLine} instance.
     *
     * @param mainCommand  the root command
     * @param execHandler  handler for execution-time exceptions
     * @param paramHandler handler for parameter/usage exceptions
     * @return a configured {@link CommandLine} instance
     */
    @Bean
    public CommandLine commandLine(MainCommand mainCommand,
                                   AppExecutionExceptionHandler execHandler,
                                   AppParameterExceptionHandler paramHandler) {
        CommandLine cmd = new CommandLine(mainCommand);
        cmd.setExecutionExceptionHandler(execHandler);
        cmd.setParameterExceptionHandler(paramHandler);
        return cmd;
    }

}
