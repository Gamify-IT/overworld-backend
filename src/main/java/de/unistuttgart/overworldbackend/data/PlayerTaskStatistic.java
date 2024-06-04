package de.unistuttgart.overworldbackend.data;

import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Store the progress of a player with its player statistic of a minigame task.
 *
 * @see PlayerStatistic
 * @see MinigameTask
 */
@Entity
@Table(
    uniqueConstraints = { @UniqueConstraint(columnNames = { "player_statistic_id", "minigame_task_id", "course_id" }) }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerTaskStatistic {

    @Id
    @GeneratedValue(generator = "uuid")
    UUID id;

    @ManyToOne
    PlayerStatistic playerStatistic;

    @ManyToOne
    MinigameTask minigameTask;

    @ManyToOne
    Course course;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    Set<PlayerTaskActionLog> playerTaskActionLogs = new HashSet<>();

    @Min(0)
    @Max(100)
    long highscore = 0;

    boolean completed = false;

    int rewards;


    public void addActionLog(final PlayerTaskActionLog actionLog) {
        this.playerTaskActionLogs.add(actionLog);
    }
}
