package de.exxcellent.challenge.reader.serviceImpl;

import de.exxcellent.challenge.App;
import de.exxcellent.challenge.model.Weather;
import de.exxcellent.challenge.reader.model.FileType;
import de.exxcellent.challenge.reader.service.TableFileReader;
import de.exxcellent.challenge.reader.service.TableFileReaderFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit-Test für die Klasse {@link TableFileReaderCsvImpl}.
 */
@SpringBootTest(classes = App.class)
public class TableFileReaderCsvImplTest {

    @Autowired
    private TableFileReaderFactory tableFileReaderFactory;

    /**
     * Tests that the factory correctly creates a CSV reader that can parse weather data.
     * Case0: 3 rows of correct weather data with usual headers
     */
    @Test
    @SneakyThrows
    void readCsvFileSuccessfullyCase0() {

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

}
