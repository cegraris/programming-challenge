package de.exxcellent.challenge.cli;

import de.exxcellent.challenge.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

/**
 * Picocli execution exception handler for runtime errors.
 */
@Component
@Slf4j
public class AppExecutionExceptionHandler implements CommandLine.IExecutionExceptionHandler {

    @Value("${logging.file.name}")
    private String logFileName;

    @Override
    public int handleExecutionException(Exception ex, CommandLine cmd, CommandLine.ParseResult parseResult) {
        AppException appEx;

        if (ex instanceof AppException ae) {
            appEx = ae;
        } else {
            appEx = AppException.unexpected(ex.getMessage(), ex);
        }

        cmd.getErr().println("Oops! Something went wrong.");
        cmd.getErr().println(appEx.getMessage());
        log.error("CLI execution failed: {}", appEx.getMessage(), appEx);

        cmd.getErr().println(String.format("You can find more details in log: %s", logFileName));

        return appEx.getErrorCode().getExitCode();
    }

}
