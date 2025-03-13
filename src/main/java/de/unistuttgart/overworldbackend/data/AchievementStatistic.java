package de.unistuttgart.overworldbackend.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(columnNames = { "player_user_id", "course_id", "achievement_id" }),
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AchievementStatistic {

    @Id
    @GeneratedValue(generator = "uuid")
    UUID id;

    @JsonBackReference(value = "achievement-player")
    @ManyToOne(cascade = CascadeType.ALL)
    Player player;

    @JsonBackReference(value = "achievement-course")
    @ManyToOne(cascade = CascadeType.ALL)
    Course course;

    @OneToOne
    Achievement achievement;

    int progress;

    boolean completed;

    @ElementCollection
    List<IntTuple> interactedObjects;

    public AchievementStatistic(Player player, Course course, Achievement achievement) {
        this.player = player;
        this.course = course;
        this.achievement = achievement;
        this.progress = 0;
        this.completed = false;
        this.interactedObjects = new ArrayList<>();
    }

    /**
     * Sets the progress to the given value, if valid, and updates the completed flag accordingly
     * @param newProgress the new progress
     * @throws IllegalArgumentException if the new progress is smaller than the current one
     */
    public void setProgress(int newProgress) {
        if (newProgress < progress) {
            throw new IllegalArgumentException("The new progress cannot be smaller than the current one");
        }
        progress = newProgress;
        if (progress >= achievement.getAmountRequired()) {
            completed = true;
        }
    }
}
