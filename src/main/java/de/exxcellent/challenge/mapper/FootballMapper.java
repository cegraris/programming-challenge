package de.exxcellent.challenge.mapper;

import de.exxcellent.challenge.model.Football;
import de.exxcellent.challenge.reader.model.FootballCsv;
import org.mapstruct.Mapper;

/**
 * Maps {@link Football} relate classes.
 */
@Mapper(componentModel = "spring")
public interface FootballMapper {
    Football toDomain(FootballCsv row);

}
