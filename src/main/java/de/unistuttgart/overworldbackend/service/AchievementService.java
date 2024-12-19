package de.unistuttgart.overworldbackend.service;

import de.unistuttgart.overworldbackend.data.Achievement;
import de.unistuttgart.overworldbackend.data.AchievementStatistic;
import de.unistuttgart.overworldbackend.data.Course;
import de.unistuttgart.overworldbackend.data.Player;
import de.unistuttgart.overworldbackend.data.enums.AchievementCategory;
import de.unistuttgart.overworldbackend.data.enums.AchievementDescription;
import de.unistuttgart.overworldbackend.data.enums.AchievementImage;
import de.unistuttgart.overworldbackend.data.enums.AchievementTitle;
import de.unistuttgart.overworldbackend.repositories.AchievementRepository;
import de.unistuttgart.overworldbackend.repositories.CourseRepository;
import de.unistuttgart.overworldbackend.repositories.PlayerRepository;

import java.util.*;

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

    @Autowired
    private CourseRepository courseRepository;

    /**
     * Checks for all players the current achievements adds new created achievements to the player and removes none existing achievements.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void updatePlayerStatisticAchievements() {
        List<Achievement> currentAchievementList = Arrays.asList(
                new Achievement(
                        AchievementTitle.GO_FOR_A_WALK,
                        AchievementDescription.WALK_TILES.getDescriptionWithRequiredAmount(10),
                        AchievementImage.FOOT_IMAGE.getImageName(),
                        10,
                        Arrays.asList(AchievementCategory.EXPLORING, AchievementCategory.ACHIEVING)
                ),
                new Achievement(
                        AchievementTitle.GO_FOR_A_LONGER_WALK,
                        AchievementDescription.WALK_TILES.getDescriptionWithRequiredAmount(1000),
                        AchievementImage.FOOT_IMAGE.getImageName(),
                        1000,
                        Arrays.asList(AchievementCategory.EXPLORING, AchievementCategory.ACHIEVING)
                ),
                new Achievement(
                        AchievementTitle.SELECT_CHARACTER,
                        AchievementDescription.CHANGE_SKIN.getDescription(),
                        AchievementImage.NPC_IMAGE.getImageName(),
                        1,
                        List.of(AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.MINIGAME_ACHIEVER,
                        AchievementDescription.COMPLETE_MINIGAMES.getDescriptionWithRequiredAmount(2),
                        AchievementImage.STAR_IMAGE.getImageName(),
                        2,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE)
                ),
                new Achievement(
                        AchievementTitle.MINIGAME_PROFESSIONAL,
                        AchievementDescription.COMPLETE_MINIGAMES.getDescriptionWithRequiredAmount(5),
                        AchievementImage.STAR_IMAGE.getImageName(),
                        5,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE)
                ),
                new Achievement(
                        AchievementTitle.BEGINNER,
                        AchievementDescription.PLAY_MINUTES.getDescriptionWithRequiredAmount(30),
                        AchievementImage.CLOCK_IMAGE.getImageName(),
                        30,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING, AchievementCategory.SOCIALIZING, AchievementCategory.COMPETITIVE)
                ),
                new Achievement(
                        AchievementTitle.EXPERIENCED_PLAYER,
                        AchievementDescription.PLAY_MINUTES.getDescriptionWithRequiredAmount(90),
                        AchievementImage.CLOCK_IMAGE.getImageName(),
                        90,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING, AchievementCategory.SOCIALIZING, AchievementCategory.COMPETITIVE)
                ),
                new Achievement(
                        AchievementTitle.SPEEDRUNNER,
                        AchievementDescription.USE_SPRINT.getDescriptionWithRequiredAmount(30),
                        AchievementImage.ROCKET_IMAGE.getImageName(),
                        30,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE)
                ),
                new Achievement(
                        AchievementTitle.MINER_WORLD_1,
                        AchievementDescription.UNLOCK_DUNGEONS.getDescriptionWithRequiredAmount(1),
                        AchievementImage.DUNGEON_IMAGE.getImageName(),
                        1,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.MINER_WORLD_2,
                        AchievementDescription.UNLOCK_DUNGEONS.getDescriptionWithRequiredAmount(2),
                        AchievementImage.DUNGEON_IMAGE.getImageName(),
                        1,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.MINER_WORLD_3,
                        AchievementDescription.UNLOCK_DUNGEONS.getDescriptionWithRequiredAmount(3),
                        AchievementImage.DUNGEON_IMAGE.getImageName(),
                        1,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.MINER_WORLD_4,
                        AchievementDescription.UNLOCK_DUNGEONS.getDescriptionWithRequiredAmount(4),
                        AchievementImage.DUNGEON_IMAGE.getImageName(),
                        1,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.OPENER_WORLD_2,
                        AchievementDescription.UNLOCK_WORLD.getDescriptionWithRequiredAmount(2),
                        AchievementImage.GLASS_IMAGE.getImageName(),
                        1,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.OPENER_WORLD_3,
                        AchievementDescription.UNLOCK_WORLD.getDescriptionWithRequiredAmount(3),
                        AchievementImage.GLASS_IMAGE.getImageName(),
                        1,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.OPENER_WORLD_4,
                        AchievementDescription.UNLOCK_WORLD.getDescriptionWithRequiredAmount(4),
                        AchievementImage.GLASS_IMAGE.getImageName(),
                        1,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.MINIGAME_SPOTS_FINDER,
                        AchievementDescription.FIND_MINIGAME_SPOTS.getDescriptionWithRequiredAmount(2),
                        AchievementImage.MINIGAME_SPOT_IMAGE.getImageName(),
                        2,
                        List.of(AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.MINIGAME_SPOTS_MASTER,
                        AchievementDescription.FIND_MINIGAME_SPOTS.getDescriptionWithRequiredAmount(5),
                        AchievementImage.MINIGAME_SPOT_IMAGE.getImageName(),
                        5,
                        List.of(AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.READER_LEVEL_1_WORLD_1,
                        AchievementDescription.INTERACT_WITH_BOOKS.getDescriptionWithRequiredAmount(5),
                        AchievementImage.BOOK_IMAGE.getImageName(),
                        5,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.READER_LEVEL_2_WORLD_1,
                        AchievementDescription.INTERACT_WITH_BOOKS.getDescriptionWithRequiredAmount(10),
                        AchievementImage.BOOK_IMAGE.getImageName(),
                        10,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.READER_LEVEL_3_WORLD_1,
                        AchievementDescription.INTERACT_WITH_ALL_BOOKS.getDescription(),
                        AchievementImage.BOOK_IMAGE.getImageName(),
                        25,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.READER_LEVEL_1_WORLD_2,
                        AchievementDescription.INTERACT_WITH_BOOKS.getDescriptionWithRequiredAmount(5),
                        AchievementImage.BOOK_IMAGE.getImageName(),
                        5,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.READER_LEVEL_2_WORLD_2,
                        AchievementDescription.INTERACT_WITH_BOOKS.getDescriptionWithRequiredAmount(10),
                        AchievementImage.BOOK_IMAGE.getImageName(),
                        10,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.READER_LEVEL_3_WORLD_2,
                        AchievementDescription.INTERACT_WITH_ALL_BOOKS.getDescription(),
                        AchievementImage.BOOK_IMAGE.getImageName(),
                        25,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.READER_LEVEL_1_WORLD_3,
                        AchievementDescription.INTERACT_WITH_BOOKS.getDescriptionWithRequiredAmount(5),
                        AchievementImage.BOOK_IMAGE.getImageName(),
                        5,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.READER_LEVEL_2_WORLD_3,
                        AchievementDescription.INTERACT_WITH_BOOKS.getDescriptionWithRequiredAmount(10),
                        AchievementImage.BOOK_IMAGE.getImageName(),
                        10,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.READER_LEVEL_3_WORLD_3,
                        AchievementDescription.INTERACT_WITH_ALL_BOOKS.getDescription(),
                        AchievementImage.BOOK_IMAGE.getImageName(),
                        25,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.READER_LEVEL_1_WORLD_4,
                        AchievementDescription.INTERACT_WITH_BOOKS.getDescriptionWithRequiredAmount(5),
                        AchievementImage.BOOK_IMAGE.getImageName(),
                        5,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.READER_LEVEL_2_WORLD_4,
                        AchievementDescription.INTERACT_WITH_BOOKS.getDescriptionWithRequiredAmount(10),
                        AchievementImage.BOOK_IMAGE.getImageName(),
                        10,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.READER_LEVEL_3_WORLD_4,
                        AchievementDescription.INTERACT_WITH_ALL_BOOKS.getDescription(),
                        AchievementImage.BOOK_IMAGE.getImageName(),
                        25,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.READER_LEVEL_1,
                        AchievementDescription.INTERACT_WITH_BOOKS_IN_TOTAL.getDescriptionWithRequiredAmount(25),
                        AchievementImage.BOOK_IMAGE.getImageName(),
                        25,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.READER_LEVEL_2,
                        AchievementDescription.INTERACT_WITH_BOOKS_IN_TOTAL.getDescriptionWithRequiredAmount(50),
                        AchievementImage.BOOK_IMAGE.getImageName(),
                        50,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.READER_LEVEL_3,
                        AchievementDescription.INTERACT_WITH_ALL_BOOKS.getDescription(),
                        AchievementImage.BOOK_IMAGE.getImageName(),
                        100,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.TELEPORTER_BEGINNER_WORLD_1,
                        AchievementDescription.OPEN_TELEPORTERS.getDescriptionWithRequiredAmount(2),
                        AchievementImage.TELEPORTER_IMAGE.getImageName(),
                        2,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.TELEPORTER_PROFESSIONAL_WORLD_1,
                        AchievementDescription.OPEN_TELEPORTERS.getDescriptionWithRequiredAmount(10),
                        AchievementImage.TELEPORTER_IMAGE.getImageName(),
                        10,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.TELEPORTER_MASTER_WORLD_1,
                        AchievementDescription.OPEN_ALL_TELEPORTERS.getDescription(),
                        AchievementImage.TELEPORTER_IMAGE.getImageName(),
                        19,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.TELEPORTER_BEGINNER_WORLD_2,
                        AchievementDescription.OPEN_TELEPORTERS.getDescriptionWithRequiredAmount(2),
                        AchievementImage.TELEPORTER_IMAGE.getImageName(),
                        2,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.TELEPORTER_PROFESSIONAL_WORLD_2,
                        AchievementDescription.OPEN_TELEPORTERS.getDescriptionWithRequiredAmount(12),
                        AchievementImage.TELEPORTER_IMAGE.getImageName(),
                        12,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.TELEPORTER_MASTER_WORLD_2,
                        AchievementDescription.OPEN_ALL_TELEPORTERS.getDescription(),
                        AchievementImage.TELEPORTER_IMAGE.getImageName(),
                        24,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.TELEPORTER_BEGINNER_WORLD_3,
                        AchievementDescription.OPEN_TELEPORTERS.getDescriptionWithRequiredAmount(2),
                        AchievementImage.TELEPORTER_IMAGE.getImageName(),
                        2,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.TELEPORTER_PROFESSIONAL_WORLD_3,
                        AchievementDescription.OPEN_TELEPORTERS.getDescriptionWithRequiredAmount(8),
                        AchievementImage.TELEPORTER_IMAGE.getImageName(),
                        8,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.TELEPORTER_MASTER_WORLD_3,
                        AchievementDescription.OPEN_ALL_TELEPORTERS.getDescription(),
                        AchievementImage.TELEPORTER_IMAGE.getImageName(),
                        16,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.TELEPORTER_BEGINNER_WORLD_4,
                        AchievementDescription.OPEN_TELEPORTERS.getDescriptionWithRequiredAmount(2),
                        AchievementImage.TELEPORTER_IMAGE.getImageName(),
                        2,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.TELEPORTER_PROFESSIONAL_WORLD_4,
                        AchievementDescription.OPEN_TELEPORTERS.getDescriptionWithRequiredAmount(8),
                        AchievementImage.TELEPORTER_IMAGE.getImageName(),
                        8,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.TELEPORTER_MASTER_WORLD_4,
                        AchievementDescription.OPEN_ALL_TELEPORTERS.getDescription(),
                        AchievementImage.TELEPORTER_IMAGE.getImageName(),
                        16,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.TELEPORTER_BEGINNER,
                        AchievementDescription.OPEN_TELEPORTERS_IN_TOTAL.getDescriptionWithRequiredAmount(20),
                        AchievementImage.TELEPORTER_IMAGE.getImageName(),
                        20,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.TELEPORTER_PROFESSIONAL,
                        AchievementDescription.OPEN_TELEPORTERS_IN_TOTAL.getDescriptionWithRequiredAmount(40),
                        AchievementImage.TELEPORTER_IMAGE.getImageName(),
                        40,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.TELEPORTER_MASTER,
                        AchievementDescription.OPEN_ALL_TELEPORTERS.getDescription(),
                        AchievementImage.TELEPORTER_IMAGE.getImageName(),
                        75,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.COMMUNICATOR_LEVEL_1_WORLD_1,
                        AchievementDescription.TALK_TO_NPC.getDescriptionWithRequiredAmount(5),
                        AchievementImage.NPC_IMAGE.getImageName(),
                        5,
                        List.of(AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.COMMUNICATOR_LEVEL_2_WORLD_1,
                        AchievementDescription.TALK_TO_NPC.getDescriptionWithRequiredAmount(25),
                        AchievementImage.NPC_IMAGE.getImageName(),
                        25,
                        List.of(AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.COMMUNICATOR_LEVEL_3_WORLD_1,
                        AchievementDescription.TALK_TO_ALL_NPC.getDescription(),
                        AchievementImage.NPC_IMAGE.getImageName(),
                        50,
                        List.of(AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.COMMUNICATOR_LEVEL_1_WORLD_2,
                        AchievementDescription.TALK_TO_NPC.getDescriptionWithRequiredAmount(5),
                        AchievementImage.NPC_IMAGE.getImageName(),
                        5,
                        List.of(AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.COMMUNICATOR_LEVEL_2_WORLD_2,
                        AchievementDescription.TALK_TO_NPC.getDescriptionWithRequiredAmount(25),
                        AchievementImage.NPC_IMAGE.getImageName(),
                        25,
                        List.of(AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.COMMUNICATOR_LEVEL_3_WORLD_2,
                        AchievementDescription.TALK_TO_ALL_NPC.getDescription(),
                        AchievementImage.NPC_IMAGE.getImageName(),
                        50,
                        List.of(AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.COMMUNICATOR_LEVEL_1_WORLD_3,
                        AchievementDescription.TALK_TO_NPC.getDescriptionWithRequiredAmount(5),
                        AchievementImage.NPC_IMAGE.getImageName(),
                        5,
                        List.of(AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.COMMUNICATOR_LEVEL_2_WORLD_3,
                        AchievementDescription.TALK_TO_NPC.getDescriptionWithRequiredAmount(25),
                        AchievementImage.NPC_IMAGE.getImageName(),
                        25,
                        List.of(AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.COMMUNICATOR_LEVEL_3_WORLD_3,
                        AchievementDescription.TALK_TO_ALL_NPC.getDescription(),
                        AchievementImage.NPC_IMAGE.getImageName(),
                        50,
                        List.of(AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.COMMUNICATOR_LEVEL_1_WORLD_4,
                        AchievementDescription.TALK_TO_NPC.getDescriptionWithRequiredAmount(5),
                        AchievementImage.NPC_IMAGE.getImageName(),
                        5,
                        List.of(AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.COMMUNICATOR_LEVEL_2_WORLD_4,
                        AchievementDescription.TALK_TO_NPC.getDescriptionWithRequiredAmount(25),
                        AchievementImage.NPC_IMAGE.getImageName(),
                        25,
                        List.of(AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.COMMUNICATOR_LEVEL_3_WORLD_4,
                        AchievementDescription.TALK_TO_ALL_NPC.getDescription(),
                        AchievementImage.NPC_IMAGE.getImageName(),
                        50,
                        List.of(AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.COMMUNICATOR_LEVEL_1,
                        AchievementDescription.TALK_TO_NPC_IN_TOTAL.getDescriptionWithRequiredAmount(50),
                        AchievementImage.NPC_IMAGE.getImageName(),
                        50,
                        List.of(AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.COMMUNICATOR_LEVEL_2,
                        AchievementDescription.TALK_TO_NPC_IN_TOTAL.getDescriptionWithRequiredAmount(100),
                        AchievementImage.NPC_IMAGE.getImageName(),
                        100,
                        List.of(AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.COMMUNICATOR_LEVEL_3,
                        AchievementDescription.TALK_TO_ALL_NPC.getDescription(),
                        AchievementImage.NPC_IMAGE.getImageName(),
                        200,
                        List.of(AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.CHICKENSHOCK_MASTER,
                        AchievementDescription.CHICKENSHOCK.getDescription(),
                        AchievementImage.CHICKEN_IMAGE.getImageName(),
                        1,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE)
                ),
                new Achievement(
                        AchievementTitle.MEMORY_MASTER,
                        AchievementDescription.MEMORY.getDescription(),
                        AchievementImage.MEMORY_IMAGE.getImageName(),
                        1,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE)
                ),
                new Achievement(
                        AchievementTitle.FINITEQUIZ_MASTER,
                        AchievementDescription.FINITEQUIZ.getDescription(),
                        AchievementImage.FINITEQUIZ_IMAGE.getImageName(),
                        1,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE)
                ),
                new Achievement(
                        AchievementTitle.CROSSWORDPUZZLE_MASTER,
                        AchievementDescription.CROSSWORDPUZZLE.getDescription(),
                        AchievementImage.CROSSWORDPUZZLE_IMAGE.getImageName(),
                        1,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE)
                ),
                new Achievement(
                        AchievementTitle.BUGFINDER_MASTER,
                        AchievementDescription.BUGFINDER.getDescription(),
                        AchievementImage.BUGFINDER_IMAGE.getImageName(),
                        1,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE)
                ),
                new Achievement(
                        AchievementTitle.TOWERCRUSH_MASTER,
                        AchievementDescription.TOWERCRUSH.getDescription(),
                        AchievementImage.TOWERCRUSH_IMAGE.getImageName(),
                        1,
                        Arrays.asList(AchievementCategory.SOCIALIZING, AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE)
                ),
                new Achievement(
                        AchievementTitle.TOWERDEFENSE_MASTER,
                        AchievementDescription.TOWERDEFENSE.getDescription(),
                        AchievementImage.TOWERDEFENSE_IMAGE.getImageName(),
                        1,
                        Arrays.asList(AchievementCategory.SOCIALIZING, AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE)
                ),
                new Achievement(
                        AchievementTitle.TRAVELER,
                        AchievementDescription.USE_UFO.getDescriptionWithRequiredAmount(3),
                        AchievementImage.UFO_IMAGE.getImageName(),
                        3,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING)
                ),
                new Achievement(
                        AchievementTitle.GAMER,
                        AchievementDescription.LOGIN.getDescriptionWithRequiredAmount(2),
                        AchievementImage.CALENDER_IMAGE.getImageName(),
                        2,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING, AchievementCategory.SOCIALIZING, AchievementCategory.COMPETITIVE)
                ),
                new Achievement(
                        AchievementTitle.PROFESSIONAL_GAMER,
                        AchievementDescription.LOGIN.getDescriptionWithRequiredAmount(5),
                        AchievementImage.CALENDER_IMAGE.getImageName(),
                        5,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING, AchievementCategory.SOCIALIZING, AchievementCategory.COMPETITIVE)
                ),
                new Achievement(
                        AchievementTitle.GOAT,
                        AchievementDescription.LEADEROARD_1.getDescription(),
                        AchievementImage.MEDAL_1_IMAGE.getImageName(),
                        1,
                        List.of(AchievementCategory.COMPETITIVE)
                ),
                new Achievement(
                        AchievementTitle.ONE_OF_THE_BEST_PLAYERS,
                        AchievementDescription.LEADEROARD_2_3.getDescription(),
                        AchievementImage.MEDAL_3_IMAGE.getImageName(),
                        1,
                        List.of(AchievementCategory.COMPETITIVE)
                ),
                new Achievement(
                        AchievementTitle.GET_COINS,
                        AchievementDescription.GET_COINS.getDescriptionWithRequiredAmount(50),
                        AchievementImage.COIN_IMAGE.getImageName(),
                        50,
                        List.of(AchievementCategory.ACHIEVING)
                ),
                new Achievement(
                        AchievementTitle.GET_MORE_COINS,
                        AchievementDescription.GET_COINS.getDescriptionWithRequiredAmount(150),
                        AchievementImage.COIN_IMAGE.getImageName(),
                        150,
                        List.of(AchievementCategory.ACHIEVING)
                ),
                new Achievement(
                        AchievementTitle.LEVEL_UP,
                        AchievementDescription.LEVEL_UP.getDescriptionWithRequiredAmount(2),
                        AchievementImage.LEVEL_UP_IMAGE.getImageName(),
                        2,
                        List.of(AchievementCategory.ACHIEVING)
                )
        );
        achievementRepository.saveAll(currentAchievementList);
        initializeAchievements();
    }

    /**
     * Initializes for all players their achievements of their courses.
     */
    private void initializeAchievements() {
        for (final Player player : playerRepository.findAll()) {
            initializeAchievementsOfPlayer(player);
            removeNotExistentAchievements(player, achievementRepository.findAll());
            playerRepository.save(player);
        }
    }

    /**
     * Initializes all achievement statistics for a player's courses if non-existent
     *
     * @param player player whose achievements are initialized
     */
    @Transactional
    public void initializeAchievementsOfPlayer(Player player) {
        for (final Course course : courseRepository.findAll()) {
            createAchievementStatistics(course, player);
        }
    }

    /**
     * Initializes all achievement statistics for a course's players if non-existent
     *
     * @param course player whose achievements are initialized
     */
    @Transactional
    public void initializeAchievementsOfCourse(Course course) {
        for (final Player player : playerRepository.findAll()) {
            createAchievementStatistics(course, player);
        }
    }

    /**
     * Creates all achievement statistics for a given course and given player if non-existent
     *
     * @param course course the achievements should be created for
     * @param player player the achievements should be created for
     */
    private void createAchievementStatistics(Course course, Player player) {
        final List<Achievement> achievements = achievementRepository.findAll();
        player.getAchievementStatistics().removeIf(stat ->
                achievements.stream().noneMatch(ach -> ach.getId().equals(stat.getAchievement().getId()))
        );

        for (final Achievement achievement : achievements) {
            if (player.getAchievementStatistics().stream()
                    .noneMatch(stat -> stat.getAchievement().getId().equals(achievement.getId())
                            && stat.getCourse().equals(course))) {
                final AchievementStatistic stat = new AchievementStatistic(player, course, achievement);
                player.getAchievementStatistics().add(stat);
                course.getAchievementStatistics().add(stat);
            }
        }
    }

    /**
     * Removes all non-existent achievements of a player
     *
     * @param player to remove non-existent achievements from
     */
    private void removeNotExistentAchievements(Player player, List<Achievement> achievements) {
        player.getAchievementStatistics()
                .removeIf(achievementStatistic ->
                        achievements
                                .stream()
                                .noneMatch(achievement ->
                                        achievement
                                                .getAchievementTitle()
                                                .equals(achievementStatistic.getAchievement().getAchievementTitle())
                                )
                );
    }
}
