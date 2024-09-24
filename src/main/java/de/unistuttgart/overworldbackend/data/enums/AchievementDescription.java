package de.unistuttgart.overworldbackend.data.enums;

public enum AchievementDescription {
    WALK_TILES("Walk %d tiles"),
    CHANGE_SKIN("Change skin of your character"),
    COMPLETE_MINIGAMES("Successfully complete %d minigames"),
    PLAY_MINUTES("Play for %d minutes"),
    USE_SPRINT("Use sprint for %d seconds"),
    UNLOCK_DUNGEONS("Unlock all dungeons in World %d"),
    UNLOCK_WORLD("Unlock World %d"),
    FIND_MINIGAME_SPOTS("Locate %d minigame spots"),
    INTERACT_WITH_BOOKS("Interact with %d books"),
    INTERACT_WITH_BOOKS_IN_TOTAL("Interact with %d books in total"),
    INTERACT_WITH_ALL_BOOKS("Interact with all books"),
    OPEN_TELEPORTERS("Open %d teleporters"),
    OPEN_ALL_TELEPORTERS("Open all teleporters"),
    OPEN_TELEPORTERS_IN_TOTAL("Open %d teleporters in total"),
    TALK_TO_NPC("Talk to %d NPCs"),
    TALK_TO_NPC_IN_TOTAL("Talk to %d NPCs in total"),
    TALK_TO_ALL_NPC("Talk to all NPCs"),
    CHICKENSHOCK("Successfully complete \"chickenshock\""),
    MEMORY("Successfully complete \"memory\""),
    FINITEQUIZ("Successfully complete \"finitequiz\""),
    CROSSWORDPUZZLE("Successfully complete \"crosswordpuzzle\""),
    BUGFINDER("Successfully complete \"bugfinder\""),
    TOWERCRUSH("Successfully complete \"towercrush\""),
    USE_UFO("Use UFO %d times"),
    LOGIN("Login for %d days"),
    LEADEROARD_1("Take first place in the leaderboard"),
    LEADEROARD_2_3("Enter 2d or 3d place in the leaderboard"),
    GET_COINS("Get %d coins"),
    LEVEL_UP("Reach level %d"),

    ;

    private String description;

    AchievementDescription(String description) {
        this.description = description;
    }

    public String getDescriptionWithRequiredAmount(int requiredAmount) {
        return String.format(description, requiredAmount);
    }

    public String getDescription() {
        return description;
    }
}
