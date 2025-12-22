package de.exxcellent.challenge.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Error codes for CLI exit status and log categorization.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // ===== CLI Argument Errors (exit code 1) =====
    /**
     * Required argument is missing
     */
    MISSING_ARGUMENT(1),
    /**
     * Argument value is invalid
     */
    INVALID_ARGUMENT(1),

    // ===== File Access Errors (exit code 2) =====
    /**
     * File does not exist at the specified path
     */
    FILE_NOT_FOUND(2),
    /**
     * File exists but cannot be read (permission, encoding, is directory, etc.)
     */
    FILE_NOT_READABLE(2),

    // ===== File Content Errors (exit code 3) =====
    /**
     * Column(s) missing from file header
     */
    MISSING_COLUMN(3),
    /**
     * Row data is malformed (parse error, type mismatch, required value empty, etc.)
     */
    MALFORMED_ROW(3),

    // ===== Analysis Errors (exit code 4) =====
    /**
     * Analysis cannot be completed (invalid range, no valid data after filtering, duplicate key values etc.)
     */
    ANALYSIS_FAILED(4);

    private final int exitCode;
}
