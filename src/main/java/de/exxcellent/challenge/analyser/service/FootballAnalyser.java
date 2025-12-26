package de.exxcellent.challenge.analyser.service;

import de.exxcellent.challenge.exception.AppException;
import de.exxcellent.challenge.model.Football;

import java.util.Set;
import java.util.stream.Stream;

/**
 * Analyser for {@link Football} data.
 */
public interface FootballAnalyser {
    /**
     * Finds the football team(s) with the smallest absolute goal difference.
     *
     * @param footballs the dataset to analyse
     * @return the team name(s) with minimum {@code abs(goals - goalsAllowed)}, return multiple teams if they all
     * have the same minimum value
     * @throws AppException if the dataset does not meet relational constraints or is empty.
     */
    Set<String> findTeamWithSmallestAbsGoalDifference(Stream<Football> footballs);

}
