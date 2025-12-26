package de.exxcellent.challenge.cli;

import de.exxcellent.challenge.exception.AppException;
import de.exxcellent.challenge.mapper.ExceptionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

/**
 * Picocli parameter exception handler for argument parsing/validation errors.
 */
@Component
@Slf4j
public class AppParameterExceptionHandler implements CommandLine.IParameterExceptionHandler {

    @Value("${logging.file.name}")
    private String logFileName;

    @Override
    public int handleParseException(CommandLine.ParameterException ex, String[] args) {
        CommandLine cmd = ex.getCommandLine();

        AppException appEx = ExceptionMapper.fromParameterExceptionToAppException(ex);

        cmd.getErr().println("Oops! Something went wrong.");
        cmd.getErr().println(appEx.getMessage());
        cmd.usage(cmd.getErr());

        log.error("CLI execution failed: {}", appEx.getMessage(), appEx);

        cmd.getErr().println(String.format("You can find more details in log: %s", logFileName));

        return appEx.getErrorCode().getExitCode();
    }

}
