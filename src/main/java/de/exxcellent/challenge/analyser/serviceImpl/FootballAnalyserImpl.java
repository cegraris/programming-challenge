package de.exxcellent.challenge.analyser.serviceImpl;

import de.exxcellent.challenge.analyser.service.FootballAnalyser;
import de.exxcellent.challenge.analyser.service.WeatherAnalyser;
import de.exxcellent.challenge.exception.AppException;
import de.exxcellent.challenge.model.Football;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Implementation of {@link WeatherAnalyser}
 */
@Component
public class FootballAnalyserImpl implements FootballAnalyser {

    @Override
    public Set<String> findTeamWithSmallestAbsGoalDifference(Stream<Football> footballs) {
        Set<String> seenTeams = new HashSet<>();
        Set<String> result = new HashSet<>();
        AtomicInteger minAbsDifference = new AtomicInteger(Integer.MAX_VALUE);

        footballs
                .peek(f -> validateFootball(f, seenTeams))
                .forEach(f -> {
                    int absDifference = Math.abs(f.getGoals() - f.getGoalsAllowed());
                    if (absDifference < minAbsDifference.get()) {
                        minAbsDifference.set(absDifference);
                        result.clear();
                        result.add(f.getTeam());
                    } else if (absDifference == minAbsDifference.get()) {
                        result.add(f.getTeam());
                    }
                });

        if (result.isEmpty()) {
            throw AppException.analysisFailed("No valid data after filtering");
        }
        return result;
    }

    /**
     * Validates a single football record.
     *
     * @param record    the football record to validate
     * @param seenTeams set of team names already processed, used for duplicate detection
     * @throws AppException if validation fails
     */
    private void validateFootball(Football record, Set<String> seenTeams) {
        if (!seenTeams.add(record.getTeam())) {
            throw AppException.analysisFailed(
                    String.format("Duplicate Team: %s", record.getTeam()));
        }
    }

}
