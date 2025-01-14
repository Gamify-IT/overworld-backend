package de.unistuttgart.overworldbackend.data.enums;

/**
 * AchievementImage class contains all categories of images that are displayed in the achievement table.
 * Each element of AchievementImage contains a name and the corresponding image name stored in the frontend.
 */
public enum AchievementImage {
    FOOT_IMAGE("footImage"),
    NPC_IMAGE("npcImage"),
    STAR_IMAGE("starImage"),
    CLOCK_IMAGE("clockImage"),
    ROCKET_IMAGE("rocketImage"),
    DUNGEON_IMAGE("dungeonImage"),
    GLASS_IMAGE("glassImage"),
    MINIGAME_SPOT_IMAGE("minigameSpotImage"),
    BOOK_IMAGE("bookImage"),
    TELEPORTER_IMAGE("teleporterImage"),
    CHICKEN_IMAGE("chickenImage"),
    MEMORY_IMAGE("cardImage"),
    FINITEQUIZ_IMAGE("brainImage"),
    CROSSWORDPUZZLE_IMAGE("puzzleImage"),
    BUGFINDER_IMAGE("bugImage"),
    TOWERCRUSH_IMAGE("towerImage"),
    TOWERDEFENSE_IMAGE("towerdefenseImage"),
    UFO_IMAGE("ufoImage"),
    CALENDER_IMAGE("calenderImage"),
    MEDAL_1_IMAGE("medal1Image"),
    MEDAL_3_IMAGE("medal3Image"),
    COIN_IMAGE("coinImage"),
    LEVEL_UP_IMAGE("levelUpImage");

    /**
     * Name of the image that will be shown in the achievement table
     */
    private String imageName;
    AchievementImage(String imageName) {
        this.imageName = imageName;
    }

    public String getImageName() {
        return imageName;
    }
}
