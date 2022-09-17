package de.unistuttgart.overworldbackend.data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;

/**
 * A course is the highest-level entity:
 * It contains everything, all worlds and dungeons, minigames, NPCs, and player statistics.<br>
 * When multiple courses exist, no data will be shared between them.
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "courseName", "semester" }) })
@Getter
@Setter
@ToString
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
    @ToString.Exclude
    List<World> worlds;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    Set<PlayerStatistic> playerStatistics = new HashSet<>();

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
            world.setCourse(this);
            world
                .getMinigameTasks()
                .forEach(minigameTask -> {
                    minigameTask.setCourse(this);
                    minigameTask.setArea(world);
                });
            world
                .getNpcs()
                .forEach(npc -> {
                    npc.setCourse(this);
                    npc.setArea(world);
                });
            for (final Dungeon dungeon : world.getDungeons()) {
                dungeon.setWorld(world);
                dungeon.setCourse(this);
                dungeon
                    .getMinigameTasks()
                    .forEach(minigameTask -> {
                        minigameTask.setCourse(this);
                        minigameTask.setArea(dungeon);
                    });
                dungeon
                    .getNpcs()
                    .forEach(npc -> {
                        npc.setCourse(this);
                        npc.setArea(dungeon);
                    });
            }
        });
    }

    public void addPlayerStatistic(final PlayerStatistic playerStatistic) {
        this.playerStatistics.add(playerStatistic);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o != null) {
            Hibernate.getClass(this);
            Hibernate.getClass(o);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
