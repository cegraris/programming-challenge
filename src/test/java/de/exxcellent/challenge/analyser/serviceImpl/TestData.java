package de.exxcellent.challenge.analyser.serviceImpl;

import de.exxcellent.challenge.model.Football;
import de.exxcellent.challenge.model.Weather;
import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * Test data for the Unit-Tests.
 */
@UtilityClass
class TestData {

    static List<Weather> createWeatherList() {
        return List.of(
                new Weather(1, 30, 20, 74, 53.8, 0.0, 280, 9.6, 270, 17.0, 1.6, 93, 23, 1004.5),
                new Weather(2, 25, 22, 71, 46.5, 0.0, 330, 8.7, 340, 23.0, 3.3, 70, 28, 1004.5),
                new Weather(3, 28, 18, 66, 39.6, 0.0, 350, 5.0, 350, 9.0, 2.8, 59, 24, 1016.8)
        );
    }

    static List<Weather> createWeatherListWithSingleRecord() {
        return List.of(
                new Weather(2, 30, 20, 74, 53.8, 0.0, 280, 9.6, 270, 17.0, 1.6, 93, 23, 1004.5)
        );
    }

    static List<Weather> createWeatherListWithMultipleSameSmallestTemperatureSpread() {
        return List.of(
                new Weather(1, 30, 20, 74, 53.8, 0.0, 280, 9.6, 270, 17.0, 1.6, 93, 23, 1004.5),
                new Weather(2, 25, 22, 71, 46.5, 0.0, 330, 8.7, 340, 23.0, 3.3, 70, 28, 1004.5),
                new Weather(3, 28, 25, 66, 39.6, 0.0, 350, 5.0, 350, 9.0, 2.8, 59, 24, 1016.8)
        );
    }

    static List<Weather> createWeatherListWithMnTExceedsMxT() {
        return List.of(
                new Weather(1, 88, 59, 74, 53.8, 0.0, 280, 9.6, 270, 17.0, 1.6, 93, 23, 1004.5),
                new Weather(2, 79, 63, 71, 46.5, 0.0, 330, 8.7, 340, 23.0, 3.3, 70, 28, 1004.5),
                new Weather(3, 77, 78, 66, 39.6, 0.0, 350, 5.0, 350, 9.0, 2.8, 59, 24, 1016.8),
                new Weather(4, 77, 55, 66, 39.6, 0.0, 350, 5.0, 350, 9.0, 2.8, 59, null, 1016.8)
        );
    }

    static List<Weather> createWeatherListWithDuplicatedDay() {
        return List.of(
                new Weather(1, 88, 59, 74, 53.8, 0.0, 280, 9.6, 270, 17.0, 1.6, 93, 23, 1004.5),
                new Weather(2, 79, 63, 71, 46.5, 0.0, 330, 8.7, 340, 23.0, 3.3, 70, 28, 1004.5),
                new Weather(2, 77, 55, 66, 39.6, 0.0, 350, 5.0, 350, 9.0, 2.8, 59, 24, 1016.8),
                new Weather(4, 77, 55, 66, 39.6, 0.0, 350, 5.0, 350, 9.0, 2.8, 59, null, 1016.8)
        );
    }

    static List<Football> createFootballList() {
        return List.of(
                new Football("Arsenal", 38, 26, 9, 3, 79, 36, 87),
                new Football("Liverpool", 38, 24, 8, 6, 67, 30, 80),
                new Football("Manchester United", 38, 24, 5, 9, 87, 45, 77)
        );
    }

    static List<Football> createFootballListWithSameSmallestAbsGoalDifference() {
        return List.of(
                new Football("Arsenal", 38, 26, 9, 3, 79, 36, 87),
                new Football("Liverpool", 38, 24, 8, 6, 67, 30, 80),
                new Football("Manchester United", 38, 24, 5, 9, 50, 87, 77)
        );
    }

    static List<Football> createFootballListWithDuplicatedTeamName() {
        return List.of(
                new Football("Arsenal", 38, 26, 9, 3, 79, 36, 87),
                new Football("Arsenal", 38, 24, 8, 6, 67, 30, 80),
                new Football("Manchester United", 38, 24, 5, 9, 87, 45, 77)
        );
    }

    static List<Football> createFootballListWithGoalsSmallerThanZero() {
        return List.of(
                new Football("Arsenal", 38, 26, 9, 3, 79, -36, 87),
                new Football("Liverpool", 38, 24, 8, 6, 67, 30, 80),
                new Football("Manchester United", 38, 24, 5, 9, 50, 87, 77)
        );
    }

}
