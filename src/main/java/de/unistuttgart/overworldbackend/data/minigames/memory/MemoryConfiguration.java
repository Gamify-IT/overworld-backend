package de.unistuttgart.overworldbackend.data.minigames.memory;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.util.List;
import java.util.UUID;



@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemoryConfiguration {
  
    UUID id;

    List<MemoryCardPair> pairs;

}
