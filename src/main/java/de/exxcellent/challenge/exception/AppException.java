package de.exxcellent.challenge.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private final ErrorCode errorCode;

    private AppException(ErrorCode errorCode, String userMessage, Throwable cause) {
        super(userMessage, cause);
        this.errorCode = errorCode;
    }

    // ========== CLI Argument Errors ==========

    /**
     * Required CLI argument is missing.
     */
    public static AppException missingArgument(String argumentName) {
        return new AppException(
                ErrorCode.MISSING_ARGUMENT,
                String.format("Missing required argument: %s", argumentName),
                null
        );
    }

    /**
     * CLI argument value is invalid.
     */
    public static AppException invalidArgument(String detail) {
        return new AppException(
                ErrorCode.INVALID_ARGUMENT,
                String.format("Invalid argument: %s", detail),
                null
        );
    }

    // ========== File Access Errors ==========

    /**
     * File does not exist at the specified path.
     */
    public static AppException fileNotFound(String path, Throwable cause) {
        return new AppException(
                ErrorCode.FILE_NOT_FOUND,
                String.format("File '%s' does not exist. Please check the path.", path),
                cause
        );
    }

    /**
     * File exists but cannot be read (permission denied, is a directory, encoding issue, etc.).
     */
    public static AppException fileNotReadable(String path, Throwable cause) {
        return new AppException(
                ErrorCode.FILE_NOT_READABLE,
                String.format("Cannot read file '%s'. Please check permissions and file type.", path),
                cause
        );
    }

    /**
     * Failed to close a file after reading.
     */
    public static AppException fileCloseFailure(Throwable cause) {
        return new AppException(
                ErrorCode.FILE_CLOSE_FAILURE,
                "Failed to close file. The file may remain open or locked.",
                cause
        );
    }

    // ========== File Content Errors ==========

    /**
     * Required column missing from file header.
     */
    public static AppException missingColumn(String columnName) {
        return new AppException(
                ErrorCode.MISSING_COLUMN,
                String.format("Missing required column '%s'. Please check the file header.", columnName),
                null
        );
    }

    /**
     * Row data is malformed (parse error, type mismatch, empty required value, etc.).
     */
    public static AppException malformedRow(long lineNumber, Throwable cause) {
        return new AppException(
                ErrorCode.MALFORMED_ROW,
                String.format("Invalid data at line %d. Please check the file content.", lineNumber),
                cause
        );
    }

    // ========== Analysis Errors ==========

    /**
     * Analysis cannot be completed due to business logic failure.
     */
    public static AppException analysisFailed(String reason) {
        return new AppException(
                ErrorCode.ANALYSIS_FAILED,
                String.format("Analysis failed: %s", reason),
                null
        );
    }

    // ========== Unexpected Errors ==========

    /**
     * Unexpected error that doesn't fit into known categories.
     */
    public static AppException unexpected(String message, Throwable cause) {
        return new AppException(
                ErrorCode.UNEXPECTED_ERROR,
                String.format("Unexpected error: %s", message),
                cause
        );
    }
}
