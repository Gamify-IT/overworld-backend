package de.unistuttgart.overworldbackend.data.mapper;

import de.unistuttgart.overworldbackend.data.PlayerStatistic;
import de.unistuttgart.overworldbackend.data.PlayerStatisticDTO;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { AreaLocationMapper.class, AreaLocationMapper.class })
public interface PlayerStatisticMapper {
    PlayerStatisticDTO playerStatisticToPlayerstatisticDTO(final PlayerStatistic playerstatistic);

    @Mapping(target = "lastActive", source = "lastActive", dateFormat = "yyyy-MM-dd HH:mm:ss")
    PlayerStatisticDTO playerStatisticToPlayerStatisticDTO(PlayerStatistic playerStatistic);

    @Mapping(target = "lastActive", source = "lastActive", dateFormat = "yyyy-MM-dd HH:mm:ss")
    PlayerStatistic playerStatisticDTOToPlayerStatistic(PlayerStatisticDTO playerStatisticDTO);

    default String map(LocalDateTime value) {
        return value != null ? value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
    }

    default LocalDateTime map(String value) {
        return value != null ? LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
    }
}
