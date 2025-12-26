package de.exxcellent.challenge.reader.model;

import com.opencsv.bean.CsvBindByName;
import de.exxcellent.challenge.model.Weather;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Csv DTO of {@link Weather}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherCsv {
    @CsvBindByName(column = "Day", required = true)
    private Integer day;

    @CsvBindByName(column = "MxT", required = true)
    private Integer maxTemperature;

    @CsvBindByName(column = "MnT", required = true)
    private Integer minTemperature;

    @CsvBindByName(column = "AvT")
    private Integer avgTemperature;

    @CsvBindByName(column = "AvDP")
    private Double avgDewPoint;

    @CsvBindByName(column = "1HrP TPcpn")
    private Double oneHourPrecipitation;

    @CsvBindByName(column = "PDir")
    private Integer prevailingWindDirection;

    @CsvBindByName(column = "AvSp")
    private Double avgWindSpeed;

    @CsvBindByName(column = "Dir")
    private Integer windDirection;

    @CsvBindByName(column = "MxS")
    private Double maxWindSpeed;

    @CsvBindByName(column = "SkyC")
    private Double skyCover;

    @CsvBindByName(column = "MxR")
    private Integer maxRelativeHumidity;

    @CsvBindByName(column = "Mn")
    private Integer minRelativeHumidity;

    @CsvBindByName(column = "R AvSLP")
    private Double avgSeaLevelPressure;
}
