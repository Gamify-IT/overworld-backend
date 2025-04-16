package de.unistuttgart.overworldbackend.service;

import de.unistuttgart.overworldbackend.data.Achievement;
import de.unistuttgart.overworldbackend.data.AchievementStatistic;
import de.unistuttgart.overworldbackend.data.Course;
import de.unistuttgart.overworldbackend.data.Player;
import de.unistuttgart.overworldbackend.data.World;
import de.unistuttgart.overworldbackend.data.enums.AchievementCategory;
import de.unistuttgart.overworldbackend.data.enums.AchievementDescription;
import de.unistuttgart.overworldbackend.data.enums.AchievementImage;
import de.unistuttgart.overworldbackend.data.enums.AchievementTitle;
import de.unistuttgart.overworldbackend.repositories.AchievementRepository;
import de.unistuttgart.overworldbackend.repositories.AchievementStatisticRepository;
import de.unistuttgart.overworldbackend.repositories.CourseRepository;
import de.unistuttgart.overworldbackend.repositories.PlayerRepository;
import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@EnableScheduling
public class AchievementService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private WorldService worldService;

    @Autowired
    private AchievementStatisticRepository achievementStatisticRepository;

    /**
     * Checks for all players the current achievements adds new created achievements to the player and removes none existing achievements.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void updatePlayerStatisticAchievements() {
        List<Achievement> currentAchievementList = new ArrayList<>();

        for (final Course course : courseRepository.findAll()) {
            createBookAchievements(currentAchievementList, course);
            createAchievementForEachMinigame(currentAchievementList, course);
            createNpcAchievements(currentAchievementList, course);
            createDungeonAchievements(currentAchievementList, course);
            createMinigameAchievements(currentAchievementList, course);
            createOpenerAndLevelUpAchievements(currentAchievementList, course);
            createAllOtherAchievements(currentAchievementList, course);
        }

        achievementRepository.saveAll(currentAchievementList);

        /*List<Achievement> existingAchievements = achievementRepository.findAll(Sort.by("achievementTitle"));

        existingAchievements.sort(Comparator.comparing(Achievement::getAchievementTitle));
        List<Achievement> achievementsToDelete = existingAchievements.stream()
                .filter(existingAchievement ->
                        currentAchievementList.stream()
                                .noneMatch(currentAchievement ->
                                        currentAchievement.getAchievementTitle().equals(existingAchievement.getAchievementTitle())
                                )
                )
                .filter(achievement ->
                        playerRepository.findAll().stream()
                                .flatMap(player -> player.getAchievementStatistics().stream())
                                .noneMatch(statistic ->
                                        statistic.getAchievement().equals(achievement) && statistic.isCompleted()
                                )
                )
                .toList();

        for (final Course course : courseRepository.findAll()) {
            for (final Player player : playerRepository.findAll()) {
                for (final Achievement achievement : currentAchievementList) {
                    if (player.getAchievementStatistics()
                            .stream()
                            .noneMatch(achievementStatistic ->
                                    achievementStatistic.getAchievement().getAchievementTitle().equals(achievement.getAchievementTitle()))) {
                        player.getAchievementStatistics().add(new AchievementStatistic(player, course, achievement));
                    }
                }


                player.getAchievementStatistics().removeIf(achievementStatistic ->
                        achievementsToDelete.stream()
                                .anyMatch(achievement ->
                                        achievement.getAchievementTitle().equals(achievementStatistic.getAchievement().getAchievementTitle())

                                )
                );
                playerRepository.save(player);
            }
        }

        if (!achievementsToDelete.isEmpty()) {
            entityManager.flush();
            entityManager.clear();
            achievementRepository.deleteAll(achievementsToDelete);
        }

        achievementRepository.saveAll(currentAchievementList);*/
        initializeAchievements();
    }

    /**
     * Initializes for all players their achievements of their courses.
     */
    private void initializeAchievements() {
        for (final Player player : playerRepository.findAll()) {
            initializeAchievementsOfPlayer(player);
            //removeNotExistentAchievements(player, achievementRepository.findAll());
        }
    }

    /**
     * Initializes all achievement statistics for a player's courses if non-existent.
     *
     * @param player player whose achievements are initialized
     */
    public void initializeAchievementsOfPlayer(Player player) {
        for (final Course course : courseRepository.findAll()) {
            createAchievementStatistics(course, player);
        }
    }

    /**
     * Initializes all achievement statistics for a course's players if non-existent.
     *
     * @param course player whose achievements are initialized
     */
    public void initializeAchievementsOfCourse(Course course) {
        for (final Player player : playerRepository.findAll()) {
            createAchievementStatistics(course, player);
        }
    }

    /**
     * Creates all achievement statistics for a given course and given player if non-existent.
     *
     * @param course course the achievements should be created for
     * @param player player the achievements should be created for
     */
    private void createAchievementStatistics(Course course, Player player) {
        final List<Achievement> achievements = achievementRepository.findAll();
        player
            .getAchievementStatistics()
            .removeIf(stat ->
                achievements
                    .stream()
                    .noneMatch(achievement -> achievement.getId().equals(stat.getAchievement().getId()) && !stat.isCompleted())
            );

        for (final Achievement achievement : achievements) {
            if (
                achievement.getCourseId() == course.getId() &&
                player
                    .getAchievementStatistics()
                    .stream()
                    .noneMatch(achievementStatistic ->
                        achievementStatistic.getAchievement().getId().equals(achievement.getId()) &&
                        achievementStatistic.getCourse().equals(course)
                    )
            ) {
                final AchievementStatistic stat = new AchievementStatistic(player, course, achievement);
                player.getAchievementStatistics().add(stat);
                course.getAchievementStatistics().add(stat);
            }
        }
    }

    /**
     * Removes all non-existent achievements of a player.
     *
     * @param player to remove non-existent achievements from
     */
    private void removeNotExistentAchievements(Player player, List<Achievement> achievements) {
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
    }

    /**
     * Creates 5 achievements related to books.
     * This function calculates the number of books, that have description and text and are available in each world and in total,
     * and creates achievements based on the number of books and active worlds.
     *
     * @param currentAchievementList The list of achievements to which the newly created achievements will be added.
     *
     */
    private void createBookAchievements(List<Achievement> currentAchievementList, Course course) {
        int[] bookCountWorld = new int[5];
        int bookCountInTotal = 0;
        String achievementTitlePrefix = "READER_WORLD_";

        for (int i = 1; i <= 4; i++) {
            bookCountWorld[i] = getBookCount(i, course.getId());
            bookCountInTotal += bookCountWorld[i];
            if (bookCountWorld[i] !=0) {
                if (i == 1 || isWorldActive(i, course.getId())) {
                    currentAchievementList.addAll(Arrays.asList(
                            new Achievement(
                                    AchievementTitle.valueOf(achievementTitlePrefix + i),
                                    AchievementDescription.INTERACT_WITH_BOOKS.getDescriptionWithRequiredAmount(bookCountWorld[i]),
                                    AchievementImage.BOOK_IMAGE.getImageName(),
                                    bookCountWorld[i],
                                    Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING),
                                    course.getId()
                            )
                    ));
                }
            }
            if (bookCountInTotal != 0) {
                currentAchievementList.addAll(Arrays.asList(
                        new Achievement(
                                AchievementTitle.READER,
                                AchievementDescription.INTERACT_WITH_BOOKS_IN_TOTAL.getDescriptionWithRequiredAmount(bookCountInTotal),
                                AchievementImage.BOOK_IMAGE.getImageName(),
                                bookCountInTotal,
                                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING),
                                course.getId()
                        )
                ));
            }
        }

    }

    /**
     * Creates 5 achievements related to NPCs.
     * This function calculates the number of NPCs, that have description and text and are available in each world and in total,
     * and creates achievements based on the number of NPCs and active worlds.
     *
     * @param currentAchievementList The list of achievements to which the newly created achievements will be added.
     *
     */
    private void createNpcAchievements(List<Achievement> currentAchievementList, Course course) {

        int[] npcCountWorld = new int[5];
        int npcCountInTotal = 0;
        String achievementTitlePrefix = "COMMUNICATOR_WORLD_";

        for (int i = 1; i <= 4; i++) {
            npcCountWorld[i] = getNpcCount(i, course.getId());
            npcCountInTotal += npcCountWorld[i];
            if (npcCountWorld[i] != 0) {
                if (i == 1 || isWorldActive(i, course.getId())) {
                    currentAchievementList.addAll(Arrays.asList(
                            new Achievement(
                                    AchievementTitle.valueOf(achievementTitlePrefix + i),
                                    AchievementDescription.TALK_TO_NPC.getDescriptionWithRequiredAmount(npcCountWorld[i]),
                                    AchievementImage.NPC_IMAGE.getImageName(),
                                    npcCountWorld[i],
                                    Arrays.asList(AchievementCategory.EXPLORING),
                                    course.getId()
                            )
                    ));
                }
            }
        }
        if(npcCountInTotal != 0){
            currentAchievementList.addAll(Arrays.asList(
                    new Achievement(
                            AchievementTitle.COMMUNICATOR,
                            AchievementDescription.TALK_TO_NPC_IN_TOTAL.getDescriptionWithRequiredAmount(npcCountInTotal),
                            AchievementImage.NPC_IMAGE.getImageName(),
                            npcCountInTotal,
                            Arrays.asList(AchievementCategory.EXPLORING),
                            course.getId()
                    )
            ));
        }
    }

    /**
     * Creates 4 achievements related to dungeons.
     * This function creates achievements for each active world if there is at least one dungeon active.
     *
     * @param currentAchievementList The list of achievements to which the newly created achievements will be added.
     *
     */
    private void createDungeonAchievements(List<Achievement> currentAchievementList, Course course) {
        int[] countActiveDungeons =  new int[5];
        String achievementTitle = "MINER_WORLD_";

        for(int i = 1; i <= 4; i++){
            countActiveDungeons[i] = getActiveDungeonCount(i, course.getId());
            if (countActiveDungeons[i] >= 1) {
                currentAchievementList.addAll(Arrays.asList(new Achievement(
                        AchievementTitle.valueOf(achievementTitle + i),
                        AchievementDescription.UNLOCK_DUNGEONS.getDescriptionWithRequiredAmount(i),
                        AchievementImage.DUNGEON_IMAGE.getImageName(),
                        1,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING),
                        course.getId()
                )));
            }
        }
    }

    /**
     * Creates 1 achievement related to successfully completed minigames and 1 achievement related to founded minigame spots.
     * This function calculates the number of minigames, that are created in each world
     * and creates achievements based on the number of minigames and active worlds.
     *
     * @param currentAchievementList The list of achievements to which the newly created achievements will be added.
     *
     */
    private void createMinigameAchievements(List<Achievement> currentAchievementList, Course course) {
        int minigameCountInTotal = 0;
        int requiredAmountForAchievement;
        for (int i = 1; i <= 4; i++) {
            minigameCountInTotal += getMinigameCount(i, course.getId());
        }

        if(minigameCountInTotal != 0){
            if(minigameCountInTotal == 1 || minigameCountInTotal == 2 || minigameCountInTotal == 3) {
                requiredAmountForAchievement = minigameCountInTotal;
            } else {
                requiredAmountForAchievement = minigameCountInTotal - 2;
            }
            currentAchievementList.addAll(Arrays.asList(
                    new Achievement(
                            AchievementTitle.MINIGAME_ACHIEVER,
                            AchievementDescription.COMPLETE_MINIGAMES.getDescriptionWithRequiredAmount(requiredAmountForAchievement),
                            AchievementImage.STAR_IMAGE.getImageName(),
                            requiredAmountForAchievement,
                            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE),
                            course.getId()
                    ),
                    new Achievement(
                            AchievementTitle.MINIGAME_SPOTS_FINDER,
                            AchievementDescription.FIND_MINIGAME_SPOTS.getDescriptionWithRequiredAmount(requiredAmountForAchievement),
                            AchievementImage.MINIGAME_SPOT_IMAGE.getImageName(),
                            requiredAmountForAchievement,
                            Arrays.asList(AchievementCategory.EXPLORING),
                            course.getId()
                    )
            ));
        } else {
            List<Achievement> achievementsToDelete = achievementRepository.findAll(Sort.by("achievementTitle")).stream()
                    .filter(achievement ->
                            AchievementTitle.MINIGAME_ACHIEVER.toString().equals(achievement.getAchievementTitle()) ||
                                    AchievementTitle.MINIGAME_SPOTS_FINDER.toString().equals(achievement.getAchievementTitle()))
                    .collect(Collectors.toList());

            achievementRepository.deleteAll(achievementsToDelete);
        }
    }

    /**
     * Creates 1 achievement for level up if the world 2 is active and 3 achievements for 3 worlds, if they are active.
     * This function check if the necessary world is active and creates achievements based on it.
     *
     * @param currentAchievementList The list of achievements to which the newly created achievements will be added.
     *
     */
    private void createOpenerAndLevelUpAchievements(List<Achievement> currentAchievementList, Course course) {
        String achievementTitle = "OPENER_WORLD_";
        for (int i = 2; i <= 4; i++) {
            if (isWorldActive(i, course.getId())) {
                currentAchievementList.addAll(Arrays.asList(
                        new Achievement(
                                AchievementTitle.valueOf(achievementTitle + i),
                                AchievementDescription.UNLOCK_WORLD.getDescriptionWithRequiredAmount(i),
                                AchievementImage.GLASS_IMAGE.getImageName(),
                                1,
                                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING),
                                course.getId()
                        )
                ));

                if (i == 2) {
                    currentAchievementList.addAll(Arrays.asList(
                            new Achievement(
                                    AchievementTitle.LEVEL_UP,
                                    AchievementDescription.LEVEL_UP.getDescriptionWithRequiredAmount(i),
                                    AchievementImage.LEVEL_UP_IMAGE.getImageName(),
                                    i,
                                    Arrays.asList(AchievementCategory.ACHIEVING),
                                    course.getId()
                            )
                    ));
                }
            }
        }
    }

    /**
     * Creates achievement for each type of minigames, that were created in course, based on their name and active
     * worlds, in which these minigames were created.
     *
     * @param currentAchievementList The list of achievements to which the newly created achievements will be added.
     *
     */
    private void createAchievementForEachMinigame(List<Achievement> currentAchievementList, Course course) {
        List<String> minigameNames = getMinigameNames(course.getId());
        minigameNames.forEach(minigameName -> {
            if (!minigameName.toUpperCase().equals("NONE") && !minigameName.toUpperCase().equals("REGEXGAME")) {
                currentAchievementList.addAll(Arrays.asList(
                        new Achievement(
                                AchievementTitle.valueOf(minigameName.toUpperCase() + "_MASTER"),
                                AchievementDescription.valueOf(minigameName.toUpperCase()).getDescription(),
                                AchievementImage.valueOf(minigameName.toUpperCase() + "_IMAGE").getImageName(),
                                1,
                                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE),
                                course.getId()
                        )
                ));
            }
        });
    }

    /**
     * Creates all static achievements, that are independent of course settings and do not require conditions to be met
     * for the achievement to be created
     *
     * @param currentAchievementList The list of achievements to which the newly created achievements will be added.
     *
     */
    private void createAllOtherAchievements(List<Achievement> currentAchievementList, Course course) {
        currentAchievementList.addAll(Arrays.asList(
                new Achievement(
                        AchievementTitle.GO_FOR_A_WALK,
                        AchievementDescription.WALK_TILES.getDescriptionWithRequiredAmount(10),
                        AchievementImage.FOOT_IMAGE.getImageName(),
                        10,
                        Arrays.asList(AchievementCategory.EXPLORING, AchievementCategory.ACHIEVING),
                        course.getId()
                ),
                new Achievement(
                        AchievementTitle.GO_FOR_A_LONGER_WALK,
                        AchievementDescription.WALK_TILES.getDescriptionWithRequiredAmount(1000),
                        AchievementImage.FOOT_IMAGE.getImageName(),
                        1000,
                        Arrays.asList(AchievementCategory.EXPLORING, AchievementCategory.ACHIEVING),
                        course.getId()
                ),
                new Achievement(
                        AchievementTitle.SELECT_CHARACTER,
                        AchievementDescription.CHANGE_SKIN.getDescription(),
                        AchievementImage.NPC_IMAGE.getImageName(),
                        1,
                        Arrays.asList(AchievementCategory.EXPLORING),
                        course.getId()
                ),
                new Achievement(
                        AchievementTitle.BEGINNER,
                        AchievementDescription.PLAY_MINUTES.getDescriptionWithRequiredAmount(30),
                        AchievementImage.CLOCK_IMAGE.getImageName(),
                        30,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING, AchievementCategory.SOCIALIZING, AchievementCategory.COMPETITIVE),
                        course.getId()
                ),
                new Achievement(
                        AchievementTitle.EXPERIENCED_PLAYER,
                        AchievementDescription.PLAY_MINUTES.getDescriptionWithRequiredAmount(90),
                        AchievementImage.CLOCK_IMAGE.getImageName(),
                        90,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING, AchievementCategory.SOCIALIZING, AchievementCategory.COMPETITIVE),
                        course.getId()
                ),
                new Achievement(
                        AchievementTitle.SPEEDRUNNER,
                        AchievementDescription.USE_SPRINT.getDescriptionWithRequiredAmount(30),
                        AchievementImage.ROCKET_IMAGE.getImageName(),
                        30,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE),
                        course.getId()
                ),
                new Achievement(
                        AchievementTitle.TELEPORTER_BEGINNER,
                        AchievementDescription.OPEN_TELEPORTERS.getDescriptionWithRequiredAmount(4),
                        AchievementImage.TELEPORTER_IMAGE.getImageName(),
                        4,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING),
                        course.getId()
                ),
                new Achievement(
                        AchievementTitle.TELEPORTER_PROFESSIONAL,
                        AchievementDescription.OPEN_TELEPORTERS.getDescriptionWithRequiredAmount(10),
                        AchievementImage.TELEPORTER_IMAGE.getImageName(),
                        10,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING),
                        course.getId()
                ),
                new Achievement(
                        AchievementTitle.TRAVELER,
                        AchievementDescription.USE_UFO.getDescriptionWithRequiredAmount(3),
                        AchievementImage.UFO_IMAGE.getImageName(),
                        3,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING),
                        course.getId()
                ),
                new Achievement(
                        AchievementTitle.GAMER,
                        AchievementDescription.LOGIN.getDescriptionWithRequiredAmount(2),
                        AchievementImage.CALENDER_IMAGE.getImageName(),
                        2,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING, AchievementCategory.SOCIALIZING, AchievementCategory.COMPETITIVE),
                        course.getId()
                ),
                new Achievement(
                        AchievementTitle.PROFESSIONAL_GAMER,
                        AchievementDescription.LOGIN.getDescriptionWithRequiredAmount(5),
                        AchievementImage.CALENDER_IMAGE.getImageName(),
                        5,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING, AchievementCategory.SOCIALIZING, AchievementCategory.COMPETITIVE),
                        course.getId()
                ),
                new Achievement(
                        AchievementTitle.GOAT,
                        AchievementDescription.LEADEROARD_1.getDescription(),
                        AchievementImage.MEDAL_1_IMAGE.getImageName(),
                        1,
                        Arrays.asList(AchievementCategory.COMPETITIVE),
                        course.getId()
                ),
                new Achievement(
                        AchievementTitle.ONE_OF_THE_BEST_PLAYERS,
                        AchievementDescription.LEADEROARD_2_3.getDescription(),
                        AchievementImage.MEDAL_3_IMAGE.getImageName(),
                        1,
                        Arrays.asList(AchievementCategory.COMPETITIVE),
                        course.getId()
                ),
                new Achievement(
                        AchievementTitle.GET_COINS,
                        AchievementDescription.GET_COINS.getDescriptionWithRequiredAmount(50),
                        AchievementImage.COIN_IMAGE.getImageName(),
                        50,
                        Arrays.asList(AchievementCategory.ACHIEVING),
                        course.getId()
                ),
                new Achievement(
                        AchievementTitle.GET_MORE_COINS,
                        AchievementDescription.GET_COINS.getDescriptionWithRequiredAmount(150),
                        AchievementImage.COIN_IMAGE.getImageName(),
                        150,
                        Arrays.asList(AchievementCategory.ACHIEVING),
                        course.getId()
                )
        ));
    }

    /**
     * Gets amount of books from the specified course and world, checks if the world is active and filters books based
     * on whether they have a description or text.
     *
     * @param worldId The ID of the world from which the book count is to be retrieved, based on whether the book has a
     *                description or text.
     * @return The total number of books in the specified world that have a description and text, or 0 if the world is
     * not active.
     *
     */
    private int getBookCount(int worldId, int courseId){
        World world = worldService.getWorldByIndexFromCourse(courseId, worldId);

        if(isWorldActive(worldId, courseId) || worldId == 1) {
            long bookCountInWorld = world.getBooks().stream()
                    .filter(book -> book.getDescription() != null && !book.getText().isEmpty())
                    .count();
            return (int) bookCountInWorld;
        } else {
            return 0;
        }
    }

    /**
     * Gets amount of NPCs from the specified course and world, checks if the world is active and filters NPCs based
     * on whether they have a description or text.
     *
     * @param worldId The ID of the world from which the NPC count is to be retrieved, based on whether the NPC has a
     *                description or text.
     * @return The total number of NPCs in the specified world that have a description and text, or 0 if the world is
     * not active.
     *
     */
    private int getNpcCount(int worldId, int courseId){
        World world = worldService.getWorldByIndexFromCourse(courseId, worldId);

        if(isWorldActive(worldId, courseId) || worldId == 1) {
            long npcCountInWorld = world.getNpcs().stream()
                    .filter(npc -> npc.getDescription() != null && npc.getText() != null && !npc.getText().isEmpty())
                    .count();
            return (int) npcCountInWorld;
        } else {
            return 0;
        }
    }

    /**
     * Checks if the specified world is active.
     *
     * @param worldId The ID of the world to check.
     * @return True if the world is active, false otherwise.
     */
    private boolean isWorldActive(int worldId, int courseId){
        World world = worldService.getWorldByIndexFromCourse(courseId, worldId);
        return world.isActive();
    }

    /**
     * Gets the count of active dungeons in the specified course and world.
     *
     * @param worldId The ID of the world from which the count of active dungeons is to be retrieved.
     * @return The number of active dungeons in the specified world.
     */
    private int getActiveDungeonCount(int worldId, int courseId){
        World world = worldService.getWorldByIndexFromCourse(courseId, worldId);

        float countOfActiveDungeonsInWorld = world.getDungeons().stream()
                .filter(dungeon -> dungeon.isActive())
                .count();
        return (int) countOfActiveDungeonsInWorld;
    }

    /**
     * Gets the count of minigames in the specified course and world, considering only active worlds.
     *
     * @param worldId The ID of the world from which the count of minigames is to be retrieved.
     * @return The number of minigames in the specified world that were created, or 0 if the world is not active.
     */
    private int getMinigameCount(int worldId, int courseId){
        World world = worldService.getWorldByIndexFromCourse(courseId, worldId);
        if(isWorldActive(worldId, courseId) || worldId == 1) {
            long minigameCountInWorld = world.getMinigameTasks().stream()
                    .filter(minigame -> minigame.getGame() != null && !"NONE".equals(minigame.getGame().name()))
                    .count();

            return (int) minigameCountInWorld;
        }
        return 0;
    }

    /**
     * Retrieves the names of minigames from all active worlds, filtering out duplicates.
     *
     * @return A list of unique minigame names from the active worlds.
     */
    private List<String> getMinigameNames(int courseId) {
        Set<World> allWorlds = worldService.getAllWorldsFromCourse(courseId);

        List<World> activeWorlds = allWorlds.stream()
                .filter(world -> world.isActive() || world.getIndex() == 1)
                .collect(Collectors.toList());

        List<String> minigameNames = activeWorlds.stream()
                .flatMap(world -> world.getMinigameTasks().stream())
                .filter(minigame -> minigame.getGame() != null)
                .map(minigame -> minigame.getGame().name())
                .distinct()
                .collect(Collectors.toList());

        return minigameNames;
    }
}

