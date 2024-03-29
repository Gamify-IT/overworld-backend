package de.unistuttgart.overworldbackend.data;

import de.unistuttgart.overworldbackend.data.enums.AchievementCategory;
import de.unistuttgart.overworldbackend.data.enums.AchievementTitle;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
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
}
