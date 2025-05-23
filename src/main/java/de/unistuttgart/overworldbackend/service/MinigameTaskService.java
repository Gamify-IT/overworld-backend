package de.unistuttgart.overworldbackend.service;

import de.unistuttgart.overworldbackend.data.Area;
import de.unistuttgart.overworldbackend.data.MinigameTask;
import de.unistuttgart.overworldbackend.data.MinigameTaskDTO;
import de.unistuttgart.overworldbackend.data.PlayerStatistic;
import de.unistuttgart.overworldbackend.data.comparator.AreaComparator;
import de.unistuttgart.overworldbackend.data.enums.Minigame;
import de.unistuttgart.overworldbackend.data.mapper.MinigameTaskMapper;
import de.unistuttgart.overworldbackend.repositories.CourseRepository;
import de.unistuttgart.overworldbackend.repositories.MinigameTaskRepository;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class MinigameTaskService {

    @Autowired
    private MinigameTaskRepository minigameTaskRepository;

    @Autowired
    private AreaService areaService;

    @Autowired
    private WorldService worldService;

    @Autowired
    private DungeonService dungeonService;

    @Autowired
    private MinigameTaskMapper minigameTaskMapper;

    @Autowired
    private PlayerStatisticService playerStatisticService;

    @Autowired
    private AchievementService achievementService;

    @Autowired
    private CourseRepository courseRepository;

    /**
     * Get a minigame task of an area
     *
     * @throws ResponseStatusException (404) if area or task with its id could not be found in the course
     * @param courseId the id of the course the minigame task is part of
     * @param worldIndex the index of the world the task is part of
     * @param dungeonIndex the index of the dungeon the task is part of
     * @param taskIndex the index of the task searching for
     * @return the found task object
     */
    public MinigameTask getMinigameTaskFromAreaOrThrowNotFound(
        final int courseId,
        final int worldIndex,
        final Optional<Integer> dungeonIndex,
        final int taskIndex
    ) {
        return areaService
            .getAreaFromIndex(courseId, worldIndex, dungeonIndex)
            .getMinigameTasks()
            .parallelStream()
            .filter(minigameTask -> minigameTask.getIndex() == taskIndex)
            .findAny()
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format(
                        "Task not found with index %s in course %s in world %s%s.",
                        taskIndex,
                        courseId,
                        worldIndex,
                        dungeonIndex.map(index -> " and dungeon " + index).orElse("")
                    )
                )
            );
    }

    /**
     * Get a list of minigame tasks of a course and a world
     *
     * @throws ResponseStatusException (404) if course or area with its id do not exist
     * @param courseId the id of the course the minigame tasks should be part of
     * @param worldIndex the index of the world where the minigame tasks should be part of
     * @return a list of minigame tasks as DTO
     */
    public Set<MinigameTaskDTO> getMinigameTasksFromArea(final int courseId, final int worldIndex) {
        return minigameTaskMapper.minigameTasksToMinigameTaskDTOs(
            worldService.getWorldByIndexFromCourse(courseId, worldIndex).getMinigameTasks()
        );
    }

    /**
     * Get a list of minigame tasks of a course and a dungeon
     *
     * @throws ResponseStatusException (404) if course or area with its id do not exist
     * @param courseId the id of the course the minigame tasks should be part of
     * @param worldIndex the index of the world where the minigame tasks should be part of
     * @param dungeonIndex the index of the dungeon where the minigame tasks should be part of
     * @return a list of minigame tasks as DTO
     */
    public Set<MinigameTaskDTO> getMinigameTasksFromArea(
        final int courseId,
        final int worldIndex,
        final int dungeonIndex
    ) {
        return minigameTaskMapper.minigameTasksToMinigameTaskDTOs(
            dungeonService.getDungeonByIndexFromCourse(courseId, worldIndex, dungeonIndex).getMinigameTasks()
        );
    }

    /**
     * Get a minigame task by its index from a course and an area
     *
     * @throws ResponseStatusException (404) if course, area or task by its id do not exist
     * @param courseId the id of the course the minigame task should be part of
     * @param worldIndex the index of the world where the minigame task should be part of
     * @param dungeonIndex the index of the dungen where the minigame task should be part of
     * @param taskIndex the index of the minigame task searching for
     * @return the found minigame task as DTO
     */
    public MinigameTaskDTO getMinigameTaskFromArea(
        final int courseId,
        final int worldIndex,
        final Optional<Integer> dungeonIndex,
        final int taskIndex
    ) {
        final MinigameTask minigameTask = getMinigameTaskFromAreaOrThrowNotFound(
            courseId,
            worldIndex,
            dungeonIndex,
            taskIndex
        );
        return minigameTaskMapper.minigameTaskToMinigameTaskDTO(minigameTask);
    }

    /**
     * Update a minigame task by index id from a course and an area.
     *
     * Only the game and configuration id is updatable.
     *
     * @throws ResponseStatusException (404) if course, world or dungeon by its id do not exist
     * @param courseId the id of the course the minigame task should be part of
     * @param worldIndex the index of the world where the minigame task should be part of
     * @param dungeonIndex the index of the dungeon where the minigame task should be part of
     * @param taskIndex the index of the minigame task that should get updated
     * @param taskDTO the updated parameters
     * @return the updated area as DTO
     */
    public MinigameTaskDTO updateMinigameTaskFromArea(
        final int courseId,
        final int worldIndex,
        final Optional<Integer> dungeonIndex,
        final int taskIndex,
        final MinigameTaskDTO taskDTO
    ) {
        final MinigameTask minigameTask = getMinigameTaskFromAreaOrThrowNotFound(
            courseId,
            worldIndex,
            dungeonIndex,
            taskIndex
        );
        final boolean configuredBefore = Minigame.isConfigured(minigameTask.getGame());
        minigameTask.setGame(taskDTO.getGame());
        minigameTask.setConfigurationId(taskDTO.getConfigurationId());
        minigameTask.setDescription(taskDTO.getDescription());
        final MinigameTask updatedMinigameTask = minigameTaskRepository.save(minigameTask);
        final boolean configuredAfter = Minigame.isConfigured(updatedMinigameTask.getGame());
        if (configuredAfter && !configuredBefore) {
            minigameAdded(courseId, worldIndex, dungeonIndex);
        } else if (!configuredAfter && configuredBefore) {
            minigameRemoved(courseId, worldIndex, dungeonIndex);
        }
        achievementService.updateMinigameAchievements(courseRepository.
                findById(courseId).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("There is no course with id %s.", courseId))
                ));
        achievementService.updateAchievementForEachMinigame(courseRepository.
                findById(courseId).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("There is no course with id %s.", courseId))
                ));
        return minigameTaskMapper.minigameTaskToMinigameTaskDTO(updatedMinigameTask);
    }

    /**
     * If a minigame is added, the configured flag of the area is set
     * @param courseId the id of the course the minigame is in
     * @param worldIndex the index of the world the minigame is in
     * @param dungeonIndex the index of the dungeon the minigame is in
     */
    private void minigameAdded(final int courseId, final int worldIndex, final Optional<Integer> dungeonIndex) {
        final Area area = areaService.getAreaFromIndex(courseId, worldIndex, dungeonIndex);
        if (!area.isConfigured()) {
            area.setConfigured(true);
            unlockAddedArea(area);
        }
    }

    /**
     * unlocks an area for all players which have already unlocked a world or dungeon with a higher index
     * @param area area which is unlocked
     */
    private void unlockAddedArea(final Area area) {
        final Set<PlayerStatistic> playerStatistics = playerStatisticService.getPlayerStatisticsFromCourse(
            area.getCourse().getId()
        );
        playerStatistics.forEach(playerStatistic -> unlockAreaIfProgressFurther(area, playerStatistic));
    }

    /**
     * Unlocks an area if the player has already unlocked a world or dungeon with a higher index
     * @param area Area which might get unlocked
     * @param playerStatistic statistic of the player
     * @throws ResponseStatusException (400) if playerStatistic is not initialized
     */
    private void unlockAreaIfProgressFurther(final Area area, final PlayerStatistic playerStatistic) {
        final AreaComparator areaComparator = new AreaComparator();
        final Area furthestPlayerArea = playerStatistic
            .getUnlockedAreas()
            .stream()
            .max(areaComparator)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "PlayerStatistic not initiallized"));
        if (areaComparator.compare(furthestPlayerArea, area) > 0) {
            playerStatistic.getUnlockedAreas().add(area);
        } else {
            playerStatisticService.checkForUnlockedAreas(furthestPlayerArea, playerStatistic);
        }
    }

    /**
     * If a minigame is removed, it is checked whether the configured flag of the area needs to be changed
     * @param courseId the id of the course the minigame is in
     * @param worldIndex the index of the world the minigame is in
     * @param dungeonIndex the index of the dungeon the minigame is in
     */
    private void minigameRemoved(final int courseId, final int worldIndex, final Optional<Integer> dungeonIndex) {
        final Area area = areaService.getAreaFromIndex(courseId, worldIndex, dungeonIndex);
        final Set<MinigameTask> minigames = area.getMinigameTasks();
        final int amountOfConfiguredMinigames = getAmountOfConfiguredMinigames(minigames);
        if (amountOfConfiguredMinigames <= 0) {
            area.setConfigured(false);
        }
    }

    /**
     * Counts the amount of configured minigames in the given set
     * @param minigames the minigames to check
     * @return the amount of configured minigames
     */
    private int getAmountOfConfiguredMinigames(final Set<MinigameTask> minigames) {
        int amountOfConfiguredMinigames = 0;
        for (final MinigameTask minigame : minigames) {
            if (Minigame.isConfigured(minigame.getGame())) {
                amountOfConfiguredMinigames++;
            }
        }
        return amountOfConfiguredMinigames;
    }
}
