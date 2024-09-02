package de.unistuttgart.overworldbackend.data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

/**
 * Data Transfer Object for PlayerStatistic.
 *
 * @see PlayerStatistic
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerStatisticDTO {

    @Nullable
    UUID id;

    List<AreaLocationDTO> unlockedAreas;

    List<AreaLocationDTO> completedDungeons;
    Set<TeleporterDTO> unlockedTeleporters;

    AreaLocationDTO currentArea;

    String userId;
    String username;

    String created;

    String lastActive;
    
    int volumeLevel;

    float logoutPositionX;

    float logoutPositionY;

    String logoutScene;

    int currentCharacterIndex;

    long knowledge;

    int rewards;

    boolean visibility;

    int credit;

    String pseudonym;

}
