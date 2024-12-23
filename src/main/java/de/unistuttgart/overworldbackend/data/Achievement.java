package de.unistuttgart.overworldbackend.data;

import de.unistuttgart.overworldbackend.data.enums.AchievementCategory;
import de.unistuttgart.overworldbackend.data.enums.AchievementTitle;
import java.util.List;
import java.util.UUID;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Data
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

    @ElementCollection
    List<AchievementCategory> categories;

    public Achievement(
        AchievementTitle achievementTitle,
        String description,
        String imageName,
        int amountRequired,
        List<AchievementCategory> categories
    ) {
        this.achievementTitle = achievementTitle;
        this.description = description;
        this.imageName = imageName;
        this.amountRequired = amountRequired;
        this.categories = categories;
    }
}
