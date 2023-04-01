package de.unistuttgart.overworldbackend.data.minigames.memory;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;


@FieldDefaults(level = AccessLevel.PRIVATE)
public enum MemoryCardType {
    IMAGE,
    TEXT,
    MARKDOWN,
}
