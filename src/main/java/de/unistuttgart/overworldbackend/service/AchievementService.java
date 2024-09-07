package de.unistuttgart.overworldbackend.service;

import de.unistuttgart.overworldbackend.data.Achievement;
import de.unistuttgart.overworldbackend.data.AchievementStatistic;
import de.unistuttgart.overworldbackend.data.Player;
import de.unistuttgart.overworldbackend.data.enums.AchievementCategory;
import de.unistuttgart.overworldbackend.data.enums.AchievementTitle;
import de.unistuttgart.overworldbackend.repositories.AchievementRepository;
import de.unistuttgart.overworldbackend.repositories.PlayerRepository;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class AchievementService {

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private PlayerRepository playerRepository;

    /**
     * Checks for all players the current achievements adds new created achievements to the player and removes none existing achievements.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void updatePlayerStatisticAchievements() {
        List<Achievement> currentAchievementList = Arrays.asList(
            new Achievement(
                AchievementTitle.GO_FOR_A_WALK,
                "Walk 10 tiles",
                "footImage",
                10,
                Arrays.asList(AchievementCategory.EXPLORING, AchievementCategory.ACHIEVING)
            ),
            new Achievement(
                AchievementTitle.GO_FOR_A_LONGER_WALK,
                "Walk 1000 tiles",
                "footImage",
                1000,
                Arrays.asList(AchievementCategory.EXPLORING, AchievementCategory.ACHIEVING)
            ),
            new Achievement(
                AchievementTitle.SELECT_CHARACTER,
                "Change skin of your character",
                "npcImage",
                1,
                Arrays.asList(AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.MINIGAME_ACHIEVER,
                "Successfully complete 2 minigames",
                "starImage",
                2,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE)
            ),
            new Achievement(
                AchievementTitle.MINIGAME_PROFESSIONAL,
                "Successfully complete 5 minigames",
                "starImage",
                5,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE)
            ),
            new Achievement(
                AchievementTitle.BEGINNER,
                "Play for 30 minutes",
                "clockImage",
                30,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING, AchievementCategory.SOCIALIZING, AchievementCategory.COMPETITIVE)
            ),
            new Achievement(
                AchievementTitle.EXPERIENCED_PLAYER,
                "Play for 90 minutes",
                "clockImage",
                90,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING, AchievementCategory.SOCIALIZING, AchievementCategory.COMPETITIVE)
            ),
            new Achievement(
                AchievementTitle.SPEEDRUNNER,
                "Use sprint for 30 seconds",
                "rocketImage",
                30,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE)
            ),
            new Achievement(
                AchievementTitle.MINER_WORLD_1,
                "Unlock all dungeons in World 1",
                "dungeonImage",
                4,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.MINER_WORLD_2,
                "Unlock all dungeons in World 2",
                "dungeonImage",
                4,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.MINER_WORLD_3,
                "Unlock all dungeons in World 3",
                "dungeonImage",
                4,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.MINER_WORLD_4,
                "Unlock all dungeons in World 4",
                "dungeonImage",
                4,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.OPENER_WORLD_2,
                "Unlock world 2",
                "glassImage",
                1,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.OPENER_WORLD_3,
                "Unlock world 3",
                "glassImage",
                1,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.OPENER_WORLD_4,
                "Unlock world 4",
                "glassImage",
                1,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.MINIGAME_SPOTS_FINDER,
                "Locate 2 minigame spots",
                "minigameSpotImage",
                2,
                Arrays.asList(AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.MINIGAME_SPOTS_MASTER,
                "Locate 5 minigame spots",
                "minigameSpotImage",
                5,
                Arrays.asList(AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.READER_LEVEL_1_WORLD_1,
                "Interact with 5 books",
                "bookImage",
                5,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.READER_LEVEL_2_WORLD_1,
                "Interact with 10 books",
                "bookImage",
                10,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.READER_LEVEL_3_WORLD_1,
                "Interact with all books",
                "bookImage",
                25,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.READER_LEVEL_1_WORLD_2,
                "Interact with 5 books",
                "bookImage",
                5,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.READER_LEVEL_2_WORLD_2,
                "Interact with 10 books",
                "bookImage",
                10,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.READER_LEVEL_3_WORLD_2,
                "Interact with all books",
                "bookImage",
                25,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.READER_LEVEL_1_WORLD_3,
                "Interact with 5 books",
                "bookImage",
                5,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.READER_LEVEL_2_WORLD_3,
                "Interact with 10 books",
                "bookImage",
                10,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.READER_LEVEL_3_WORLD_3,
                "Interact with all books",
                "bookImage",
                25,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.READER_LEVEL_1_WORLD_4,
                "Interact with 5 books",
                "bookImage",
                5,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.READER_LEVEL_2_WORLD_4,
                "Interact with 10 books",
                "bookImage",
                10,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.READER_LEVEL_3_WORLD_4,
                "Interact with all books",
                "bookImage",
                25,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.READER_LEVEL_1,
                "Interact with 25 books in total",
                "bookImage",
                25,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.READER_LEVEL_2,
                "Interact with 50 books in total",
                "bookImage",
                50,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.READER_LEVEL_3,
                "Interact with all books in total",
                "bookImage",
                100,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.TELEPORTER_BEGINNER_WORLD_1,
                "Open 2 teleporters",
                "teleporterImage",
                2,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.TELEPORTER_PROFESSIONAL_WORLD_1,
                "Open 10 teleporters",
                "teleporterImage",
                10,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.TELEPORTER_MASTER_WORLD_1,
                "Open all teleporters",
                "teleporterImage",
                19,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.TELEPORTER_BEGINNER_WORLD_2,
                "Open 2 teleporters",
                "teleporterImage",
                2,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.TELEPORTER_PROFESSIONAL_WORLD_2,
                "Open 12 teleporters",
                "teleporterImage",
                12,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.TELEPORTER_MASTER_WORLD_2,
                "Open all teleporters",
                "teleporterImage",
                24,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.TELEPORTER_BEGINNER_WORLD_3,
                "Open 2 teleporters",
                "teleporterImage",
                2,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.TELEPORTER_PROFESSIONAL_WORLD_3,
                "Open 8 teleporters",
                "teleporterImage",
                8,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.TELEPORTER_MASTER_WORLD_3,
                "Open all teleporters",
                "teleporterImage",
                16,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.TELEPORTER_BEGINNER_WORLD_4,
                "Open 2 teleporters",
                "teleporterImage",
                2,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.TELEPORTER_PROFESSIONAL_WORLD_4,
                "Open 8 teleporters",
                "teleporterImage",
                8,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.TELEPORTER_MASTER_WORLD_4,
                "Open all teleporters",
                "teleporterImage",
                16,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.TELEPORTER_BEGINNER,
                "Open 20 teleporters in total",
                "teleporterImage",
                20,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.TELEPORTER_PROFESSIONAL,
                "Open 40 teleporters in total",
                "teleporterImage",
                40,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.TELEPORTER_MASTER,
                "Open all teleporters in total",
                "teleporterImage",
                75,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.COMMUNICATOR_LEVEL_1_WORLD_1,
                "Talk to 5 NPCs",
                "npcImage",
                5,
                Arrays.asList(AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.COMMUNICATOR_LEVEL_2_WORLD_1,
                "Talk to 25 NPCs",
                "npcImage",
                25,
                Arrays.asList(AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.COMMUNICATOR_LEVEL_3_WORLD_1,
                "Talk to all NPCs",
                "npcImage",
                50,
                Arrays.asList(AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.COMMUNICATOR_LEVEL_1_WORLD_2,
                "Talk to 5 NPCs",
                "npcImage",
                5,
                Arrays.asList(AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.COMMUNICATOR_LEVEL_2_WORLD_2,
                "Talk to 25 NPCs",
                "npcImage",
                25,
                Arrays.asList(AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.COMMUNICATOR_LEVEL_3_WORLD_2,
                "Talk to all NPCs",
                "npcImage",
                50,
                Arrays.asList(AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.COMMUNICATOR_LEVEL_1_WORLD_3,
                "Talk to 5 NPCs",
                "npcImage",
                5,
                Arrays.asList(AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.COMMUNICATOR_LEVEL_2_WORLD_3,
                "Talk to 25 NPCs",
                "npcImage",
                25,
                Arrays.asList(AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.COMMUNICATOR_LEVEL_3_WORLD_3,
                "Talk to all NPCs",
                "npcImage",
                50,
                Arrays.asList(AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.COMMUNICATOR_LEVEL_1_WORLD_4,
                "Talk to 5 NPCs",
                "npcImage",
                5,
                Arrays.asList(AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.COMMUNICATOR_LEVEL_2_WORLD_4,
                "Talk to 25 NPCs",
                "npcImage",
                25,
                Arrays.asList(AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.COMMUNICATOR_LEVEL_3_WORLD_4,
                "Talk to all NPCs",
                "npcImage",
                50,
                Arrays.asList(AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.COMMUNICATOR_LEVEL_1,
                "Talk to 50 NPCs in total",
                "npcImage",
                50,
                Arrays.asList(AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.COMMUNICATOR_LEVEL_2,
                "Talk to 100 NPCs in total",
                "npcImage",
                100,
                Arrays.asList(AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.COMMUNICATOR_LEVEL_3,
                "Talk to all NPCs in total",
                "npcImage",
                200,
                Arrays.asList(AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.CHICKENSHOCK_MASTER,
                "Successfully complete \"chickenshock\"",
                "chickenImage",
                1,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE)
            ),
            new Achievement(
                AchievementTitle.MEMORY_MASTER,
                "Successfully complete \"memory\"",
                "cardImage",
                1,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE)
            ),
            new Achievement(
                AchievementTitle.FINITEQUIZ_MASTER,
                "Successfully complete \"finitequiz\"",
                "brainImage",
                1,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE)
            ),
            new Achievement(
                AchievementTitle.CROSSWORDPUZZLE_MASTER,
                "Successfully complete \"crosswordpuzzle\"",
                "puzzleImage",
                1,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE)
            ),
            new Achievement(
                AchievementTitle.BUGFINDER_MASTER,
                "Successfully complete \"bugfinder\"",
                "bugImage",
                1,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE)
            ),
            new Achievement(
                AchievementTitle.TOWERCRUSH_MASTER,
                "Successfully complete \"towercrush\"",
                "towerImage",
                1,
                Arrays.asList(AchievementCategory.SOCIALIZING, AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE)
            ),
            new Achievement(
                AchievementTitle.TRAVELER,
                "Use UFO 3 times",
                "ufoImage",
                3,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
            ),
            new Achievement(
                AchievementTitle.GAMER,
                "Login for 2 days",
                "calenderImage",
                2,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING, AchievementCategory.SOCIALIZING, AchievementCategory.COMPETITIVE)
            ),
            new Achievement(
                AchievementTitle.PROFESSIONAL_GAMER,
                "Login for 5 days",
                "calenderImage",
                5,
                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING, AchievementCategory.SOCIALIZING, AchievementCategory.COMPETITIVE)
            ),
            new Achievement(
                AchievementTitle.GOAT,
                "Take first place in the leaderboard",
                "medal1Image",
                1,
                Arrays.asList(AchievementCategory.COMPETITIVE)
            ),
            new Achievement(
                AchievementTitle.ONE_OF_THE_BEST_PLAYERS,
                "Enter 2d or 3d place in the leaderboard",
                "medal3Image",
                1,
                Arrays.asList(AchievementCategory.COMPETITIVE)
            ),
            new Achievement(
                AchievementTitle.GET_COINS,
                "Get 50 coins",
                "coinImage",
                50,
                Arrays.asList(AchievementCategory.ACHIEVING)
            ),
            new Achievement(
                AchievementTitle.GET_MORE_COINS,
                "Get 150 coins",
                "coinImage",
                150,
                Arrays.asList(AchievementCategory.ACHIEVING)
            ),
            new Achievement(
                AchievementTitle.LEVEL_UP,
                "Reach level 2",
                "levelUpImage",
                2,
                Arrays.asList(AchievementCategory.ACHIEVING)
            )
        );

        currentAchievementList.forEach(
            achievement -> {
                achievementRepository.save(achievement);
            }
        );

        final List<Achievement> achievements = achievementRepository.findAll();

        for (final Player player : playerRepository.findAll()) {
            // add statistic for achievement if not exists
            for (final Achievement achievement : achievements) {
                if (
                    player
                        .getAchievementStatistics()
                        .stream()
                        .noneMatch(achievementStatistic ->
                            achievementStatistic
                                .getAchievement()
                                .getAchievementTitle()
                                .equals(achievement.getAchievementTitle())
                        )
                ) {
                    player.getAchievementStatistics().add(new AchievementStatistic(player, achievement));
                }
            }
            // remove statistic for achievement if not exists
            player
                .getAchievementStatistics()
                .removeIf(achievementStatistic ->
                    achievements
                        .stream()
                        .noneMatch(achievement ->
                            achievement
                                .getAchievementTitle()
                                .equals(achievementStatistic.getAchievement().getAchievementTitle())
                        )
                );
            playerRepository.save(player);
        }
    }
}