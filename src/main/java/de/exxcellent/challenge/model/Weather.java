package de.exxcellent.challenge.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain model of weather.
 */
@Data
@NoArgsConstructor
public class Weather {
    private Integer day;

    private Integer maxTemperature;

    private Integer minTemperature;

    private Integer avgTemperature;

    private Double avgDewPoint;

    private Double oneHourPrecipitation;

    private Double totalPrecipitation;

    private Integer prevailingWindDirection;

    private Double avgWindSpeed;

    private Integer windDirection;

    private Double maxWindSpeed;

    private Double skyCover;

    private Integer maxRelativeHumidity;

    private Integer minRelativeHumidity;

    private Double avgSeaLevelPressure;
}
