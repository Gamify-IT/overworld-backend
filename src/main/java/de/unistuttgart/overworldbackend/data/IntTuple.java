package de.unistuttgart.overworldbackend.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import javax.persistence.Embeddable;
import java.io.Serializable;
import javax.persistence.Embeddable;

/**
 * IntTuple is a helper class for storing the unique identifier of objects in the game (e.g. book, NPC, etc.).
 *
 * The unique identifier consists of 3 int values, namely:
 * worldId - indicates the number of the world where the object is located,
 * dungeonId - the number of the dungeon where the object is located,
 * numberId - the serial number of the object.
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IntTuple implements Serializable {
    /**
     * number of the world where the object is located
     */
    private int worldId;

    /**
     * number of the dungeon where the object is located
     */
    private int dungeonId;

    /**
     * serial number of the object
     */
    private int numberId;
}
