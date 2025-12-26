package de.exxcellent.challenge.reader.serviceImpl;

import de.exxcellent.challenge.App;
import de.exxcellent.challenge.exception.AppException;
import de.exxcellent.challenge.exception.ErrorCode;
import de.exxcellent.challenge.reader.model.FileType;
import de.exxcellent.challenge.reader.model.WeatherCsv;
import de.exxcellent.challenge.reader.service.TableFileReader;
import de.exxcellent.challenge.reader.service.TableFileReaderFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-Test for the class {@link TableFileReaderCsvImpl}.
 */
@SpringBootTest(classes = App.class, properties = "cli.autorun=false")
class TableFileReaderCsvImplTest {

    @Autowired
    private TableFileReaderFactory tableFileReaderFactory;

    // ==================== Happy Path Tests ====================

    @Test
    @SneakyThrows
    @DisplayName("Should process records using stream()")
    void shouldProcessRecordsUsingStream() {

        List<WeatherCsv> expected = TestData.createWeatherList();
        String filePath = Objects.requireNonNull(getClass().getClassLoader()
                        .getResource("csv/weather-small.csv"))
                .getPath();

        try (TableFileReader<WeatherCsv> weatherReader = tableFileReaderFactory.create(
                filePath,
                FileType.CSV,
                WeatherCsv.class
        )) {
            List<WeatherCsv> actual = weatherReader.stream().toList();
            assertEquals(4, actual.size());
            assertEquals(expected, actual);
        }
    }

    @Test
    @SneakyThrows
    @DisplayName("Should read records one by one using read")
    void shouldReadRecordsOneByOne() {

        List<WeatherCsv> expected = TestData.createWeatherList();
        String filePath = Objects.requireNonNull(getClass().getClassLoader()
                        .getResource("csv/weather-small.csv"))
                .getPath();

        try (TableFileReader<WeatherCsv> weatherReader = tableFileReaderFactory.create(
                filePath,
                FileType.CSV,
                WeatherCsv.class
        )) {
            for (int i = 0; i < 4; i++) {
                Optional<WeatherCsv> weather = weatherReader.read();
                assertTrue(weather.isPresent());
                assertEquals(expected.get(i), weather.get());
            }
            Optional<WeatherCsv> tail = weatherReader.read();
            assertTrue(tail.isEmpty());
        }
    }

    @Test
    @SneakyThrows
    @DisplayName("Should iterate using for-each loop")
    void shouldIterateUsingForEach() {

        List<WeatherCsv> expected = TestData.createWeatherList();
        List<WeatherCsv> actual = new ArrayList<>();
        String filePath = Objects.requireNonNull(getClass().getClassLoader()
                        .getResource("csv/weather-small.csv"))
                .getPath();

        try (TableFileReader<WeatherCsv> weatherReader = tableFileReaderFactory.create(
                filePath,
                FileType.CSV,
                WeatherCsv.class
        )) {
            for (WeatherCsv weather : weatherReader) {
                actual.add(weather);
            }

            assertEquals(expected, actual);
        }
    }

    // ==================== Edge Case Tests ====================

    @Test
    @SneakyThrows
    @DisplayName("Should return empty list for CSV with only header")
    void shouldReturnEmptyListForHeaderOnly() {

        String filePath = Objects.requireNonNull(getClass().getClassLoader()
                        .getResource("csv/weather-empty.csv"))
                .getPath();

        try (TableFileReader<WeatherCsv> weatherReader = tableFileReaderFactory.create(
                filePath,
                FileType.CSV,
                WeatherCsv.class
        )) {
            List<WeatherCsv> result = weatherReader.stream().toList();

            assertTrue(result.isEmpty());
        }
    }

    @Test
    @SneakyThrows
    @DisplayName("Should handle CSV with extra whitespace")
    void shouldHandleCsvWithExtraWhitespace() {

        List<WeatherCsv> expected = TestData.createWeatherList();
        String filePath = Objects.requireNonNull(getClass().getClassLoader()
                        .getResource("csv/weather-space.csv"))
                .getPath();

        try (TableFileReader<WeatherCsv> weatherReader = tableFileReaderFactory.create(
                filePath,
                FileType.CSV,
                WeatherCsv.class
        )) {
            List<WeatherCsv> actual = weatherReader.stream().toList();

            assertEquals(expected, actual);
        }
    }

    @Test
    @SneakyThrows
    @DisplayName("Should handle CSV with changed columns")
    void shouldHandleCsvWithChangedColumns() {

        List<WeatherCsv> expected = TestData.createWeatherList();
        String filePath = Objects.requireNonNull(getClass().getClassLoader()
                        .getResource("csv/weather-changecols.csv"))
                .getPath();

        try (TableFileReader<WeatherCsv> weatherReader = tableFileReaderFactory.create(
                filePath,
                FileType.CSV,
                WeatherCsv.class
        )) {
            List<WeatherCsv> actual = weatherReader.stream().toList();

            assertEquals(expected, actual);
        }
    }

    // ==================== Error Cases ====================

    @Test
    @SneakyThrows
    @DisplayName("Should throw AppException when required column is missing")
    void shouldThrowWhenRequiredColumnMissing() {

        String filePath = Objects.requireNonNull(getClass().getClassLoader()
                        .getResource("csv/weather-misscols.csv"))
                .getPath();

        AppException exception = assertThrows(AppException.class, () -> {
            try (TableFileReader<WeatherCsv> reader = tableFileReaderFactory.create(
                    filePath,
                    FileType.CSV,
                    WeatherCsv.class
            )) {
                reader.stream().toList();
            }
        });

        assertEquals(ErrorCode.MISSING_COLUMN, exception.getErrorCode());
    }

    @Test
    @SneakyThrows
    @DisplayName("Should collect AppExceptions when row has type mismatch")
    void shouldCollectWhenTypeMismatch() {

        List<WeatherCsv> expected = TestData.createWeatherList();
        String filePath = Objects.requireNonNull(getClass().getClassLoader()
                        .getResource("csv/weather-mismatchrows.csv"))
                .getPath();

        try (TableFileReader<WeatherCsv> weatherReader = tableFileReaderFactory.create(
                filePath,
                FileType.CSV,
                WeatherCsv.class
        )) {
            List<WeatherCsv> actual = weatherReader.stream().toList();
            List<AppException> failedRowsExceptions = weatherReader.getFailedRowsExceptions();

            assertEquals(expected, actual);
            assertEquals(7, failedRowsExceptions.size());
            for (int i = 0; i < 7; i++) {
                assertEquals(failedRowsExceptions.get(i).getErrorCode(), ErrorCode.MALFORMED_ROW);
            }
        }
    }

    @Test
    @SneakyThrows
    @DisplayName("Should throw AppException when file does not exist")
    void shouldThrowWhenFileNotFound() {

        String filePath = "csv/this-file-does-not-exist.csv";

        AppException exception = assertThrows(AppException.class, () -> {
            try (TableFileReader<WeatherCsv> reader = tableFileReaderFactory.create(
                    filePath,
                    FileType.CSV,
                    WeatherCsv.class
            )) {
                reader.stream().toList();
            }
        });

        assertEquals(ErrorCode.FILE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @SneakyThrows
    @DisplayName("Should throw AppException when path is a directory")
    void shouldThrowWhenPathIsDirectory() {

        String filePath = Objects.requireNonNull(getClass().getClassLoader()
                        .getResource("csv"))
                .getPath();

        AppException exception = assertThrows(AppException.class, () -> {
            try (TableFileReader<WeatherCsv> reader = tableFileReaderFactory.create(
                    filePath,
                    FileType.CSV,
                    WeatherCsv.class
            )) {
                reader.stream().toList();
            }
        });

        assertEquals(ErrorCode.FILE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @SneakyThrows
    @DisplayName("Should throw AppException when csv file is illegal")
    void shouldThrowWhenCsvMalformed() {

        String filePath = Objects.requireNonNull(getClass().getClassLoader()
                        .getResource("csv/weather-bad-csv.csv"))
                .getPath();

        AppException exception = assertThrows(AppException.class, () -> {
            try (TableFileReader<WeatherCsv> reader = tableFileReaderFactory.create(
                    filePath,
                    FileType.CSV,
                    WeatherCsv.class
            )) {
                reader.stream().toList();
            }
        });

        assertEquals(ErrorCode.FILE_NOT_READABLE, exception.getErrorCode());
    }

    @Test
    @SneakyThrows
    @DisplayName("Should throw AppException when file is not a csv")
    void shouldThrowWhenFileIsPngButExpectCsv() {

        String filePath = Objects.requireNonNull(getClass().getClassLoader()
                        .getResource("csv/fruits.png"))
                .getPath();

        AppException exception = assertThrows(AppException.class, () -> {
            try (TableFileReader<WeatherCsv> reader = tableFileReaderFactory.create(
                    filePath,
                    FileType.CSV,
                    WeatherCsv.class
            )) {
                reader.stream().toList();
            }
        });

        assertEquals(ErrorCode.FILE_NOT_READABLE, exception.getErrorCode());
    }
}
