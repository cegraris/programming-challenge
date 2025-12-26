package de.exxcellent.challenge.cli;

import picocli.CommandLine;

/**
 * CLI option group that controls which analysis mode should be executed.
 */
class ModeOption {
    @CommandLine.Option(names = "--weather", description = "Weather Data Analysis Mode")
    boolean weather;

    @CommandLine.Option(names = "--football", description = "Football Data Analysis Mode")
    boolean football;
}
