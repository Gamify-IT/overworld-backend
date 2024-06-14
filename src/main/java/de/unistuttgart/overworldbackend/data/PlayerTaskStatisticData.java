package de.unistuttgart.overworldbackend.data;

import de.unistuttgart.overworldbackend.data.enums.Minigame;
import java.util.Map;
import java.util.UUID;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Data Transfer Object to communicate a change of a minigame run from a player.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerTaskStatisticData {

    @NotNull
    Minigame game;

    @NotNull
    UUID configurationId;

    @Min(0)
    @Max(100)
    long score;

    @NotNull
    String userId;

    @Min(0)
    @Max(10)
    int rewards;

    @Min(0)
    @Max(200)
    int totalRewards;
}
