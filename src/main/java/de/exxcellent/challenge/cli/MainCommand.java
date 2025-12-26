package de.exxcellent.challenge.cli;


import de.exxcellent.challenge.analyser.model.AnalyserType;
import de.exxcellent.challenge.analyser.service.AnalyserFactory;
import de.exxcellent.challenge.analyser.service.WeatherAnalyser;
import de.exxcellent.challenge.exception.AppException;
import de.exxcellent.challenge.mapper.WeatherMapper;
import de.exxcellent.challenge.model.Weather;
import de.exxcellent.challenge.reader.model.FileType;
import de.exxcellent.challenge.reader.model.WeatherCsv;
import de.exxcellent.challenge.reader.service.TableFileReader;
import de.exxcellent.challenge.reader.service.TableFileReaderFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

/**
 * Root picocli command of the application.
 */
@Component
@Slf4j
@CommandLine.Command(name = "exxcellent-programming-challenge",
        mixinStandardHelpOptions = true,
        versionProvider = MainCommand.Version.class)
public class MainCommand implements Callable<Integer> {

    private final TableFileReaderFactory tableFileReaderFactory;
    private final AnalyserFactory analyserFactory;
    private final WeatherMapper weatherMapper;
    @Spec
    private CommandSpec spec;
    @Value("${logging.file.name}")
    private String logFileName;
    @CommandLine.ArgGroup()
    private ModeOption modeOption;
    @CommandLine.Option(
            names = {"-f", "--format"},
            defaultValue = "CSV",
            description = "Input File Formant. (Default: CSV)"
    )
    private FileType format;
    @CommandLine.Parameters(index = "0", description = "Input File Path")
    private Path inputFile;

    public MainCommand(TableFileReaderFactory tableFileReaderFactory, AnalyserFactory analyserFactory, WeatherMapper weatherMapper) {
        this.tableFileReaderFactory = tableFileReaderFactory;
        this.analyserFactory = analyserFactory;
        this.weatherMapper = weatherMapper;
    }

    @Override
    public Integer call() {
        printBanner();

        AnalyserType mode = AnalyserType.WEATHER;

        log.info("Mode: {}", mode);
        log.info("File: {}", inputFile.toAbsolutePath());

        spec.commandLine().getOut().println("Starting...");

        switch (mode) {
            case WEATHER -> handleWeather(analyserFactory.getWeatherAnalyser(), inputFile);
        }

        spec.commandLine().getOut().println(String.format("Log file: %s", logFileName));

        return 0;
    }

    /**
     * Executes the weather analysis workflow
     *
     * @param weatherAnalyser analyser implementation for weather data
     * @param filePath        input file path
     */
    private void handleWeather(WeatherAnalyser weatherAnalyser, Path filePath) {
        String path = filePath.toAbsolutePath().normalize().toString();
        switch (format) {
            case CSV -> {
                try (TableFileReader<WeatherCsv> reader = tableFileReaderFactory.create(
                        path,
                        FileType.CSV,
                        WeatherCsv.class
                )) {
                    Set<Integer> days;
                    try (Stream<Weather> weathers = reader.stream().map(weatherMapper::toDomain)) {
                        days = weatherAnalyser.findDayOfSmallestTemperatureSpread(weathers);
                    }
                    List<AppException> failedRowsExceptions = reader.getFailedRowsExceptions();
                    spec.commandLine().getOut().println("Weather Data Analysis Ended Successfully");
                    log.info("Weather Data Analysis Ended Successfully");
                    spec.commandLine().getOut().println();
                    printReport(String.format("Smallest temperature spread day(s): %s", days), failedRowsExceptions);
                } catch (AppException e) {
                    throw e;
                } catch (Exception e) {
                    throw AppException.unexpected(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Prints the ASCII banner to standard output.
     */
    private void printBanner() {
        spec.commandLine().getOut().println("""
                                                                                                          \s
                        ███████╗██╗  ██╗██╗  ██╗ ██████╗███████╗██╗     ██╗     ███████╗███╗   ██╗████████╗
                        ██╔════╝╚██╗██╔╝╚██╗██╔╝██╔════╝██╔════╝██║     ██║     ██╔════╝████╗  ██║╚══██╔══╝
                        █████╗   ╚███╔╝  ╚███╔╝ ██║     █████╗  ██║     ██║     █████╗  ██╔██╗ ██║   ██║  \s
                        ██╔══╝   ██╔██╗  ██╔██╗ ██║     ██╔══╝  ██║     ██║     ██╔══╝  ██║╚██╗██║   ██║  \s
                        ███████╗██╔╝ ██╗██╔╝ ██╗╚██████╗███████╗███████╗███████╗███████╗██║ ╚████║   ██║  \s
                        ╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚══════╝╚══════╝╚══════╝╚══════╝╚═╝  ╚═══╝   ╚═╝  \s
                                                                                                          \s
                         ██████╗██╗  ██╗ █████╗ ██╗     ██╗     ███████╗███╗   ██╗ ██████╗ ███████╗       \s
                        ██╔════╝██║  ██║██╔══██╗██║     ██║     ██╔════╝████╗  ██║██╔════╝ ██╔════╝       \s
                        ██║     ███████║███████║██║     ██║     █████╗  ██╔██╗ ██║██║  ███╗█████╗         \s
                        ██║     ██╔══██║██╔══██║██║     ██║     ██╔══╝  ██║╚██╗██║██║   ██║██╔══╝         \s
                        ╚██████╗██║  ██║██║  ██║███████╗███████╗███████╗██║ ╚████║╚██████╔╝███████╗       \s
                         ╚═════╝╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝╚══════╝╚═╝  ╚═══╝ ╚═════╝ ╚══════╝       \s
                                                                                                          \s
                """);
    }

    /**
     * Prints the final analysis report and any row-level errors that were collected.
     *
     * @param result               analysis result summary line
     * @param failedRowsExceptions exceptions collected while parsing rows
     */
    private void printReport(String result, List<AppException> failedRowsExceptions) {
        spec.commandLine().getOut().println("====================== ANALYSIS REPORT ====================== ");
        spec.commandLine().getOut().println(result);
        log.info(result);
        if (!failedRowsExceptions.isEmpty()) {
            spec.commandLine().getOut().println("The following rows were skipped: ");
            for (AppException e : failedRowsExceptions) {
                log.error(e.getMessage());
                if (e.getCause() != null) {
                    log.error(e.getCause().getMessage());
                }
            }
            failedRowsExceptions.stream()
                    .map(AppException::getMessage)
                    .filter(Objects::nonNull)
                    .distinct()
                    .forEach(msg -> spec.commandLine().getOut().println(msg));
            spec.commandLine().getOut().println("You can find more information from log.");
        }
        spec.commandLine().getOut().println("============================================================= ");
    }

    /**
     * Use the version defined by pom.xml.
     */
    static class Version implements CommandLine.IVersionProvider {
        @Override
        public String[] getVersion() {
            String v = MainCommand.class.getPackage().getImplementationVersion();
            return new String[]{v != null ? v : "unknown"};
        }
    }
}
