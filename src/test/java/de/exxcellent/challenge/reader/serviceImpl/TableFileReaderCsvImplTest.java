package de.exxcellent.challenge.reader.serviceImpl;

import de.exxcellent.challenge.App;
import de.exxcellent.challenge.model.Weather;
import de.exxcellent.challenge.reader.model.FileType;
import de.exxcellent.challenge.reader.service.TableFileReader;
import de.exxcellent.challenge.reader.service.TableFileReaderFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit-Test für die Klasse {@link TableFileReaderCsvImpl}.
 */
@SpringBootTest(classes = App.class)
public class TableFileReaderCsvImplTest {

    @Autowired
    private TableFileReaderFactory tableFileReaderFactory;

    // ==================== Happy Path Tests ====================

    @Test
    @SneakyThrows
    @DisplayName("Should process records using stream()")
    void shouldProcessRecordsUsingStream() {

        List<Weather> expected = TestDaten.createWeatherList();

        Path filePath = Path.of(Objects.requireNonNull(getClass().getResource("/csv/weather-small.csv")).toURI());
        BufferedReader bufferedReader = Files.newBufferedReader(filePath);

        try (TableFileReader<Weather> weatherReader = tableFileReaderFactory.create(
                bufferedReader,
                FileType.CSV,
                Weather.class
        )) {
            List<Weather> actual = weatherReader.stream().toList();
            assertEquals(3, actual.size());
            assertEquals(expected, actual);
        }
    }

    @Test
    @SneakyThrows
    @DisplayName("Should read records one by one using read")
    void shouldReadRecordsOneByOne() {

        List<Weather> expected = TestDaten.createWeatherList();

        Path filePath = Path.of(Objects.requireNonNull(getClass().getResource("/csv/weather-small.csv")).toURI());
        BufferedReader bufferedReader = Files.newBufferedReader(filePath);

        try (TableFileReader<Weather> weatherReader = tableFileReaderFactory.create(
                bufferedReader,
                FileType.CSV,
                Weather.class
        )) {
            Optional<Weather> first = weatherReader.read();
            assertTrue(first.isPresent());
            assertEquals(expected.get(0), first.get());

            Optional<Weather> second = weatherReader.read();
            assertTrue(second.isPresent());
            assertEquals(expected.get(1), second.get());

            Optional<Weather> third = weatherReader.read();
            assertTrue(third.isPresent());
            assertEquals(expected.get(2), third.get());

            Optional<Weather> fourth = weatherReader.read();
            assertTrue(fourth.isEmpty());
        }
    }

    @Test
    @SneakyThrows
    @DisplayName("Should iterate using for-each loop")
    void shouldIterateUsingForEach() {

        List<Weather> expected = TestDaten.createWeatherList();

        Path filePath = Path.of(Objects.requireNonNull(getClass().getResource("/csv/weather-small.csv")).toURI());
        BufferedReader bufferedReader = Files.newBufferedReader(filePath);

        List<Weather> actual = new ArrayList<>();
        try (TableFileReader<Weather> weatherReader = tableFileReaderFactory.create(
                bufferedReader,
                FileType.CSV,
                Weather.class
        )) {
            for (Weather weather : weatherReader) {
                actual.add(weather);
            }
        }

        assertEquals(expected, actual);
    }

}
