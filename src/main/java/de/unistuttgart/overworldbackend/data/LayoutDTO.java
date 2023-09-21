package de.unistuttgart.overworldbackend.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Data Transfer Object for Layout.
 *
 * @see Layout
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LayoutDTO {

    @Nullable
    UUID id;

    int sizeX;
    int sizeY;
    int layers;
    String tiles;
}
