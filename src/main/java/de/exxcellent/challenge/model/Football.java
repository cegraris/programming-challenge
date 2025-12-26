package de.exxcellent.challenge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain Model of {@code Football}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Football {

    private String team;

    private Integer games;

    private Integer wins;

    private Integer losses;

    private Integer draws;

    private Integer goals;

    private Integer goalsAllowed;

    private Integer points;
}