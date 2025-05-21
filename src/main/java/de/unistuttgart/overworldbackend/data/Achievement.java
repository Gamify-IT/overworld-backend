package de.unistuttgart.overworldbackend.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import de.unistuttgart.overworldbackend.data.enums.AchievementCategory;
import de.unistuttgart.overworldbackend.data.enums.AchievementTitle;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Achievement {

    @Id
    @GeneratedValue(generator = "uuid")
    UUID id;

    AchievementTitle achievementTitle;

    String description;

    /**
     * The name of the image which should be shown
     */
    String imageName;

    /**
     * The amount of events until the achievement is completed
     */
    int amountRequired;

    @JsonBackReference(value = "achievement-course")
    @ManyToOne
    Course course;

    @ElementCollection
    List<AchievementCategory> categories = new ArrayList<>();

    public Achievement(
        AchievementTitle achievementTitle,
        String description,
        String imageName,
        int amountRequired,
        List<AchievementCategory> categories,
        Course course
    ) {
        this.achievementTitle = achievementTitle;
        this.description = description;
        this.imageName = imageName;
        this.amountRequired = amountRequired;
        this.categories = categories;
        this.course = course;
    }
}
