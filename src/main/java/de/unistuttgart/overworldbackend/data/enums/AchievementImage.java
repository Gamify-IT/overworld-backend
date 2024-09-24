package de.unistuttgart.overworldbackend.data.enums;

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
    UFO_IMAGE("ufoImage"),
    CALENDER_IMAGE("calenderImage"),
    MEDAL_1_IMAGE("medal1Image"),
    MEDAL_3_IMAGE("medal3Image"),
    COIN_IMAGE("coinImage"),
    LEVEL_UP_IMAGE("levelUpImage");

    private String imageName;
    AchievementImage(String imageName) {
        this.imageName = imageName;
    }

    public String getImageName() {
        return imageName;
    }
}
