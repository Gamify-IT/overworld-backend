package de.unistuttgart.overworldbackend.data;

import javax.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * A MinigameSpot is an instance of an ObjectSpot
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MinigameSpot extends ObjectSpot {

    int index;

    public MinigameSpot(final AreaLocation area, final Position position, final int index) {
        super(area, position);
        this.index = index;
    }
}
