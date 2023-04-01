package de.unistuttgart.overworldbackend.data.minigames.memory;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemoryCard {
    
    public class Card {

        UUID id;
    
        String content;
    
        MemoryCardType type;
    }
}

