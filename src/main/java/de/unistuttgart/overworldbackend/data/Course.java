package de.unistuttgart.overworldbackend.data;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * A course is the highest-level entity:
 * It contains everything, all worlds and dungeons, minigames, NPCs, and player statistics.<br>
 * When multiple courses exist, no data will be shared between them.
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "courseName", "semester" }) })
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Course {

    static final String SEMESTER_PATTERN = "^(WS|SS)-\\d\\d$";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @NotNull
    String courseName;

    @Pattern(regexp = SEMESTER_PATTERN)
    String semester;

    String description;
    boolean active;

    @OneToMany(cascade = CascadeType.ALL)
    List<World> worlds;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    Set<PlayerStatistic> playerStatistics = new HashSet<>();

    @JsonManagedReference(value = "achievement-course")
    @OneToMany(
        mappedBy = "course",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        targetEntity = AchievementStatistic.class
    )
    List<AchievementStatistic> achievementStatistics = new ArrayList<>();

    @ManyToMany(mappedBy = "courses")
    private List<Player> players = new ArrayList<>();

    public Course(
        final String courseName,
        final String semester,
        final String description,
        final boolean active,
        final List<World> worlds
    ) {
        this.courseName = courseName;
        this.semester = semester;
        this.description = description;
        this.active = active;
        this.worlds = worlds;
    }

    @PrePersist
    private void updateCourseIds() {
        worlds.forEach(world -> {
            setAreaCourseId(world);
            for (final Dungeon dungeon : world.getDungeons()) {
                dungeon.setWorld(world);
                setAreaCourseId(dungeon);
            }
        });
    }

    public void addPlayerStatistic(final PlayerStatistic playerStatistic) {
        this.playerStatistics.add(playerStatistic);
    }

    public void removePlayerStatistic(final PlayerStatistic playerStatistic) {
        this.playerStatistics.remove(playerStatistic);
    }

    public void clearPlayerStatistics() {
        this.playerStatistics.clear();
    }

    private void setAreaCourseId(final Area area) {
        area.setCourse(this);
        area
            .getMinigameTasks()
            .forEach(minigameTask -> {
                minigameTask.setCourse(this);
                minigameTask.setArea(area);
            });
        area
            .getNpcs()
            .forEach(npc -> {
                npc.setCourse(this);
                npc.setArea(area);
            });
        area
            .getBooks()
            .forEach(book -> {
                book.setCourse(this);
                book.setArea(area);
            });
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }
}
