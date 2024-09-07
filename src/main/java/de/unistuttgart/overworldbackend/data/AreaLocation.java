package de.unistuttgart.overworldbackend.data;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Represents the location of an object spot
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AreaLocation {

    @Id
    @GeneratedValue(generator = "uuid")
    UUID id;

    int worldIndex;
    int dungeonIndex;
}
