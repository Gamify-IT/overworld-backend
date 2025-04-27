package de.unistuttgart.overworldbackend.service;

import de.unistuttgart.overworldbackend.data.*;
import de.unistuttgart.overworldbackend.data.enums.*;
import de.unistuttgart.overworldbackend.repositories.AchievementRepository;
import de.unistuttgart.overworldbackend.repositories.CourseRepository;
import de.unistuttgart.overworldbackend.repositories.PlayerRepository;

import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.unistuttgart.overworldbackend.repositories.WorldRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
    private WorldRepository worldRepository;

    /**
     * Creates achievements for each existing course based on their configuration at the start of the application.
     * Checks for all players in each course their current achievement statistics, adds new ones and removes non exiting ones.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initializePlayerStatisticAchievements() {
        for (final Course course : courseRepository.findAll()) {
            updateCourseAchievements(course);
        }
    }

    /**
     * Creates new achievement statistics for a list of achievements for a player of a certain course.
     *
     * @param course             course of the player
     * @param courseAchievements achievements to create achievements statistics for
     * @param player             player whose achievement statistics are created
     * @return true if new achievement statistics were created
     */
    private boolean createNewAchievementStatistics(final Course course, final List<Achievement> courseAchievements, final Player player) {
        boolean changed = false;

        Map<UUID, AchievementStatistic> existingStats = player.getAchievementStatistics()
                .stream()
                .filter(stat -> stat.getCourseId() == course.getId())
                .collect(Collectors.toMap(
                        stat -> stat.getAchievement().getId(),
                        stat -> stat
                ));

        for (Achievement achievement : courseAchievements) {
            if (!existingStats.containsKey(achievement.getId())) {
                player.getAchievementStatistics().add(
                        new AchievementStatistic(player, course.getId(), achievement)
                );
                changed = true;
            }
        }

        return changed;
    }

    /**
     * Updates all achievements of a course.
     *
     * @param course course whose achievements are updated
     */
    public void updateCourseAchievements(final Course course) {
        updateBookAchievements(course);
        updateNpcAchievements(course);
        updateDungeonAchievements(course);
        updateMinigameAchievements(course);
        updateAchievementForEachMinigame(course);
        updateOpenerAndLevelUpAchievements(course);
        updateAllOtherAchievements(course);
    }

    /**
     * Creates all achievements for a course.
     *
     * @param course course whose achievements are created
     * @return list of created achievements
     */
    public List<Achievement> createCourseAchievements(final Course course) {
        List<Achievement> courseAchievements = new ArrayList<>();

        createBookAchievements(courseAchievements, course);
        createAchievementForEachMinigame(courseAchievements, course);
        createNpcAchievements(courseAchievements, course);
        createDungeonAchievements(courseAchievements, course);
        createMinigameAchievements(courseAchievements, course);
        createOpenerAndLevelUpAchievements(courseAchievements, course);
        createAllOtherAchievements(courseAchievements, course);

        achievementRepository.saveAll(courseAchievements);

        return courseAchievements;
    }

    /**
     * Creates achievements for a course and updates the achievement statistics of its players.
     *
     * @param courseAchievements achievements whose statistics are updated
     * @param course             course whose achievements are created
     */
    public void initializeAchievements(final List<Achievement> courseAchievements, final Course course) {
        for (final Player player : playerRepository.findAll()) {
            if (createNewAchievementStatistics(course, courseAchievements, player)) {
                playerRepository.save(player);
            }
        }
    }

    /**
     * Creates achievement statistics for a list of achievements for all players in a course.
     *
     * @param course       course who is considered
     * @param achievements achievements to create statistics for
     */
    private void updatePlayerStatisticAchievements(final Course course, final List<Achievement> achievements) {
        for (Player player : playerRepository.findAll()) {
            Map<UUID, AchievementStatistic> achievementStats = player.getAchievementStatistics()
                    .stream()
                    .filter(stat -> stat.getCourseId() == course.getId())
                    .collect(Collectors.toMap(
                            stat -> stat.getAchievement().getId(),
                            stat -> stat
                    ));
            for (Achievement achievement : achievements) {
                AchievementStatistic stat = achievementStats.get(achievement.getId());
                if (stat != null) {
                    stat.setAchievement(achievement);
                } else {
                    stat = new AchievementStatistic(player, course.getId(), achievement);
                    player.getAchievementStatistics().add(stat);
                }
            }
            playerRepository.save(player);
        }
    }

    /**
     * Creates 5 achievements related to books.
     * This function calculates the number of books, that have description and text and are available in each world and in total,
     * and creates achievements based on the number of books and active worlds.
     *
     * @param courseAchievements The list of achievements to which the newly created achievements will be added.
     */
    public void createBookAchievements(List<Achievement> courseAchievements, Course course) {
        int[] bookCountWorld = new int[5];
        int bookCountInTotal = 0;
        String achievementTitlePrefix = "READER_WORLD_";

        for (int i = 1; i <= 4; i++) {
            bookCountWorld[i] = getBookCount(i, course.getId());
            bookCountInTotal += bookCountWorld[i];
            if (bookCountWorld[i] != 0) {
                if (i == 1 || isWorldActive(i, course.getId())) {
                    courseAchievements.add(
                            new Achievement(
                                    AchievementTitle.valueOf(achievementTitlePrefix + i),
                                    AchievementDescription.INTERACT_WITH_BOOKS.getDescriptionWithRequiredAmount(bookCountWorld[i]),
                                    AchievementImage.BOOK_IMAGE.getImageName(),
                                    bookCountWorld[i],
                                    Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING),
                                    course
                            )
                    );
                }
            }
        }

        if (bookCountInTotal != 0) {
            courseAchievements.add(
                    new Achievement(
                            AchievementTitle.READER,
                            AchievementDescription.INTERACT_WITH_BOOKS_IN_TOTAL.getDescriptionWithRequiredAmount(bookCountInTotal),
                            AchievementImage.BOOK_IMAGE.getImageName(),
                            bookCountInTotal,
                            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING),
                            course
                    )
            );
        }
    }

    /**
     * Updates all book related achievements of a course and the achievement statistics of the course members.
     *
     * @param course course whose achievements are updated
     */
    public void updateBookAchievements(final Course course) {
        List<Achievement> courseAchievements = new ArrayList<>(course.getCourseAchievements());

        int[] bookCountWorld = new int[5];
        int bookCountInTotal = 0;
        String achievementTitlePrefix = "READER_WORLD_";

        for (int i = 1; i <= 4; i++) {
            bookCountWorld[i] = getBookCount(i, course.getId());
            bookCountInTotal += bookCountWorld[i];
            int worldIndex = i;

            // get book-related achievement
            Achievement achievement = courseAchievements.stream().filter(a ->
                    a.getAchievementTitle().name().startsWith(String.format("%s%d", achievementTitlePrefix, worldIndex))).findFirst().orElse(null);

            if (bookCountWorld[i] == 0) {
                // case 1: no achievement required -> delete existing one if there is one
                if ((i == 1 || isWorldActive(i, course.getId())) && achievement != null) {
                    course.removeAchievement(achievement);
                }
            } else {
                // case 2: update existent achievement
                if (achievement != null) {
                    achievement.setDescription(AchievementDescription.INTERACT_WITH_BOOKS.getDescriptionWithRequiredAmount(bookCountWorld[i]));
                    achievement.setAmountRequired(bookCountWorld[i]);
                }
                // case 3: create achievement from scratch
                else {
                    course.addAchievement(new Achievement(
                                    AchievementTitle.valueOf(achievementTitlePrefix + i),
                                    AchievementDescription.INTERACT_WITH_BOOKS.getDescriptionWithRequiredAmount(bookCountWorld[i]),
                                    AchievementImage.BOOK_IMAGE.getImageName(),
                                    bookCountWorld[i],
                                    Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING),
                                    course
                            )
                    );
                }
            }
            courseRepository.save(course);
        }

        Achievement achievement = courseAchievements.stream().filter(a -> a.getAchievementTitle().name().equals("READER")).findFirst().orElse(null);

        if (bookCountInTotal == 0) {
            // case 1: no achievement required -> delete existing one
            if (achievement != null) {
                course.removeAchievement(achievement);
            }
        } else {
            // case 2: update existing achievement
            if (achievement != null) {
                achievement.setDescription(AchievementDescription.INTERACT_WITH_BOOKS_IN_TOTAL.getDescriptionWithRequiredAmount(bookCountInTotal));
                achievement.setAmountRequired(bookCountInTotal);
            }
            // case 3: create required achievement from scratch
            else {
                course.addAchievement(
                        new Achievement(
                                AchievementTitle.READER,
                                AchievementDescription.INTERACT_WITH_BOOKS_IN_TOTAL.getDescriptionWithRequiredAmount(bookCountInTotal),
                                AchievementImage.BOOK_IMAGE.getImageName(),
                                bookCountInTotal,
                                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING),
                                course
                        )
                );
            }
        }

        courseRepository.save(course);

        // update player achievement statistics
        updatePlayerStatisticAchievements(course, course.getCourseAchievements().stream().filter(a -> a.getAchievementTitle().name().startsWith("READER")).collect(Collectors.toList()));
    }

    /**
     * Creates 5 achievements related to NPCs.
     * This function calculates the number of NPCs, that have description and text and are available in each world and in total,
     * and creates achievements based on the number of NPCs and active worlds.
     *
     * @param courseAchievements The list of achievements to which the newly created achievements will be added.
     */
    private void createNpcAchievements(List<Achievement> courseAchievements, Course course) {
        int[] npcCountWorld = new int[5];
        int npcCountInTotal = 0;
        String achievementTitlePrefix = "COMMUNICATOR_WORLD_";

        for (int i = 1; i <= 4; i++) {
            npcCountWorld[i] = getNpcCount(i, course.getId());
            npcCountInTotal += npcCountWorld[i];
            if (npcCountWorld[i] != 0) {
                if (i == 1 || isWorldActive(i, course.getId())) {
                    courseAchievements.add(
                            new Achievement(
                                    AchievementTitle.valueOf(achievementTitlePrefix + i),
                                    AchievementDescription.TALK_TO_NPC.getDescriptionWithRequiredAmount(npcCountWorld[i]),
                                    AchievementImage.NPC_IMAGE.getImageName(),
                                    npcCountWorld[i],
                                    List.of(AchievementCategory.EXPLORING),
                                    course
                            )
                    );
                }
            }
        }
        if (npcCountInTotal != 0) {
            courseAchievements.add(
                    new Achievement(
                            AchievementTitle.COMMUNICATOR,
                            AchievementDescription.TALK_TO_NPC_IN_TOTAL.getDescriptionWithRequiredAmount(npcCountInTotal),
                            AchievementImage.NPC_IMAGE.getImageName(),
                            npcCountInTotal,
                            List.of(AchievementCategory.EXPLORING),
                            course
                    )
            );
        }
    }

    /**
     * Updates all npc related achievements of a course and the achievement statistics of the course members.
     *
     * @param course course whose achievements are updated
     */
    public void updateNpcAchievements(final Course course) {
        List<Achievement> courseAchievements = new ArrayList<>(course.getCourseAchievements());

        int[] npcCountWorld = new int[5];
        int npcCountInTotal = 0;
        String achievementTitlePrefix = "COMMUNICATOR_WORLD_";

        for (int i = 1; i <= 4; i++) {
            npcCountWorld[i] = getNpcCount(i, course.getId());
            npcCountInTotal += npcCountWorld[i];
            int worldIndex = i;

            // get npc-related achievement
            Achievement achievement = courseAchievements.stream().filter(a ->
                    a.getAchievementTitle().name().startsWith(String.format("%s%d", achievementTitlePrefix, worldIndex))).findFirst().orElse(null);

            if (npcCountWorld[i] == 0) {
                // case 1: no achievement required -> delete existing one if there is one
                if (i == 1 || isWorldActive(i, course.getId())) {
                    course.removeAchievement(achievement);
                }
            } else {
                // case 2: update existent achievement
                if (achievement != null) {
                    achievement.setDescription(AchievementDescription.TALK_TO_NPC.getDescriptionWithRequiredAmount(npcCountWorld[i]));
                    achievement.setAmountRequired(npcCountWorld[i]);
                }
                // case 3: create achievement from scratch
                else {
                    course.addAchievement(
                            new Achievement(
                                    AchievementTitle.valueOf(achievementTitlePrefix + i),
                                    AchievementDescription.TALK_TO_NPC.getDescriptionWithRequiredAmount(npcCountWorld[i]),
                                    AchievementImage.NPC_IMAGE.getImageName(),
                                    npcCountWorld[i],
                                    List.of(AchievementCategory.EXPLORING),
                                    course
                            )
                    );
                }
            }
            courseRepository.save(course);
        }

        Achievement achievement = courseAchievements.stream().filter(a -> a.getAchievementTitle().name().equals("COMMUNICATOR")).findFirst().orElse(null);

        if (npcCountInTotal == 0) {
            // case 1: no achievement required -> delete existing one
            if (achievement != null) {
                course.removeAchievement(achievement);
            }
        } else {
            // case 2: update existing achievement
            if (achievement != null) {
                achievement.setDescription(AchievementDescription.TALK_TO_NPC_IN_TOTAL.getDescriptionWithRequiredAmount(npcCountInTotal));
                achievement.setAmountRequired(npcCountInTotal);
            }
            // case 3: create required achievement from scratch
            else {
                course.addAchievement(
                        new Achievement(
                                AchievementTitle.COMMUNICATOR,
                                AchievementDescription.TALK_TO_NPC_IN_TOTAL.getDescriptionWithRequiredAmount(npcCountInTotal),
                                AchievementImage.NPC_IMAGE.getImageName(),
                                npcCountInTotal,
                                List.of(AchievementCategory.EXPLORING),
                                course
                        )
                );
            }
        }

        courseRepository.save(course);

        // update player achievement statistics
        updatePlayerStatisticAchievements(course, course.getCourseAchievements().stream().filter(a -> a.getAchievementTitle().name().startsWith("COMMUNICATOR")).collect(Collectors.toList()));
    }

    /**
     * Creates 4 achievements related to dungeons.
     * This function creates achievements for each active world if there is at least one dungeon active.
     *
     * @param courseAchievements The list of achievements to which the newly created achievements will be added.
     */
    private void createDungeonAchievements(List<Achievement> courseAchievements, Course course) {
        int[] countActiveDungeons = new int[5];
        String achievementTitle = "MINER_WORLD_";

        for (int i = 1; i <= 4; i++) {
            countActiveDungeons[i] = getActiveDungeonCount(i, course.getId());
            if (countActiveDungeons[i] >= 1) {
                courseAchievements.add(new Achievement(
                        AchievementTitle.valueOf(achievementTitle + i),
                        AchievementDescription.UNLOCK_DUNGEONS.getDescriptionWithRequiredAmount(i),
                        AchievementImage.DUNGEON_IMAGE.getImageName(),
                        1,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING),
                        course
                ));
            }
        }
    }

    /**
     * Updates all dungeon related achievements of a course and the achievement statistics of the course members.
     *
     * @param course course whose achievements are updated
     */
    public void updateDungeonAchievements(final Course course) {
        List<Achievement> courseAchievements = new ArrayList<>(course.getCourseAchievements());

        int[] countActiveDungeons = new int[5];
        String achievementTitlePrefix = "MINER_WORLD_";

        for (int i = 1; i <= 4; i++) {
            countActiveDungeons[i] = getActiveDungeonCount(i, course.getId());
            int worldIndex = i;

            // get dungeon related achievement
            Achievement achievement = courseAchievements.stream().filter(a ->
                    a.getAchievementTitle().name().startsWith(String.format("%s%d", achievementTitlePrefix, worldIndex))).findFirst().orElse(null);

            if (countActiveDungeons[i] == 0) {
                // case 1: no achievement required -> delete existing one if there is one
                course.removeAchievement(achievement);
            } else {
                // case 2: update existent achievement
                if (achievement != null) {
                    achievement.setDescription(AchievementDescription.UNLOCK_DUNGEONS.getDescriptionWithRequiredAmount(i));
                    achievement.setAmountRequired(1);
                }
                // case 3: create achievement from scratch
                else {
                    course.addAchievement(
                            new Achievement(
                                    AchievementTitle.valueOf(achievementTitlePrefix + i),
                                    AchievementDescription.UNLOCK_DUNGEONS.getDescriptionWithRequiredAmount(i),
                                    AchievementImage.DUNGEON_IMAGE.getImageName(),
                                    1,
                                    Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING),
                                    course
                            )
                    );
                }
            }
            courseRepository.save(course);

            // update player achievement statistics
            updatePlayerStatisticAchievements(course, course.getCourseAchievements().stream().filter(a -> a.getAchievementTitle().name().startsWith("MINER")).collect(Collectors.toList()));
        }
    }

    /**
     * Creates 1 achievement related to successfully completed minigames and 1 achievement related to founded minigame spots.
     * This function calculates the number of minigames, that are created in each world
     * and creates achievements based on the number of minigames and active worlds.
     *
     * @param courseAchievements The list of achievements to which the newly created achievements will be added.
     */
    private void createMinigameAchievements(List<Achievement> courseAchievements, Course course) {
        int minigameCountInTotal = 0;
        int requiredAmountForAchievement;
        for (int i = 1; i <= 4; i++) {
            minigameCountInTotal += getMinigameCount(i, course.getId());
        }

        if (minigameCountInTotal != 0) {
            if (minigameCountInTotal == 1 || minigameCountInTotal == 2 || minigameCountInTotal == 3) {
                requiredAmountForAchievement = minigameCountInTotal;
            } else {
                requiredAmountForAchievement = minigameCountInTotal - 2;
            }
            courseAchievements.addAll(Arrays.asList(
                    new Achievement(
                            AchievementTitle.MINIGAME_ACHIEVER,
                            AchievementDescription.COMPLETE_MINIGAMES.getDescriptionWithRequiredAmount(requiredAmountForAchievement),
                            AchievementImage.STAR_IMAGE.getImageName(),
                            requiredAmountForAchievement,
                            Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE),
                            course
                    ),
                    new Achievement(
                            AchievementTitle.MINIGAME_SPOTS_FINDER,
                            AchievementDescription.FIND_MINIGAME_SPOTS.getDescriptionWithRequiredAmount(requiredAmountForAchievement),
                            AchievementImage.MINIGAME_SPOT_IMAGE.getImageName(),
                            requiredAmountForAchievement,
                            List.of(AchievementCategory.EXPLORING),
                            course
                    )
            ));
        } else {
            List<Achievement> achievementsToDelete = achievementRepository.findAll(Sort.by("achievementTitle")).stream()
                    .filter(achievement ->
                            AchievementTitle.MINIGAME_ACHIEVER.equals(achievement.getAchievementTitle()) ||
                                    AchievementTitle.MINIGAME_SPOTS_FINDER.equals(achievement.getAchievementTitle()))
                    .collect(Collectors.toList());

            achievementRepository.deleteAll(achievementsToDelete);
        }
    }

    /**
     * Updates all minigame-spot related achievements of a course and the achievement statistics of the course members.
     *
     * @param course course whose achievements are updated
     */
    public void updateMinigameAchievements(final Course course) {
        List<Achievement> courseAchievements = new ArrayList<>(course.getCourseAchievements());

        int minigameCountInTotal = 0;
        int requiredAmountForAchievement;

        for (int i = 1; i <= 4; i++) {
            minigameCountInTotal += getMinigameCount(i, course.getId());
        }

        // get minigame related achievements
        List<Achievement> achievements = courseAchievements.stream().filter(achievement ->
                        achievement.getAchievementTitle().name().equals("MINIGAME_ACHIEVER") ||
                                achievement.getAchievementTitle().name().equals("MINIGAME_SPOTS_FINDER"))
                .toList();

        if (minigameCountInTotal == 0) {
            // case 1: no achievements required -> delete existing ones if there are any
            achievements.forEach(course::removeAchievement);
        } else {
            if (minigameCountInTotal == 1 || minigameCountInTotal == 2 || minigameCountInTotal == 3) {
                requiredAmountForAchievement = minigameCountInTotal;
            } else {
                requiredAmountForAchievement = minigameCountInTotal - 2;
            }
            // case 2: update existent achievements
            if (!achievements.isEmpty()) {
                for (Achievement achievement : achievements) {
                    if (achievement.getAchievementTitle().name().equals("MINIGAME_ACHIEVER")) {
                        achievement.setDescription(AchievementDescription.COMPLETE_MINIGAMES.getDescriptionWithRequiredAmount(requiredAmountForAchievement));
                        achievement.setAmountRequired(requiredAmountForAchievement);
                    } else {
                        achievement.setDescription(AchievementDescription.FIND_MINIGAME_SPOTS.getDescriptionWithRequiredAmount(requiredAmountForAchievement));
                        achievement.setAmountRequired(requiredAmountForAchievement);
                    }
                }
            }
            // case 3: create achievements from scratch
            else {
                course.addAchievement(
                        new Achievement(
                                AchievementTitle.MINIGAME_ACHIEVER,
                                AchievementDescription.COMPLETE_MINIGAMES.getDescriptionWithRequiredAmount(requiredAmountForAchievement),
                                AchievementImage.STAR_IMAGE.getImageName(),
                                requiredAmountForAchievement,
                                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE),
                                course
                        )
                );
                course.addAchievement(
                        new Achievement(
                                AchievementTitle.MINIGAME_SPOTS_FINDER,
                                AchievementDescription.FIND_MINIGAME_SPOTS.getDescriptionWithRequiredAmount(requiredAmountForAchievement),
                                AchievementImage.MINIGAME_SPOT_IMAGE.getImageName(),
                                requiredAmountForAchievement,
                                List.of(AchievementCategory.EXPLORING),
                                course
                        )
                );
            }
        }
        courseRepository.save(course);

        // update player achievement statistics
        updatePlayerStatisticAchievements(course, course.getCourseAchievements().stream().filter(achievement ->
                achievement.getAchievementTitle().name().equals("MINIGAME_ACHIEVER") ||
                        achievement.getAchievementTitle().name().equals("MINIGAME_SPOTS_FINDER")
        ).collect(Collectors.toList()));
    }

    /**
     * Creates 1 achievement for level up if the world 2 is active and 3 achievements for 3 worlds, if they are active.
     * This function check if the necessary world is active and creates achievements based on it.
     *
     * @param courseAchievements The list of achievements to which the newly created achievements will be added.
     */
    private void createOpenerAndLevelUpAchievements(List<Achievement> courseAchievements, Course course) {
        String achievementTitle = "OPENER_WORLD_";
        for (int i = 2; i <= 4; i++) {
            if (isWorldActive(i, course.getId())) {
                courseAchievements.add(
                        new Achievement(
                                AchievementTitle.valueOf(achievementTitle + i),
                                AchievementDescription.UNLOCK_WORLD.getDescriptionWithRequiredAmount(i),
                                AchievementImage.GLASS_IMAGE.getImageName(),
                                1,
                                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING),
                                course
                        )
                );

                if (i == 2) {
                    courseAchievements.add(
                            new Achievement(
                                    AchievementTitle.LEVEL_UP,
                                    AchievementDescription.LEVEL_UP.getDescriptionWithRequiredAmount(i),
                                    AchievementImage.LEVEL_UP_IMAGE.getImageName(),
                                    i,
                                    List.of(AchievementCategory.ACHIEVING),
                                    course
                            )
                    );
                }
            }
        }
    }

    /**
     * Updates all opener and level up achievements of a course and the achievement statistics of the course members.
     *
     * @param course course whose achievements are updated
     */
    public void updateOpenerAndLevelUpAchievements(final Course course) {
        List<Achievement> courseAchievements = new ArrayList<>(course.getCourseAchievements());
        String achievementTitlePrefix = "OPENER_WORLD_";

        for (int i = 2; i <= 4; i++) {
            // get level up and opener achievements
            List<Achievement> achievements = courseAchievements.stream().filter(achievement ->
                    achievement.getAchievementTitle().name().startsWith(achievementTitlePrefix) ||
                            achievement.getAchievementTitle().name().equals("LEVEL_UP")
            ).toList();

            // case 1: no achievements required -> delete existing ones if there are any
            if (!isWorldActive(i, course.getId())) {
                achievements.forEach(course::removeAchievement);
            } else {
                // case 2: update existent achievements
                if (!achievements.isEmpty()) {
                    for (Achievement achievement : achievements) {
                        if (achievement.getAchievementTitle().name().startsWith(achievementTitlePrefix)) {
                            achievement.setDescription(AchievementDescription.UNLOCK_WORLD.getDescriptionWithRequiredAmount(i));
                            achievement.setAmountRequired(1);
                        } else {
                            achievement.setDescription(AchievementDescription.LEVEL_UP.getDescriptionWithRequiredAmount(i));
                            achievement.setAmountRequired(i);
                        }
                    }
                }
                // case 3: create achievements from scratch
                else {
                    course.addAchievement(
                            new Achievement(
                                    AchievementTitle.valueOf(achievementTitlePrefix + i),
                                    AchievementDescription.UNLOCK_WORLD.getDescriptionWithRequiredAmount(i),
                                    AchievementImage.GLASS_IMAGE.getImageName(),
                                    1,
                                    Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING),
                                    course
                            )
                    );
                    if (i == 2) {
                        course.addAchievement(
                                new Achievement(
                                        AchievementTitle.LEVEL_UP,
                                        AchievementDescription.LEVEL_UP.getDescriptionWithRequiredAmount(i),
                                        AchievementImage.LEVEL_UP_IMAGE.getImageName(),
                                        i,
                                        List.of(AchievementCategory.ACHIEVING),
                                        course
                                )
                        );
                    }
                }
            }
            courseRepository.save(course);
        }
        // update player achievement statistics
        updatePlayerStatisticAchievements(course, course.getCourseAchievements().stream().filter(achievement ->
                achievement.getAchievementTitle().name().startsWith(achievementTitlePrefix) ||
                        achievement.getAchievementTitle().name().equals("LEVEL_UP")
        ).collect(Collectors.toList()));
    }

    /**
     * Creates achievement for each type of minigames, that were created in course, based on their name and active
     * worlds, in which these minigames were created.
     *
     * @param courseAchievements The list of achievements to which the newly created achievements will be added.
     */
    private void createAchievementForEachMinigame(List<Achievement> courseAchievements, Course course) {
        List<String> minigameNames = getMinigameNames(course.getId());
        minigameNames.forEach(minigameName -> {
            if (!minigameName.equalsIgnoreCase("NONE") && !minigameName.equalsIgnoreCase("REGEXGAME")) {
                courseAchievements.add(
                        new Achievement(
                                AchievementTitle.valueOf(minigameName.toUpperCase() + "_MASTER"),
                                AchievementDescription.valueOf(minigameName.toUpperCase()).getDescription(),
                                AchievementImage.valueOf(minigameName.toUpperCase() + "_IMAGE").getImageName(),
                                1,
                                Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE),
                                course
                        )
                );
            }
        });
    }

    /**
     * Updates all minigame related achievements of a course and the achievement statistics of the course members.
     *
     * @param course course whose achievements are updated
     */
    public void updateAchievementForEachMinigame(final Course course) {
        List<Achievement> courseAchievements = new ArrayList<>(course.getCourseAchievements());
        List<String> minigameNames = getMinigameNames(course.getId());

        for (String minigameName : minigameNames) {
            // get minigame achievement
            Achievement achievement = courseAchievements.stream().filter(a ->
                    a.getAchievementTitle().name().startsWith(minigameName)).findFirst().orElse(null);

            // case 1: no achievement required -> delete existing one if there is one
            if (minigameName.equalsIgnoreCase("NONE") || minigameName.equalsIgnoreCase("REGEXGAME")) {
                course.removeAchievement(achievement);
            } else {
                // case 2: update existent achievement
                if (achievement != null) {
                    achievement.setDescription(AchievementDescription.valueOf(minigameName.toUpperCase()).getDescription());
                    achievement.setAmountRequired(1);
                }
                // case 3: create achievement from scratch
                else {
                    course.addAchievement(
                            new Achievement(
                                    AchievementTitle.valueOf(minigameName.toUpperCase() + "_MASTER"),
                                    AchievementDescription.valueOf(minigameName.toUpperCase()).getDescription(),
                                    AchievementImage.valueOf(minigameName.toUpperCase() + "_IMAGE").getImageName(),
                                    1,
                                    Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE),
                                    course
                            )
                    );
                }
            }
            courseRepository.save(course);
        }
        // update player achievement statistics
        updatePlayerStatisticAchievements(course, course.getCourseAchievements().stream().filter(achievement ->
                minigameNames.stream().anyMatch(name -> achievement.getAchievementTitle().name().startsWith(name))).toList());
    }

    /**
     * Creates all static achievements, that are independent of course settings and do not require conditions to be met
     * for the achievement to be created
     *
     * @param courseAchievements The list of achievements to which the newly created achievements will be added.
     */
    private void createAllOtherAchievements(List<Achievement> courseAchievements, Course course) {
        courseAchievements.addAll(Arrays.asList(
                new Achievement(
                        AchievementTitle.GO_FOR_A_WALK,
                        AchievementDescription.WALK_TILES.getDescriptionWithRequiredAmount(10),
                        AchievementImage.FOOT_IMAGE.getImageName(),
                        10,
                        Arrays.asList(AchievementCategory.EXPLORING, AchievementCategory.ACHIEVING),
                        course
                ),
                new Achievement(
                        AchievementTitle.GO_FOR_A_LONGER_WALK,
                        AchievementDescription.WALK_TILES.getDescriptionWithRequiredAmount(1000),
                        AchievementImage.FOOT_IMAGE.getImageName(),
                        1000,
                        Arrays.asList(AchievementCategory.EXPLORING, AchievementCategory.ACHIEVING),
                        course
                ),
                new Achievement(
                        AchievementTitle.SELECT_CHARACTER,
                        AchievementDescription.CHANGE_SKIN.getDescription(),
                        AchievementImage.NPC_IMAGE.getImageName(),
                        1,
                        List.of(AchievementCategory.EXPLORING),
                        course
                ),
                new Achievement(
                        AchievementTitle.BEGINNER,
                        AchievementDescription.PLAY_MINUTES.getDescriptionWithRequiredAmount(30),
                        AchievementImage.CLOCK_IMAGE.getImageName(),
                        30,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING, AchievementCategory.SOCIALIZING, AchievementCategory.COMPETITIVE),
                        course
                ),
                new Achievement(
                        AchievementTitle.EXPERIENCED_PLAYER,
                        AchievementDescription.PLAY_MINUTES.getDescriptionWithRequiredAmount(90),
                        AchievementImage.CLOCK_IMAGE.getImageName(),
                        90,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING, AchievementCategory.SOCIALIZING, AchievementCategory.COMPETITIVE),
                        course
                ),
                new Achievement(
                        AchievementTitle.SPEEDRUNNER,
                        AchievementDescription.USE_SPRINT.getDescriptionWithRequiredAmount(30),
                        AchievementImage.ROCKET_IMAGE.getImageName(),
                        30,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.COMPETITIVE),
                        course
                ),
                new Achievement(
                        AchievementTitle.TELEPORTER_BEGINNER,
                        AchievementDescription.OPEN_TELEPORTERS.getDescriptionWithRequiredAmount(4),
                        AchievementImage.TELEPORTER_IMAGE.getImageName(),
                        4,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING),
                        course
                ),
                new Achievement(
                        AchievementTitle.TELEPORTER_PROFESSIONAL,
                        AchievementDescription.OPEN_TELEPORTERS.getDescriptionWithRequiredAmount(10),
                        AchievementImage.TELEPORTER_IMAGE.getImageName(),
                        10,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING),
                        course
                ),
                new Achievement(
                        AchievementTitle.TRAVELER,
                        AchievementDescription.USE_UFO.getDescriptionWithRequiredAmount(3),
                        AchievementImage.UFO_IMAGE.getImageName(),
                        3,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING),
                        course
                ),
                new Achievement(
                        AchievementTitle.GAMER,
                        AchievementDescription.LOGIN.getDescriptionWithRequiredAmount(2),
                        AchievementImage.CALENDER_IMAGE.getImageName(),
                        2,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING, AchievementCategory.SOCIALIZING, AchievementCategory.COMPETITIVE),
                        course
                ),
                new Achievement(
                        AchievementTitle.PROFESSIONAL_GAMER,
                        AchievementDescription.LOGIN.getDescriptionWithRequiredAmount(5),
                        AchievementImage.CALENDER_IMAGE.getImageName(),
                        5,
                        Arrays.asList(AchievementCategory.ACHIEVING, AchievementCategory.EXPLORING, AchievementCategory.SOCIALIZING, AchievementCategory.COMPETITIVE),
                        course
                ),
                new Achievement(
                        AchievementTitle.GOAT,
                        AchievementDescription.LEADEROARD_1.getDescription(),
                        AchievementImage.MEDAL_1_IMAGE.getImageName(),
                        1,
                        List.of(AchievementCategory.COMPETITIVE),
                        course
                ),
                new Achievement(
                        AchievementTitle.ONE_OF_THE_BEST_PLAYERS,
                        AchievementDescription.LEADEROARD_2_3.getDescription(),
                        AchievementImage.MEDAL_3_IMAGE.getImageName(),
                        1,
                        List.of(AchievementCategory.COMPETITIVE),
                        course
                ),
                new Achievement(
                        AchievementTitle.GET_COINS,
                        AchievementDescription.GET_COINS.getDescriptionWithRequiredAmount(50),
                        AchievementImage.COIN_IMAGE.getImageName(),
                        50,
                        List.of(AchievementCategory.ACHIEVING),
                        course
                ),
                new Achievement(
                        AchievementTitle.GET_MORE_COINS,
                        AchievementDescription.GET_COINS.getDescriptionWithRequiredAmount(150),
                        AchievementImage.COIN_IMAGE.getImageName(),
                        150,
                        List.of(AchievementCategory.ACHIEVING),
                        course
                )
        ));
    }

    /**
     * Creates all achievements statistics, if non-existent, of all players in a course.
     *
     * @param course course to create statistics for
     */
    private void updateAllOtherAchievements(final Course course) {
        List<Achievement> courseAchievements = course.getCourseAchievements();

        for (Player player : playerRepository.findAll()) {
            for (Achievement achievement : courseAchievements) {
                if (player.getAchievementStatistics().stream().noneMatch(stat -> stat.getAchievement().equals(achievement))) {
                    player.getAchievementStatistics().add(
                            new AchievementStatistic(
                                    player,
                                    course.getId(),
                                    achievement
                            )
                    );
                }
            }
            playerRepository.save(player);
        }
    }

    /**
     * Gets amount of books from the specified course and world, checks if the world is active and filters books based
     * on whether they have a description or text.
     *
     * @param worldId The ID of the world from which the book count is to be retrieved, based on whether the book has a
     *                description or text.
     * @return The total number of books in the specified world that have a description and text, or 0 if the world is
     * not active.
     */
    private int getBookCount(int worldId, int courseId) {
        World world = getWorldByIndexFromCourse(courseId, worldId);

        if (isWorldActive(worldId, courseId) || worldId == 1) {
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
     */
    private int getNpcCount(int worldId, int courseId) {
        World world = getWorldByIndexFromCourse(courseId, worldId);

        if (isWorldActive(worldId, courseId) || worldId == 1) {
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
    private boolean isWorldActive(int worldId, int courseId) {
        return getWorldByIndexFromCourse(courseId, worldId).isActive();
    }

    /**
     * Gets the count of active dungeons in the specified course and world.
     *
     * @param worldId The ID of the world from which the count of active dungeons is to be retrieved.
     * @return The number of active dungeons in the specified world.
     */
    private int getActiveDungeonCount(int worldId, int courseId) {
        World world = getWorldByIndexFromCourse(courseId, worldId);

        float countOfActiveDungeonsInWorld = world.getDungeons().stream()
                .filter(Area::isActive)
                .count();
        return (int) countOfActiveDungeonsInWorld;
    }

    /**
     * Gets the count of minigames in the specified course and world, considering only active worlds.
     *
     * @param worldId The ID of the world from which the count of minigames is to be retrieved.
     * @return The number of minigames in the specified world that were created, or 0 if the world is not active.
     */
    private int getMinigameCount(int worldId, int courseId) {
        World world = getWorldByIndexFromCourse(courseId, worldId);
        if (isWorldActive(worldId, courseId) || worldId == 1) {
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
        Set<World> allWorlds = getAllWorldsFromCourse(courseId);

        List<World> activeWorlds = allWorlds.stream()
                .filter(world -> world.isActive() || world.getIndex() == 1)
                .toList();

        return activeWorlds.stream()
                .flatMap(world -> world.getMinigameTasks().stream())
                .filter(minigame -> minigame.getGame() != null)
                .map(minigame -> minigame.getGame().name())
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Get a world of a course
     *
     * @throws ResponseStatusException (404) if world with its static name could not be found in the course
     * @param courseId the id of the course the world is part of
     * @param worldIndex the index of the world searching for
     * @return the found world object
     */
    public World getWorldByIndexFromCourse(final int courseId, final int worldIndex) {
        return worldRepository
                .findByIndexAndCourseId(worldIndex, courseId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format(
                                        "There is no world with static name %s in the course with ID %s.",
                                        worldIndex,
                                        courseId
                                )
                        )
                );
    }

    /**
     * Returns all worlds of a course
     * @param courseId id of the course
     * @return Set of Worlds of the course
     */
    public Set<World> getAllWorldsFromCourse(final int courseId) {
        return worldRepository.findAllByCourseId(courseId);
    }
}