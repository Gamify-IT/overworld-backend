package de.unistuttgart.overworldbackend.data;

import java.util.List;
import java.util.UUID;
import javax.persistence.ElementCollection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

/**
 * Data Transfer Object for CustomAreaMap.
 *
 * @see CustomAreaMap
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomAreaMapDTO {

    @Nullable
    UUID id;

    LayoutDTO layout;

    @ElementCollection
    List<MinigameSpotDTO> minigameSpots;

    @ElementCollection
    List<NPCSpotDTO> npcSpots;

    @ElementCollection
    List<BookSpotDTO> bookSpots;

    @ElementCollection
    List<BarrierSpotDTO> barrierSpots;

    @ElementCollection
    List<TeleporterSpotDTO> teleporterSpots;

    @ElementCollection
    List<SceneTransitionSpotDTO> sceneTransitionSpots;
}
