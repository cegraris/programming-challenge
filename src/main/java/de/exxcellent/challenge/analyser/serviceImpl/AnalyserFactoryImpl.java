package de.exxcellent.challenge.analyser.serviceImpl;

import de.exxcellent.challenge.analyser.service.AnalyserFactory;
import de.exxcellent.challenge.analyser.service.WeatherAnalyser;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link AnalyserFactory}.
 */
@Component
public class AnalyserFactoryImpl implements AnalyserFactory {

    private final WeatherAnalyser weatherAnalyser;

    public AnalyserFactoryImpl(WeatherAnalyser weatherAnalyser) {
        this.weatherAnalyser = weatherAnalyser;
    }

    @Override
    public WeatherAnalyser getWeatherAnalyser() {
        return weatherAnalyser;
    }
}
