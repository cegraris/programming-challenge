package de.exxcellent.challenge.cli;

import de.exxcellent.challenge.App;
import de.exxcellent.challenge.analyser.service.WeatherAnalyser;
import de.exxcellent.challenge.exception.AppException;
import de.exxcellent.challenge.reader.model.FileType;
import de.exxcellent.challenge.reader.model.WeatherCsv;
import de.exxcellent.challenge.reader.service.TableFileReader;
import de.exxcellent.challenge.reader.service.TableFileReaderFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit-Test for the package {@link de.exxcellent.challenge.cli}.
 */
@SpringBootTest(classes = App.class, properties = "cli.autorun=false")
class CliTest {

    @Autowired
    CommandLine commandLine;

    ByteArrayOutputStream baos;

    @MockitoBean
    TableFileReaderFactory tableFileReaderFactory;

    @MockitoBean
    WeatherAnalyser weatherAnalyser;

    @BeforeEach
    void beforeEach() {
        baos = new ByteArrayOutputStream();
        commandLine.setOut(new PrintWriter(baos, true));
        commandLine.setErr(new PrintWriter(baos, true));
    }

    // ==================== Happy Path Tests ====================

    @Test
    @SneakyThrows
    @DisplayName("Weather mode: prints smallest temperature spread day(s) and exits with 0")
    void houldRunWeatherModeAndPrintResult() {

        @SuppressWarnings("unchecked")
        TableFileReader<WeatherCsv> reader = mock(TableFileReader.class);
        when(tableFileReaderFactory.create(
                any(),
                eq(FileType.CSV),
                eq(WeatherCsv.class)
        )).thenReturn(reader);
        when(reader.stream()).thenReturn(TestData.createWeatherList().stream());
        when(reader.getFailedRowsExceptions()).thenReturn(List.of());
        when(weatherAnalyser.findDayOfSmallestTemperatureSpread(any())).thenReturn(Set.of(2));

        CliResult result = executeAndCapture("--weather", "test.csv");

        assertThat(result.exitCode).isEqualTo(0);
        assertThat(result.output).contains("Smallest temperature spread day(s): [2]");
    }

    @Test
    @SneakyThrows
    @DisplayName("Default mode (Weather mode): prints smallest temperature spread day(s) and exits with 0")
    void shouldRunDefaultModeAndPrintResult() {

        @SuppressWarnings("unchecked")
        TableFileReader<WeatherCsv> reader = mock(TableFileReader.class);
        when(tableFileReaderFactory.create(
                any(),
                eq(FileType.CSV),
                eq(WeatherCsv.class)
        )).thenReturn(reader);
        when(reader.stream()).thenReturn(TestData.createWeatherList().stream());
        when(reader.getFailedRowsExceptions()).thenReturn(List.of());
        when(weatherAnalyser.findDayOfSmallestTemperatureSpread(any())).thenReturn(Set.of(2));

        CliResult result = executeAndCapture("test.csv");

        assertThat(result.exitCode).isEqualTo(0);
        assertThat(result.output).contains("Smallest temperature spread day(s): [2]");
    }

    // ==================== Edge Case Tests ====================

    @Test
    @SneakyThrows
    @DisplayName("Should print errors in report when rows have type mismatch")
    void shouldHandleCsvWithTypeMismatch() {

        @SuppressWarnings("unchecked")
        TableFileReader<WeatherCsv> reader = mock(TableFileReader.class);
        when(tableFileReaderFactory.create(
                any(),
                eq(FileType.CSV),
                eq(WeatherCsv.class)
        )).thenReturn(reader);
        when(reader.stream()).thenReturn(TestData.createWeatherList().stream());
        when(reader.getFailedRowsExceptions()).thenReturn(TestData.createFailedRowsExceptions());
        when(weatherAnalyser.findDayOfSmallestTemperatureSpread(any())).thenReturn(Set.of(2));

        CliResult result = executeAndCapture("test.csv");

        assertThat(result.exitCode).isEqualTo(0);
        assertThat(result.output).contains("Smallest temperature spread day(s): [2]");
        assertThat(result.output).contains("Invalid data at line 3. Please check the file content.");
        assertThat(result.output).contains("Invalid data at line 5. Please check the file content.");
        assertThat(result.output).contains("Invalid data at line 7. Please check the file content.");
        assertThat(result.output).contains("Invalid data at line 9. Please check the file content.");
        assertThat(result.output).contains("Invalid data at line 10. Please check the file content.");
    }

    // ==================== Error Case Tests ====================

    @Test
    @SneakyThrows
    @DisplayName("Should show missing argument(s) error message")
    void shouldShowMissingArgumentException() {

        CliResult result = executeAndCapture();

        assertThat(result.exitCode).isEqualTo(1);
        assertThat(result.output).contains("Missing required argument");
    }

    @Test
    @SneakyThrows
    @DisplayName("Should show invalid argument(s) error message")
    void shouldShowInvalidArgumentException() {

        CliResult result = executeAndCapture("--dummy", "csv/test.csv");

        assertThat(result.exitCode).isEqualTo(1);
        assertThat(result.output).contains("Invalid argument");
    }

    @Test
    @SneakyThrows
    @DisplayName("Should show reader reported error message")
    void shouldShowReaderThrownException() {

        @SuppressWarnings("unchecked")
        TableFileReader<WeatherCsv> reader = mock(TableFileReader.class);
        when(tableFileReaderFactory.create(
                any(),
                eq(FileType.CSV),
                eq(WeatherCsv.class)
        )).thenReturn(reader);
        when(reader.stream()).thenThrow(AppException.fileNotFound("path_to_test.csv", null));
        when(reader.getFailedRowsExceptions()).thenReturn(List.of());
        when(weatherAnalyser.findDayOfSmallestTemperatureSpread(any())).thenReturn(Set.of(2));

        CliResult result = executeAndCapture("csv/test.csv");

        assertThat(result.exitCode).isEqualTo(2);
        assertThat(result.output).contains("File 'path_to_test.csv' does not exist. Please check the path.");
    }

    @Test
    @SneakyThrows
    @DisplayName("Should show analyser reported error message")
    void shouldShowAnalyserThrownException() {

        @SuppressWarnings("unchecked")
        TableFileReader<WeatherCsv> reader = mock(TableFileReader.class);
        when(tableFileReaderFactory.create(
                any(),
                eq(FileType.CSV),
                eq(WeatherCsv.class)
        )).thenReturn(reader);
        when(reader.stream()).thenReturn(TestData.createWeatherList().stream());
        when(reader.getFailedRowsExceptions()).thenReturn(List.of());
        when(weatherAnalyser.findDayOfSmallestTemperatureSpread(any()))
                .thenThrow(AppException.analysisFailed("No valid data after filtering"));

        CliResult result = executeAndCapture("csv/test.csv");

        assertThat(result.exitCode).isEqualTo(4);
        assertThat(result.output).contains("Analysis failed: No valid data after filtering");
    }

    // ==================== Helpers ====================

    private CliResult executeAndCapture(String... args) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        commandLine.setOut(new PrintWriter(baos, true));
        commandLine.setErr(new PrintWriter(baos, true));

        int exitCode = commandLine.execute(args);
        String output = baos.toString(java.nio.charset.StandardCharsets.UTF_8);

        return new CliResult(exitCode, output);
    }

    private record CliResult(int exitCode, String output) {
    }

}
