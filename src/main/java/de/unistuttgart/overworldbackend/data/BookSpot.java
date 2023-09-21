package de.unistuttgart.overworldbackend.data;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;

/**
 * A BookSpot is an instance of an ObjectSpot
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookSpot extends ObjectSpot{

    int index;

    public BookSpot(
            final Area area,
            final Position position,
            final int index
    ) {
        super(area, position);
        this.index = index;
    }
}
