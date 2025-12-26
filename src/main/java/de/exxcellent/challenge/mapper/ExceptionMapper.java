package de.exxcellent.challenge.mapper;

import de.exxcellent.challenge.exception.AppException;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import picocli.CommandLine;

/**
 * Maps exceptions to application-specific {@link AppException}s.
 */
@UtilityClass
public class ExceptionMapper {
    /**
     * Converts a picocli {@link CommandLine.ParameterException} into an {@link AppException}.
     *
     * @param ex the picocli parameter exception to map
     * @return an application-specific exception representing the same error condition
     */
    public AppException fromParameterExceptionToAppException(
            @NonNull
            CommandLine.ParameterException ex) {
        if (ex instanceof CommandLine.MissingParameterException mpe) {
            String missing = mpe.getMissing().isEmpty()
                    ? "unknown"
                    : mpe.getMissing().get(0).toString();
            return AppException.missingArgument(missing);
        }
        return AppException.invalidArgument(ex.getMessage());
    }

}
