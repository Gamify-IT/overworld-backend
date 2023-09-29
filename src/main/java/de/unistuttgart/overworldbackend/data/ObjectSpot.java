package de.unistuttgart.overworldbackend.data;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.UUID;

/**
 * Represents a spot of an object in a generated area
 */
@Entity
@Inheritance
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ObjectSpot {
    @Id
    @GeneratedValue(generator = "uuid")
    UUID id;

    @ManyToOne
    Area area;

    @ManyToOne(cascade = CascadeType.ALL)
    Position position;

    protected ObjectSpot(
            final Area area,
            final Position position
    ) {
        this.area = area;
        this.position = position;
    }
}
