package de.unistuttgart.overworldbackend.data.mapper;

import de.unistuttgart.overworldbackend.data.Area;
import de.unistuttgart.overworldbackend.data.AreaLocationDTO;
import de.unistuttgart.overworldbackend.data.Dungeon;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class AreaLocationMapper {

  public AreaLocationDTO areaToAreaLocationDTO(final Area area) {
    if (area instanceof Dungeon) {
      Dungeon dungeon = (Dungeon) area;
      return new AreaLocationDTO(dungeon.getWorld().getIndex(), dungeon.getIndex());
    } else {
      return new AreaLocationDTO(area.getIndex(), null);
    }
  }

  public List<AreaLocationDTO> areasToAreaLocationDTOs(final List<Area> areas) {
    List<AreaLocationDTO> areaLocationDTOS = new ArrayList<>();
    for (Area area : areas) {
      areaLocationDTOS.add(areaToAreaLocationDTO(area));
    }
    return areaLocationDTOS;
  }
}
