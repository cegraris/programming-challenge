package de.exxcellent.challenge.reader.model;

import com.opencsv.bean.CsvBindByName;
import de.exxcellent.challenge.model.Football;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Csv DTO of {@link Football}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FootballCsv {

    @CsvBindByName(column = "Team", required = true)
    private String team;

    @CsvBindByName(column = "Games")
    private Integer games;

    @CsvBindByName(column = "Wins")
    private Integer wins;

    @CsvBindByName(column = "Losses")
    private Integer losses;

    @CsvBindByName(column = "Draws")
    private Integer draws;

    @CsvBindByName(column = "Goals", required = true)
    private Integer goals;

    @CsvBindByName(column = "Goals Allowed", required = true)
    private Integer goalsAllowed;

    @CsvBindByName(column = "Points")
    private Integer points;
}