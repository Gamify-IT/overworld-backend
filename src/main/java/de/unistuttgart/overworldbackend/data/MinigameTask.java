package de.unistuttgart.overworldbackend.data;

import de.unistuttgart.overworldbackend.data.enums.Minigame;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;
import org.springframework.lang.Nullable;

/**
 * A minigame tasks stores the information about a task which game type is played and with which configuration.
 *
 * A minigame task is located in an area and a player can walk in such a minigame spot to start the minigame.
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "index", "area_id", "course_id" }) })
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MinigameTask {

    @Id
    @GeneratedValue(generator = "uuid")
    UUID id;

    int index;

    @Enumerated(EnumType.STRING)
    Minigame game;

    UUID configurationId;

    @Nullable
    String description;

    @ManyToOne
    Course course;

    @ManyToOne
    Area area;

    public MinigameTask(final Minigame game, final UUID configurationId, final int index) {
        this.game = game;
        this.configurationId = configurationId;
        this.index = index;
    }

    public MinigameTask(final Minigame game, String description, final UUID configurationId, final int index) {
        this.game = game;
        this.configurationId = configurationId;
        this.index = index;
        this.description = description;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        final MinigameTask that = (MinigameTask) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
