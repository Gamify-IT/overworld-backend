package de.unistuttgart.overworldbackend.data.statistics;

import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerJoinedStatistic {

    int totalPlayers;
    Set<PlayerJoined> joined;
}
