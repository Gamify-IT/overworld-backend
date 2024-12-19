package de.unistuttgart.overworldbackend.data.enums;

/**
 * This class contains descriptions of achievements.
 * Some achievements have the same description but different amounts required to complete them.
 * The placeholder "%d" will be replaced by the specified required amount.
 */
public enum AchievementDescription {
    WALK_TILES("Walk %d tiles"),
    CHANGE_SKIN("Change skin of your character"),
    COMPLETE_MINIGAMES("Successfully complete %d minigames"),
    PLAY_MINUTES("Play for %d minutes"),
    USE_SPRINT("Use sprint for %d seconds"),
    UNLOCK_DUNGEONS("Unlock 1 dungeon in World %d"),
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
    TOWERDEFENSE("Successfully complete \"towerdefense\""),
    USE_UFO("Use UFO %d times"),
    LOGIN("Login for %d days"),
    LEADEROARD_1("Take first place in the leaderboard"),
    LEADEROARD_2_3("Enter 2d or 3d place in the leaderboard"),
    GET_COINS("Get %d coins"),
    LEVEL_UP("Reach level %d");

    /**
     * Description of achievement.
     */
    private String description;

    AchievementDescription(String description) {
        this.description = description;
    }

    /**
     * Generates a formatted description by replacing the placeholder "%d" in the achievement
     * description with the specified required amount.
     *
     * @param requiredAmount the number to be inserted into the achievement description
     *                       where the placeholder is located.
     * @return a formatted achievement description with the required amount included.
     */
    public String getDescriptionWithRequiredAmount(int requiredAmount) {
        return String.format(description, requiredAmount);
    }

    public String getDescription() {
        return description;
    }
}
