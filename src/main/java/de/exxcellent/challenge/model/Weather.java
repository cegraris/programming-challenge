package de.exxcellent.challenge.model;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain model of weather.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Weather {
    @CsvBindByName(column = "Day")
    private Integer day;

    @CsvBindByName(column = "MxT")
    private Integer maxTemperature;

    @CsvBindByName(column = "MnT")
    private Integer minTemperature;

    @CsvBindByName(column = "AvT")
    private Integer avgTemperature;

    @CsvBindByName(column = "AvDP")
    private Double avgDewPoint;

    @CsvBindByName(column = "1HrP TPcpn")
    private Double oneHourPrecipitation;

    @CsvBindByName(column = "TPcpn")
    private Double totalPrecipitation;

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
