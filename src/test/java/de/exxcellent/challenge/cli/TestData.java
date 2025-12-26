package de.exxcellent.challenge.cli;

import de.exxcellent.challenge.exception.AppException;
import de.exxcellent.challenge.reader.model.WeatherCsv;
import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * Test data for the Unit-Tests.
 */
@UtilityClass
class TestData {
    static List<WeatherCsv> createWeatherList() {
        return List.of(
                new WeatherCsv(1, 88, 59, 74, 53.8, 0.0, 280, 9.6, 270, 17.0, 1.6, 93, 23, 1004.5),
                new WeatherCsv(2, 79, 63, 71, 46.5, 0.0, 330, 8.7, 340, 23.0, 3.3, 70, 28, 1004.5),
                new WeatherCsv(3, 77, 55, 66, 39.6, 0.0, 350, 5.0, 350, 9.0, 2.8, 59, 24, 1016.8),
                new WeatherCsv(4, 77, 55, 66, 39.6, 0.0, 350, 5.0, 350, 9.0, 2.8, 59, null, 1016.8)
        );
    }

    static List<AppException> createFailedRowsExceptions() {
        return List.of(
                AppException.malformedRow(3, AppException.unexpected("test", null)),
                AppException.malformedRow(5, AppException.unexpected("test", null)),
                AppException.malformedRow(7, AppException.unexpected("test", null)),
                AppException.malformedRow(9, AppException.unexpected("test", null)),
                AppException.malformedRow(10, AppException.unexpected("test", null))
        );
    }
}
