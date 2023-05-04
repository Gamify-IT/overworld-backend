package de.unistuttgart.overworldbackend.data.statistics;

import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerJoinedStatistic {

    int totalPlayers;
    Set<PlayerJoined> joined;

    public PlayerJoinedStatistic() {
        totalPlayers = 0;
        joined = new HashSet<>();
    }

    public void addPlayer() {
        totalPlayers++;
    }
}
