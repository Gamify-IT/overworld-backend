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
        final Achievement achievement1 = new Achievement(
            AchievementTitle.GO_FOR_A_WALK,
            "Walk 10 tiles",
            "imageName",
            10,
            Arrays.asList(AchievementCategory.EXPLORING, AchievementCategory.ACHIEVING)
        );
        final Achievement achievement2 = new Achievement(
            AchievementTitle.GO_FOR_A_LONGER_WALK,
            "Walk 1000 tiles",
            "imageName",
            1000,
            Arrays.asList(AchievementCategory.EXPLORING, AchievementCategory.ACHIEVING)
        );
        final Achievement achievement3 = new Achievement(
            AchievementTitle.LEVEL_UP,
            "Reach level 2",
            "imageName",
            2,
            Arrays.asList(AchievementCategory.ACHIEVING)
        );
        final Achievement achievement4 = new Achievement(
            AchievementTitle.SELECT_CHARACTER,
            "Change skin of your character",
            "imageName",
            1,
            Arrays.asList(AchievementCategory.EXPLORING)
        );
        final Achievement achievement5 = new Achievement(
            AchievementTitle.MINIGAME_ACHIEVER,
            "Successfully complete X minigames",
            "imageName",
            0,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE)
        );
        final Achievement achievement6 = new Achievement(
            AchievementTitle.GOAT,
            "Take first place in the leaderboard",
            "imageName",
            1,
            Arrays.asList(AchievementCategory.COMPETITIVE)
        );
        final Achievement achievement7 = new Achievement(
            AchievementTitle.ONE_OF_THE_BEST_PLAYERS,
            "Enter top 3 in the leaderboard",
            "imageName",
            1,
            Arrays.asList(AchievementCategory.COMPETITIVE)
        );
        final Achievement achievement8 = new Achievement(
            AchievementTitle.GAMER,
            "Login for 2 days",
            "imageName",
            2,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING, AchievementCategory.SOCIALIZING, AchievementCategory.COMPETITIVE)
        );
        final Achievement achievement9 = new Achievement(
            AchievementTitle.PROFESSIONAL_GAMER,
            "Login for 5 days",
            "imageName",
            5,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING, AchievementCategory.SOCIALIZING, AchievementCategory.COMPETITIVE)
        );
        final Achievement achievement10 = new Achievement(
            AchievementTitle.BEGINNER,
            "Play for 30 minutes",
            "imageName",
            30,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING, AchievementCategory.SOCIALIZING, AchievementCategory.COMPETITIVE)
        );
        final Achievement achievement11 = new Achievement(
            AchievementTitle.EXPERIENCED_PLAYER,
            "Play for 90 minutes",
            "imageName",
            90,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING, AchievementCategory.SOCIALIZING, AchievementCategory.COMPETITIVE)
        );
        final Achievement achievement12 = new Achievement(
            AchievementTitle.OBTAIN_XP,
            "?",
            "imageName",
            0,
            Arrays.asList(AchievementCategory.ACHIEVING)
        );
        final Achievement achievement13 = new Achievement(
            AchievementTitle.OBTAIN_MORE_XP,
            "?",
            "imageName",
            0,
            Arrays.asList(AchievementCategory.ACHIEVING)
        );
        final Achievement achievement14 = new Achievement(
            AchievementTitle.SPEEDRUNNER,
            "Use sprint for 30 seconds",
            "imageName",
            30,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE)
        );
        final Achievement achievement15 = new Achievement(
            AchievementTitle.MINER_WORLD_1,
            "Unlock all dungeons in World 1",
            "imageName",
            4,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement16 = new Achievement(
            AchievementTitle.MINER_WORLD_2,
            "Unlock all dungeons in World 2",
            "imageName",
            4,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement17 = new Achievement(
            AchievementTitle.MINER_WORLD_3,
            "Unlock all dungeons in World 3",
            "imageName",
            4,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement18 = new Achievement(
            AchievementTitle.MINER_WORLD_4,
            "Unlock all dungeons in World 4",
            "imageName",
            4,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement19 = new Achievement(
            AchievementTitle.OPENER_WORLD_2,
            "Unlock world 2",
            "imageName",
            1,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement20 = new Achievement(
            AchievementTitle.OPENER_WORLD_3,
            "Unlock world 3",
            "imageName",
            1,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement21 = new Achievement(
            AchievementTitle.OPENER_WORLD_4,
            "Unlock world 4",
            "imageName",
            1,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement22 = new Achievement(
            AchievementTitle.MINIGAME_SPOTS_FINDER,
            "Locate all minigame spots",
            "imageName",
            0,
            Arrays.asList(AchievementCategory.EXPLORING)
        );
        final Achievement achievement23 = new Achievement(
            AchievementTitle.READER_LEVEL_1_WORLD_1,
            "Interact with 5 books",
            "imageName",
            5,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement24 = new Achievement(
            AchievementTitle.READER_LEVEL_2_WORLD_1,
            "Interact with 10 books",
            "imageName",
            10,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement25 = new Achievement(
            AchievementTitle.READER_LEVEL_3_WORLD_1,
            "Interact with all books",
            "imageName",
            25,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement26 = new Achievement(
            AchievementTitle.READER_LEVEL_1_WORLD_2,
            "Interact with 5 books",
            "imageName",
            5,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement27 = new Achievement(
            AchievementTitle.READER_LEVEL_2_WORLD_2,
            "Interact with 10 books",
            "imageName",
            10,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement28 = new Achievement(
            AchievementTitle.READER_LEVEL_3_WORLD_2,
            "Interact with all books",
            "imageName",
            25,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement29 = new Achievement(
            AchievementTitle.READER_LEVEL_1_WORLD_3,
            "Interact with 5 books",
            "imageName",
            5,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement30 = new Achievement(
            AchievementTitle.READER_LEVEL_2_WORLD_3,
            "Interact with 10 books",
            "imageName",
            10,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement31 = new Achievement(
            AchievementTitle.READER_LEVEL_3_WORLD_3,
            "Interact with all books",
            "imageName",
            25,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement32 = new Achievement(
            AchievementTitle.READER_LEVEL_1_WORLD_4,
            "Interact with 5 books",
            "imageName",
            5,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement33 = new Achievement(
            AchievementTitle.READER_LEVEL_2_WORLD_4,
            "Interact with 10 books",
            "imageName",
            10,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement34 = new Achievement(
            AchievementTitle.READER_LEVEL_3_WORLD_4,
            "Interact with all books",
            "imageName",
            25,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement35 = new Achievement(
            AchievementTitle.READER_LEVEL_1,
            "Interact with 25 books in total",
            "imageName",
            25,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement36 = new Achievement(
            AchievementTitle.READER_LEVEL_2,
            "Interact with 50 books in total",
            "imageName",
            50,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement37 = new Achievement(
            AchievementTitle.READER_LEVEL_3,
            "Interact with all books in total",
            "imageName",
            100,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );

        final Achievement achievement38 = new Achievement(
            AchievementTitle.TELEPORTER_BEGINNER_WORLD_1,
            "Open 2 teleporters",
            "imageName",
            2,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement39 = new Achievement(
            AchievementTitle.TELEPORTER_PROFESSIONAL_WORLD_1,
            "Open 10 teleporters",
            "imageName",
            10,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement40 = new Achievement(
            AchievementTitle.TELEPORTER_MASTER_WORLD_1,
            "Open all teleporters",
            "imageName",
            19,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement41 = new Achievement(
            AchievementTitle.TELEPORTER_BEGINNER_WORLD_2,
            "Open 2 teleporters",
            "imageName",
            2,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement42 = new Achievement(
            AchievementTitle.TELEPORTER_PROFESSIONAL_WORLD_2,
            "Open 12 teleporters",
            "imageName",
            12,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement43 = new Achievement(
            AchievementTitle.TELEPORTER_MASTER_WORLD_2,
            "Open all teleporters",
            "imageName",
            24,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement44 = new Achievement(
            AchievementTitle.TELEPORTER_BEGINNER_WORLD_3,
            "Open 2 teleporters",
            "imageName",
            2,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement45 = new Achievement(
            AchievementTitle.TELEPORTER_PROFESSIONAL_WORLD_3,
            "Open 8 teleporters",
            "imageName",
            8,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement46 = new Achievement(
            AchievementTitle.TELEPORTER_MASTER_WORLD_3,
            "Open all teleporters",
            "imageName",
            16,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement47 = new Achievement(
            AchievementTitle.TELEPORTER_BEGINNER_WORLD_4,
            "Open 2 teleporters",
            "imageName",
            2,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement48 = new Achievement(
            AchievementTitle.TELEPORTER_PROFESSIONAL_WORLD_4,
            "Open 8 teleporters",
            "imageName",
            8,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement49 = new Achievement(
            AchievementTitle.TELEPORTER_MASTER_WORLD_4,
            "Open all teleporters",
            "imageName",
            16,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement50 = new Achievement(
            AchievementTitle.TELEPORTER_BEGINNER,
            "Open 20 teleporters in total",
            "imageName",
            20,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement51 = new Achievement(
            AchievementTitle.TELEPORTER_PROFESSIONAL,
            "Open 40 teleporters in total",
            "imageName",
            40,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement52 = new Achievement(
            AchievementTitle.TELEPORTER_MASTER,
            "Open all teleporters in total",
            "imageName",
            75,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        final Achievement achievement53 = new Achievement(
            AchievementTitle.COMMUNICATOR_LEVEL_1_WORLD_1,
            "Talk to 5 NPCs",
            "imageName",
            5,
            Arrays.asList(AchievementCategory.EXPLORING)
        );
        final Achievement achievement54 = new Achievement(
            AchievementTitle.COMMUNICATOR_LEVEL_2_WORLD_1,
            "Talk to 25 NPCs",
            "imageName",
            25,
            Arrays.asList(AchievementCategory.EXPLORING)
        );
        final Achievement achievement55 = new Achievement(
            AchievementTitle.COMMUNICATOR_LEVEL_3_WORLD_1,
            "Talk to all NPCs",
            "imageName",
            50,
            Arrays.asList(AchievementCategory.EXPLORING)
        );
        final Achievement achievement56 = new Achievement(
            AchievementTitle.COMMUNICATOR_LEVEL_1_WORLD_2,
            "Talk to 5 NPCs",
            "imageName",
            5,
            Arrays.asList(AchievementCategory.EXPLORING)
        );
        final Achievement achievement57 = new Achievement(
            AchievementTitle.COMMUNICATOR_LEVEL_2_WORLD_2,
            "Talk to 25 NPCs",
            "imageName",
            25,
            Arrays.asList(AchievementCategory.EXPLORING)
        );
        final Achievement achievement58 = new Achievement(
            AchievementTitle.COMMUNICATOR_LEVEL_3_WORLD_2,
            "Talk to all NPCs",
            "imageName",
            50,
            Arrays.asList(AchievementCategory.EXPLORING)
        );
        final Achievement achievement59 = new Achievement(
            AchievementTitle.COMMUNICATOR_LEVEL_1_WORLD_3,
            "Talk to 5 NPCs",
            "imageName",
            5,
            Arrays.asList(AchievementCategory.EXPLORING)
        );
        final Achievement achievement60 = new Achievement(
            AchievementTitle.COMMUNICATOR_LEVEL_2_WORLD_3,
            "Talk to 25 NPCs",
            "imageName",
            25,
            Arrays.asList(AchievementCategory.EXPLORING)
        );
        final Achievement achievement61 = new Achievement(
            AchievementTitle.COMMUNICATOR_LEVEL_3_WORLD_3,
            "Talk to all NPCs",
            "imageName",
            50,
            Arrays.asList(AchievementCategory.EXPLORING)
        );
        final Achievement achievement62 = new Achievement(
            AchievementTitle.COMMUNICATOR_LEVEL_1_WORLD_4,
            "Talk to 5 NPCs",
            "imageName",
            5,
            Arrays.asList(AchievementCategory.EXPLORING)
        );
        final Achievement achievement63 = new Achievement(
            AchievementTitle.COMMUNICATOR_LEVEL_2_WORLD_4,
            "Talk to 25 NPCs",
            "imageName",
            25,
            Arrays.asList(AchievementCategory.EXPLORING)
        );
        final Achievement achievement64 = new Achievement(
            AchievementTitle.COMMUNICATOR_LEVEL_3_WORLD_4,
            "Talk to all NPCs",
            "imageName",
            50,
            Arrays.asList(AchievementCategory.EXPLORING)
        );
        final Achievement achievement65 = new Achievement(
            AchievementTitle.COMMUNICATOR_LEVEL_1,
            "Talk to 50 NPCs in total",
            "imageName",
            50,
            Arrays.asList(AchievementCategory.EXPLORING)
        );
        final Achievement achievement66 = new Achievement(
            AchievementTitle.COMMUNICATOR_LEVEL_2,
            "Talk to 100 NPCs in total",
            "imageName",
            100,
            Arrays.asList(AchievementCategory.EXPLORING)
        );
        final Achievement achievement67 = new Achievement(
            AchievementTitle.COMMUNICATOR_LEVEL_3,
            "Talk to all NPCs in total",
            "imageName",
            200,
            Arrays.asList(AchievementCategory.EXPLORING)
        );
        final Achievement achievement68 = new Achievement(
            AchievementTitle.CHICKENSHOCK_MASTER,
            "Successfully complete \"chickenshock\"",
            "imageName",
            1,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE)
        );
        final Achievement achievement69 = new Achievement(
            AchievementTitle.MEMORY_MASTER,
            "Successfully complete \"memory\"",
            "imageName",
            1,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE)
        );
        final Achievement achievement70 = new Achievement(
            AchievementTitle.FINITEQUIZ_MASTER,
            "Successfully complete \"finitequiz\"",
            "imageName",
            1,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE)
        );
        final Achievement achievement71 = new Achievement(
            AchievementTitle.CROSSWORDPUZZLE_MASTER,
            "Successfully complete \"crosswordpuzzle\"",
            "imageName",
            1,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE)
        );
        final Achievement achievement72 = new Achievement(
            AchievementTitle.BUGFINDER_MASTER,
            "Successfully complete \"bugfinder\"",
            "imageName",
            1,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE)
        );
        final Achievement achievement73 = new Achievement(
            AchievementTitle.TOWERCRUSH_MASTER,
            "Successfully complete \"towercrush\"",
            "imageName",
            1,
            Arrays.asList(AchievementCategory.SOCIALIZING, AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE)
        );
        final Achievement achievement74 = new Achievement(
            AchievementTitle.TRAVELER,
            "Use UFO 3 times",
            "imageName",
            3,
            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
        );
        

        achievementRepository.save(achievement1);
        achievementRepository.save(achievement2);
        achievementRepository.save(achievement3);
        achievementRepository.save(achievement4);
        achievementRepository.save(achievement5);
        achievementRepository.save(achievement6);
        achievementRepository.save(achievement7);
        achievementRepository.save(achievement8);
        achievementRepository.save(achievement9);
        achievementRepository.save(achievement10);
        achievementRepository.save(achievement11);
        achievementRepository.save(achievement12);
        achievementRepository.save(achievement13);
        achievementRepository.save(achievement14);
        achievementRepository.save(achievement15);
        achievementRepository.save(achievement16);
        achievementRepository.save(achievement17);
        achievementRepository.save(achievement18);
        achievementRepository.save(achievement19);
        achievementRepository.save(achievement20);
        achievementRepository.save(achievement21);
        achievementRepository.save(achievement22);
        achievementRepository.save(achievement23);
        achievementRepository.save(achievement24);
        achievementRepository.save(achievement25);
        achievementRepository.save(achievement26);
        achievementRepository.save(achievement27);
        achievementRepository.save(achievement28);
        achievementRepository.save(achievement29);
        achievementRepository.save(achievement30);
        achievementRepository.save(achievement31);
        achievementRepository.save(achievement32);
        achievementRepository.save(achievement33);
        achievementRepository.save(achievement34);
        achievementRepository.save(achievement35);
        achievementRepository.save(achievement36);
        achievementRepository.save(achievement37);
        achievementRepository.save(achievement38);
        achievementRepository.save(achievement39);
        achievementRepository.save(achievement40);
        achievementRepository.save(achievement41);
        achievementRepository.save(achievement42);
        achievementRepository.save(achievement43);
        achievementRepository.save(achievement44);
        achievementRepository.save(achievement45);
        achievementRepository.save(achievement46);
        achievementRepository.save(achievement47);
        achievementRepository.save(achievement48);
        achievementRepository.save(achievement49);
        achievementRepository.save(achievement50);
        achievementRepository.save(achievement51);
        achievementRepository.save(achievement52);
        achievementRepository.save(achievement53);
        achievementRepository.save(achievement54);
        achievementRepository.save(achievement55);
        achievementRepository.save(achievement56);
        achievementRepository.save(achievement57);
        achievementRepository.save(achievement58);
        achievementRepository.save(achievement59);
        achievementRepository.save(achievement60);
        achievementRepository.save(achievement61);
        achievementRepository.save(achievement62);
        achievementRepository.save(achievement63);
        achievementRepository.save(achievement64);
        achievementRepository.save(achievement65);
        achievementRepository.save(achievement66);
        achievementRepository.save(achievement67);
        achievementRepository.save(achievement68);
        achievementRepository.save(achievement69);
        achievementRepository.save(achievement70);
        achievementRepository.save(achievement71);
        achievementRepository.save(achievement72);
        achievementRepository.save(achievement73);
        achievementRepository.save(achievement74);

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
