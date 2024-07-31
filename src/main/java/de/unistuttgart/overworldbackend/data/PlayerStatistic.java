package de.unistuttgart.overworldbackend.data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * The statistic of a player in a course.
 *
 * It contains informations about unlocked areas, completed dungeons, current area,
 * statistics of minigames and statistics of npcs.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerStatistic {

    @Id
    @GeneratedValue(generator = "uuid")
    UUID id;

    @ManyToMany(cascade = CascadeType.ALL)
    List<Area> unlockedAreas;

    @ManyToMany(cascade = CascadeType.ALL)
    List<Area> completedDungeons;

    @ManyToMany(cascade = CascadeType.ALL)
    Set<Teleporter> unlockedTeleporters;

    @ManyToOne(cascade = CascadeType.ALL)
    Area currentArea;

    @ManyToOne(cascade = CascadeType.ALL)
    Course course;

    @NotNull
    String userId;

    @NotNull
    String username;

    LocalDateTime created;

    LocalDateTime lastActive;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    Set<PlayerTaskStatistic> playerTaskStatistics = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    Set<PlayerNPCStatistic> playerNPCStatistics = new HashSet<>();

    @PrePersist
    void onCreate() {
        lastActive = LocalDateTime.now();
        created = LocalDateTime.now();
    }

    long knowledge = 0;

    int rewards = 0;

    boolean showRewards = false;

    String pseudonym;


    public void addKnowledge(final long gainedKnowledge) {
        knowledge += gainedKnowledge;
    }

    public void addPlayerTaskStatistic(final PlayerTaskStatistic playerTaskStatistic) {
        this.playerTaskStatistics.add(playerTaskStatistic);
    }

    public void addPlayerNPCStatistic(final PlayerNPCStatistic playerNPCStatistic) {
        this.playerNPCStatistics.add(playerNPCStatistic);
    }

    public void addUnlockedArea(final Area area) {
        if (!this.unlockedAreas.contains(area)) {
            this.unlockedAreas.add(area);
        }
    }

    public void addUnlockedTeleporter(final Teleporter teleporter) {
        this.unlockedTeleporters.add(teleporter);
    }

    public void addRewards(final int gainedRewards) {
        rewards += gainedRewards;
    }


}
