package de.exxcellent.challenge.analyser.serviceImpl;

import de.exxcellent.challenge.App;
import de.exxcellent.challenge.analyser.service.WeatherAnalyser;
import de.exxcellent.challenge.exception.AppException;
import de.exxcellent.challenge.exception.ErrorCode;
import de.exxcellent.challenge.model.Weather;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Unit-Test für die Klasse {@link WeatherAnalyserImpl}.
 */
@SpringBootTest(classes = App.class)
class WeatherAnalyserImplTest {

    @Autowired
    private WeatherAnalyser weatherAnalyser;

    // ==================== Happy Path Tests ====================

    @Test
    @DisplayName("Should return correct day with smallest temperature spread")
    void findDayOfSmallestTemperatureSpreadReturnsCorrectDay() {
        Stream<Weather> weathers = TestDaten.createWeatherList().stream();
        Set<Integer> expected = Set.of(2);

        Set<Integer> result = weatherAnalyser.findDayOfSmallestTemperatureSpread(weathers);

        assertEquals(expected, result);
    }

    // ==================== Edge Cases ====================

    @Test
    @DisplayName("Should return that day when only single record exists")
    void findDayOfSmallestTemperatureSpreadWithSingleRecordReturnsThatDay() {
        Stream<Weather> weathers = TestDaten.createWeatherListWithSingleRecord().stream();
        Set<Integer> expected = Set.of(2);

        Set<Integer> result = weatherAnalyser.findDayOfSmallestTemperatureSpread(weathers);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Should return all days when multiple records have same smallest spread")
    void findDayOfSmallestTemperatureSpreadWithSameSpreadReturnsAllDays() {
        Stream<Weather> weathers = TestDaten.createWeatherListWithMultipleSameSmallestTemperatureSpread().stream();
        Set<Integer> expected = Set.of(2, 3);

        Set<Integer> result = weatherAnalyser.findDayOfSmallestTemperatureSpread(weathers);

        assertEquals(expected, result);
    }

    // ==================== Error Cases ====================

    @Test
    @DisplayName("Should throw exception when filtered result is empty")
    void findDayOfSmallestTemperatureSpreadWithEmptyFilteredResultThrowsException() {
        Stream<Weather> weathers = Stream.empty();

        AppException exception = assertThrows(AppException.class,
                () -> weatherAnalyser.findDayOfSmallestTemperatureSpread(weathers));

        assertEquals(ErrorCode.ANALYSIS_FAILED, exception.getErrorCode());
    }

    @Test
    @DisplayName("Should throw exception when min temperature exceeds max temperature")
    void findDayOfSmallestTemperatureSpreadWithMinExceedsMaxThrowsException() {
        Stream<Weather> weathers = TestDaten.createWeatherListWithMnTExceedsMxT().stream();

        AppException exception = assertThrows(AppException.class,
                () -> weatherAnalyser.findDayOfSmallestTemperatureSpread(weathers));

        assertEquals(ErrorCode.ANALYSIS_FAILED, exception.getErrorCode());
    }

    @Test
    @DisplayName("Should throw exception when duplicate day exists")
    void findDayOfSmallestTemperatureSpreadWithDuplicateDayThrowsException() {
        Stream<Weather> weathers = TestDaten.createWeatherListWithDuplicatedDay().stream();

        AppException exception = assertThrows(AppException.class,
                () -> weatherAnalyser.findDayOfSmallestTemperatureSpread(weathers));

        assertEquals(ErrorCode.ANALYSIS_FAILED, exception.getErrorCode());
    }

}
