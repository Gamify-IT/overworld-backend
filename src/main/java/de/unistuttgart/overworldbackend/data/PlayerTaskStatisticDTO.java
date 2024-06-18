package de.unistuttgart.overworldbackend.data;

import java.util.Map;
import java.util.UUID;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

/**
 * Data Transfer Object for PlayerTaskStatistic.
 *
 * @see PlayerTaskStatistic
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerTaskStatisticDTO {

    @Nullable
    UUID id;

    @Min(0)
    @Max(100)
    long highscore;

    boolean completed;

    MinigameTaskDTO minigameTask;

    int rewards;

    int totalRewards;

    String userId;
}
