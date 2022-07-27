package de.unistuttgart.overworldbackend.service;

import de.unistuttgart.overworldbackend.data.Area;
import de.unistuttgart.overworldbackend.data.AreaLocationDTO;
import de.unistuttgart.overworldbackend.data.Dungeon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AreaService {

  @Autowired
  private DungeonService dungeonService;

  @Autowired
  private WorldService worldService;

  public Area getAreaFromAreaLocationDTO(int lectureId, AreaLocationDTO areaLocationDTO) {
    if (areaLocationDTO.getDungeonIndex() != null) {
      return dungeonService.getDungeonByIndexFromLecture(
        lectureId,
        areaLocationDTO.getWorldIndex(),
        areaLocationDTO.getDungeonIndex()
      );
    }
    return worldService.getWorldByIndexFromLecture(lectureId, areaLocationDTO.getWorldIndex());
  }
}
