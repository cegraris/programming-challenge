package de.exxcellent.challenge.analyser.serviceImpl;

import de.exxcellent.challenge.analyser.service.WeatherAnalyser;
import de.exxcellent.challenge.exception.AppException;
import de.exxcellent.challenge.model.Weather;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Implementation of {@link WeatherAnalyser}
 */
@Component
public class WeatherAnalyserImpl implements WeatherAnalyser {
    @Override
    public Set<Integer> findDayOfSmallestTemperatureSpread(Stream<Weather> weathers) {
        Set<Integer> seenDays = new HashSet<>();
        Set<Integer> result = new HashSet<>();
        AtomicInteger minSpread = new AtomicInteger(Integer.MAX_VALUE);

        weathers
                .peek(w -> validateWeather(w, seenDays))
                .forEach(w -> {
                    int spread = w.getMaxTemperature() - w.getMinTemperature();
                    if (spread < minSpread.get()) {
                        minSpread.set(spread);
                        result.clear();
                        result.add(w.getDay());
                    } else if (spread == minSpread.get()) {
                        result.add(w.getDay());
                    }
                });

        if (result.isEmpty()) {
            throw AppException.analysisFailed("No valid data after filtering");
        }
        return result;
    }

    /**
     * Validates a single weather record.
     * <p>Checks that:
     * <ul>
     *   <li>min temperature does not exceed max temperature</li>
     *   <li>day is not duplicated in the dataset</li>
     * </ul>
     *
     * @param record        the weather record to validate
     * @param seenDays seenDays set of days already processed, used for duplicate detection
     * @throws AppException if validation fails
     */
    private void validateWeather(Weather record, Set<Integer> seenDays) {
        if (!seenDays.add(record.getDay())) {
            throw AppException.analysisFailed(
                    String.format("Duplicate day: %d", record.getDay()));
        }
        if (record.getMinTemperature() > record.getMaxTemperature()) {
            throw AppException.analysisFailed(
                    String.format("Day %d: min temperature (%d) exceeds max temperature (%d)",
                            record.getDay(), record.getMinTemperature(), record.getMaxTemperature()));
        }
    }

}
