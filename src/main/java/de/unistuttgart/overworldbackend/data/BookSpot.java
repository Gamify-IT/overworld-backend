package de.unistuttgart.overworldbackend.data;

import javax.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * A BookSpot is an instance of an ObjectSpot
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookSpot extends ObjectSpot {

    int index;
    String name;

    public BookSpot(final AreaLocation area, final Position position, final int index, final String name) {
        super(area, position);
        this.index = index;
        this.name = name;
    }
}
