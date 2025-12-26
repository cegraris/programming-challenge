package de.exxcellent.challenge.mapper;

import de.exxcellent.challenge.model.Weather;
import de.exxcellent.challenge.reader.model.WeatherCsv;
import org.mapstruct.Mapper;

/**
 * Maps {@link Weather} relate classes.
 */
@Mapper(componentModel = "spring")
public interface WeatherMapper {
    Weather toDomain(WeatherCsv row);

}
