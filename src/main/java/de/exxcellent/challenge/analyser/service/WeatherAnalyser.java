package de.exxcellent.challenge.analyser.service;

import de.exxcellent.challenge.exception.AppException;
import de.exxcellent.challenge.model.Weather;

import java.util.Set;
import java.util.stream.Stream;

/**
 * Analyser for {@link Weather} data.
 */
public interface WeatherAnalyser {
    /**
     * Finds the weather record with the smallest temperature spread.
     *
     * @param weathers the dataset to analyse
     * @return the day with minimum (max - min) temperature, return multiple days, if they all have the same minimum temperature
     * @throws AppException if the dataset does not meet relational constraints or is empty.
     */
    Set<Integer> findDayOfSmallestTemperatureSpread(Stream<Weather> weathers);

}
