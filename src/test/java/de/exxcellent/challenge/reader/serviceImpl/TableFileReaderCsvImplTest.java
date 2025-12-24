package de.exxcellent.challenge.reader.serviceImpl;

import de.exxcellent.challenge.App;
import de.exxcellent.challenge.exception.AppException;
import de.exxcellent.challenge.exception.ErrorCode;
import de.exxcellent.challenge.model.Weather;
import de.exxcellent.challenge.reader.model.FileType;
import de.exxcellent.challenge.reader.service.TableFileReader;
import de.exxcellent.challenge.reader.service.TableFileReaderFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-Test für die Klasse {@link TableFileReaderCsvImpl}.
 */
@SpringBootTest(classes = App.class)
class TableFileReaderCsvImplTest {

    @Autowired
    private TableFileReaderFactory tableFileReaderFactory;

    @BeforeAll
    static void setup() {
        Locale.setDefault(Locale.ENGLISH);
    }

    // ==================== Happy Path Tests ====================

    @Test
    @SneakyThrows
    @DisplayName("Should process records using stream()")
    void shouldProcessRecordsUsingStream() {

        List<Weather> expected = TestDaten.createWeatherList();
        String filePath = Objects.requireNonNull(getClass().getClassLoader()
                        .getResource("csv/weather-small.csv"))
                .getPath();

        try (TableFileReader<Weather> weatherReader = tableFileReaderFactory.create(
                filePath,
                FileType.CSV,
                Weather.class
        )) {
            List<Weather> actual = weatherReader.stream().toList();
            assertEquals(4, actual.size());
            assertEquals(expected, actual);
        }
    }

    @Test
    @SneakyThrows
    @DisplayName("Should read records one by one using read")
    void shouldReadRecordsOneByOne() {

        List<Weather> expected = TestDaten.createWeatherList();
        String filePath = Objects.requireNonNull(getClass().getClassLoader()
                        .getResource("csv/weather-small.csv"))
                .getPath();

        try (TableFileReader<Weather> weatherReader = tableFileReaderFactory.create(
                filePath,
                FileType.CSV,
                Weather.class
        )) {
            for (int i = 0; i < 4; i++) {
                Optional<Weather> weather = weatherReader.read();
                assertTrue(weather.isPresent());
                assertEquals(expected.get(i), weather.get());
            }
            Optional<Weather> tail = weatherReader.read();
            assertTrue(tail.isEmpty());
        }
    }

    @Test
    @SneakyThrows
    @DisplayName("Should iterate using for-each loop")
    void shouldIterateUsingForEach() {

        List<Weather> expected = TestDaten.createWeatherList();
        List<Weather> actual = new ArrayList<>();
        String filePath = Objects.requireNonNull(getClass().getClassLoader()
                        .getResource("csv/weather-small.csv"))
                .getPath();

        try (TableFileReader<Weather> weatherReader = tableFileReaderFactory.create(
                filePath,
                FileType.CSV,
                Weather.class
        )) {
            for (Weather weather : weatherReader) {
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

        try (TableFileReader<Weather> weatherReader = tableFileReaderFactory.create(
                filePath,
                FileType.CSV,
                Weather.class
        )) {
            List<Weather> result = weatherReader.stream().toList();

            assertTrue(result.isEmpty());
        }
    }

    @Test
    @SneakyThrows
    @DisplayName("Should handle CSV with extra whitespace")
    void shouldHandleCsvWithExtraWhitespace() {

        List<Weather> expected = TestDaten.createWeatherList();
        String filePath = Objects.requireNonNull(getClass().getClassLoader()
                        .getResource("csv/weather-space.csv"))
                .getPath();

        try (TableFileReader<Weather> weatherReader = tableFileReaderFactory.create(
                filePath,
                FileType.CSV,
                Weather.class
        )) {
            List<Weather> actual = weatherReader.stream().toList();

            assertEquals(expected, actual);
        }
    }

    @Test
    @SneakyThrows
    @DisplayName("Should handle CSV with changed columns")
    void shouldHandleCsvWithChangedColumns() {

        List<Weather> expected = TestDaten.createWeatherList();
        String filePath = Objects.requireNonNull(getClass().getClassLoader()
                        .getResource("csv/weather-changecols.csv"))
                .getPath();

        try (TableFileReader<Weather> weatherReader = tableFileReaderFactory.create(
                filePath,
                FileType.CSV,
                Weather.class
        )) {
            List<Weather> actual = weatherReader.stream().toList();

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
            try (TableFileReader<Weather> reader = tableFileReaderFactory.create(
                    filePath,
                    FileType.CSV,
                    Weather.class
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

        List<Weather> expected = TestDaten.createWeatherList();
        String filePath = Objects.requireNonNull(getClass().getClassLoader()
                        .getResource("csv/weather-mismatchrows.csv"))
                .getPath();

        try (TableFileReader<Weather> weatherReader = tableFileReaderFactory.create(
                filePath,
                FileType.CSV,
                Weather.class
        )) {
            List<Weather> actual = weatherReader.stream().toList();
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
            try (TableFileReader<Weather> reader = tableFileReaderFactory.create(
                    filePath,
                    FileType.CSV,
                    Weather.class
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
            try (TableFileReader<Weather> reader = tableFileReaderFactory.create(
                    filePath,
                    FileType.CSV,
                    Weather.class
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
            try (TableFileReader<Weather> reader = tableFileReaderFactory.create(
                    filePath,
                    FileType.CSV,
                    Weather.class
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
            try (TableFileReader<Weather> reader = tableFileReaderFactory.create(
                    filePath,
                    FileType.CSV,
                    Weather.class
            )) {
                reader.stream().toList();
            }
        });

        assertEquals(ErrorCode.FILE_NOT_READABLE, exception.getErrorCode());
    }
}
