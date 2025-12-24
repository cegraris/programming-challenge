package de.exxcellent.challenge.analyser.serviceImpl;

import de.exxcellent.challenge.analyser.service.WeatherAnalyser;
import de.exxcellent.challenge.model.Weather;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Stream;

/**
 * Implementation of {@link WeatherAnalyser}
 */
@Component
public class WeatherAnalyserImpl implements WeatherAnalyser {
    @Override
    public Set<Integer> findDayOfSmallestTemperatureSpread(Stream<Weather> weathers) {
        return null;
    }

}
