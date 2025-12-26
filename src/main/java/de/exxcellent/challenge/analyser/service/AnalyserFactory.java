package de.exxcellent.challenge.analyser.service;

/**
 * Factory interface for getting {@code Analyser} instances.
 */
public interface AnalyserFactory {
    /**
     * @return a {@link WeatherAnalyser} instance
     */
    WeatherAnalyser getWeatherAnalyser();

    /**
     * @return a {@link FootballAnalyser} instance
     */
    FootballAnalyser getFootballAnalyser();
}
