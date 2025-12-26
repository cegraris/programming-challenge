package de.exxcellent.challenge.analyser.serviceImpl;

import de.exxcellent.challenge.analyser.service.AnalyserFactory;
import de.exxcellent.challenge.analyser.service.FootballAnalyser;
import de.exxcellent.challenge.analyser.service.WeatherAnalyser;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link AnalyserFactory}.
 */
@Component
public class AnalyserFactoryImpl implements AnalyserFactory {

    private final WeatherAnalyser weatherAnalyser;

    private final FootballAnalyser footballAnalyser;

    public AnalyserFactoryImpl(WeatherAnalyser weatherAnalyser, FootballAnalyser footballAnalyser) {
        this.weatherAnalyser = weatherAnalyser;
        this.footballAnalyser = footballAnalyser;
    }

    @Override
    public WeatherAnalyser getWeatherAnalyser() {
        return weatherAnalyser;
    }

    @Override
    public FootballAnalyser getFootballAnalyser() {
        return footballAnalyser;
    }
}
