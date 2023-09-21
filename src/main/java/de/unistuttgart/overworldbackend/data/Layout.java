package de.unistuttgart.overworldbackend.data;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

/**
 * A Layout represents the size and structure of an area map
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Layout
{
    @Id
    @GeneratedValue(generator = "uuid")
    UUID id;

    int sizeX;
    int sizeY;
    int layers;
    String tiles;
}
