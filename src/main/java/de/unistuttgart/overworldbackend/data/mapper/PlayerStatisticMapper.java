package de.unistuttgart.overworldbackend.data.mapper;

import de.unistuttgart.overworldbackend.data.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { AreaLocationMapper.class, AreaLocationMapper.class })
public interface PlayerStatisticMapper {
  PlayerStatisticDTO playerStatisticToPlayerstatisticDTO(final PlayerStatistic playerstatistic);

  PlayerStatistic playerStatisticDTOToPlayerStatistic(final PlayerStatisticDTO playerstatisticDTO);
}
