package de.unistuttgart.overworldbackend.service;

import de.unistuttgart.overworldbackend.data.*;
import de.unistuttgart.overworldbackend.data.enums.Minigame;
import de.unistuttgart.overworldbackend.data.mapper.AreaLocationMapper;
import de.unistuttgart.overworldbackend.data.mapper.PlayerStatisticMapper;
import de.unistuttgart.overworldbackend.repositories.PlayerStatisticRepository;
import de.unistuttgart.overworldbackend.repositories.PlayerTaskStatisticRepository;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class PlayerStatisticService {

    @Autowired
    private CourseService courseService;

    @Autowired
    private PlayerStatisticRepository playerstatisticRepository;

    @Autowired
    private PlayerTaskStatisticRepository playerTaskStatisticRepository;

    @Autowired
    private PlayerStatisticMapper playerstatisticMapper;

    @Autowired
    private AreaLocationMapper areaLocationMapper;

    @Autowired
    private AreaService areaService;

    @Autowired
    private WorldService worldService;

    /**
     * get statistics from a player course
     *
     * @param courseId the id of the course
     * @param userId   the playerId of the player searching for
     * @return the found playerstatistic
     * @throws ResponseStatusException (404) when playerstatistic with courseId and userId could not be found
     */
    public PlayerStatistic getPlayerStatisticFromCourse(final int courseId, final String userId) {
        return playerstatisticRepository
            .findByCourseIdAndUserId(courseId, userId)
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format(
                        "There is no playerstatistic from player with userId %s in course with id %s.",
                        userId,
                        courseId
                    )
                )
            );
    }

    /**
     * get statistics from a player course
     *
     * @param courseId the id of the course
     * @param userId   the playerId of the player searching for
     * @return the found playerstatistic
     * @throws ResponseStatusException (404) when playerstatistic with courseId and userId could not be found
     */
    public Set<PlayerStatistic> getPlayerStatisticsFromCourse(final int courseId) {
        return playerstatisticRepository.findByCourseId(courseId);
    }

    /**
     * Create a playerstatistic with initial data in a course.
     *
     * @param courseId the id of the course where the playerstatistic will be created
     * @param playerRegistrationDTO   the player with its userId and username
     * @return the created playerstatistic as DTO
     * @throws ResponseStatusException (404) when course with its id does not exist
     *                                 (400) when a player with the playerId already has a playerstatistic
     */
    public PlayerStatisticDTO createPlayerStatisticInCourse(
        final int courseId,
        final PlayerRegistrationDTO playerRegistrationDTO
    ) {
        final Optional<PlayerStatistic> existingPlayerstatistic = playerstatisticRepository.findByCourseIdAndUserId(
            courseId,
            playerRegistrationDTO.getUserId()
        );
        if (existingPlayerstatistic.isPresent()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                String.format(
                    "There is already a playerstatistic for userId %s in course %s",
                    playerRegistrationDTO.getUserId(),
                    courseId
                )
            );
        }
        final Course course = courseService.getCourse(courseId);
        final World firstWorld = getFirstWorld(courseId);

        final PlayerStatistic playerstatistic = new PlayerStatistic();
        playerstatistic.setCourse(course);
        playerstatistic.setCompletedDungeons(new ArrayList<>());
        playerstatistic.setUnlockedTeleporters(new HashSet<>());
        final List<Area> unlockedAreas = new ArrayList<>();
        unlockedAreas.add(firstWorld);

        playerstatistic.setUnlockedAreas(unlockedAreas);
        playerstatistic.setUserId(playerRegistrationDTO.getUserId());
        playerstatistic.setUsername(playerRegistrationDTO.getUsername());
        playerstatistic.setCurrentArea(firstWorld);
        playerstatistic.setKnowledge(0);
        course.addPlayerStatistic(playerstatistic);
        final PlayerStatistic savedPlayerStatistic = getPlayerStatisticFromCourse(
            courseId,
            playerRegistrationDTO.getUserId()
        );
        return playerstatisticMapper.playerStatisticToPlayerstatisticDTO(savedPlayerStatistic);
    }

    /**
     * Unlock a teleporter for a player.
     *
     *
     * @param playerTeleporterData the data of the teleporter to be added
     * @throws ResponseStatusException (404) when playerstatistic with courseId and userId could not be found or teleporter with id not found
     *                                 (400) when teleporter is already unlocked
     * @return the updated player statistic
     */
    public PlayerStatisticDTO addUnlockedTeleporter(
        final int courseId,
        final PlayerTeleporterData playerTeleporterData
    ) {
        final PlayerStatistic playerStatistic = getPlayerStatisticFromCourse(
            courseId,
            playerTeleporterData.getUserId()
        );
        playerStatistic
            .getUnlockedTeleporters()
            .stream()
            .filter(teleporter ->
                teleporter.getIndex() == playerTeleporterData.getIndex() &&
                areaLocationMapper.areaToAreaLocationDTO(teleporter.getArea()).equals(playerTeleporterData.getArea())
            )
            .findAny()
            .ifPresent(teleporter -> {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format(
                        "Teleporter with index %s in area with id %s is already unlocked.",
                        playerTeleporterData.getIndex(),
                        playerTeleporterData.getArea()
                    )
                );
            });
        playerStatistic.addUnlockedTeleporter(
            new Teleporter(
                playerTeleporterData.getIndex(),
                areaService.getAreaFromAreaLocationDTO(courseId, playerTeleporterData.getArea()),
                courseService.getCourse(courseId)
            )
        );
        return playerstatisticMapper.playerStatisticToPlayerstatisticDTO(
            playerstatisticRepository.save(playerStatistic)
        );
    }

    /**
     * Update a playerstatistic.
     * <p>
     * Only the currentArea is updatable.
     *
     * @param courseId           the id of the course where the playerstatistic will be created
     * @param playerId           the playerId of the player
     * @param playerstatisticDTO the updated parameters
     * @return the updated playerstatistic
     * @throws ResponseStatusException (404) when playerstatistic with its playerId in the course does not exist
     *                                 (400) when combination of world and dungeon in currentLocation does not exist
     */
    public PlayerStatisticDTO updatePlayerStatisticInCourse(
        final int courseId,
        final String playerId,
        final PlayerStatisticDTO playerstatisticDTO
    ) {
        final PlayerStatistic playerstatistic = getPlayerStatisticFromCourse(courseId, playerId);

        if (playerstatisticDTO.getCurrentArea() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current area location is not specified");
        }

        try {
            playerstatistic.setCurrentArea(
                areaService.getAreaFromAreaLocationDTO(courseId, playerstatisticDTO.getCurrentArea())
            );
        } catch (final ResponseStatusException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Specified area does not exist");
        }
        return playerstatisticMapper.playerStatisticToPlayerstatisticDTO(
            (playerstatisticRepository.save(playerstatistic))
        );
    }

    /**
     * Check if a new area is unlocked.
     * Adds next area to unlockedAreas if player fulfilled the requirements.
     *
     * @param currentArea     the area to check where the tasks may be finished
     * @param playerStatistic player statistics of the current player
     */
    public void checkForUnlockedAreas(final Area currentArea, final PlayerStatistic playerStatistic) {
        final List<PlayerTaskStatistic> playerTaskStatistics = playerTaskStatisticRepository.findByPlayerStatisticId(
            playerStatistic.getId()
        );

        final int courseId = playerStatistic.getCourse().getId();

        if (isAreaCompleted(currentArea, playerTaskStatistics)) {
            if (currentArea instanceof World currentWorld) {
                //if world -> unlock next dungeon or if not possible next world
                currentWorld
                    .getDungeons()
                    .parallelStream()
                    .min(Comparator.comparingInt(Area::getIndex))
                    .filter(Dungeon::isConfigured)
                    .ifPresentOrElse(
                        playerStatistic::addUnlockedArea,
                        () ->
                            worldService
                                .getOptionalWorldByIndexFromCourse(currentWorld.getIndex() + 1, courseId)
                                .filter(Area::isConfigured)
                                .ifPresent(playerStatistic::addUnlockedArea)
                    );
            } else if (currentArea instanceof Dungeon currentDungeon) {
                //if dungeon -> unlock next dungeon or if not possible next world
                currentDungeon
                    .getWorld()
                    .getDungeons()
                    .parallelStream()
                    .filter(dungeon -> dungeon.getIndex() > currentDungeon.getIndex())
                    .min(Comparator.comparingInt(Dungeon::getIndex))
                    .filter(Dungeon::isConfigured)
                    .ifPresentOrElse(
                        playerStatistic::addUnlockedArea,
                        () ->
                            worldService
                                .getOptionalWorldByIndexFromCourse(currentDungeon.getWorld().getIndex() + 1, courseId)
                                .filter(Area::isConfigured)
                                .ifPresent(playerStatistic::addUnlockedArea)
                    );
            }
        }
    }

    /**
     * Check if area is completed.
     *
     * @param area                 the area to check if its completed
     * @param playerTaskStatistics player statistics of the current player
     * @return boolean if the area is completed
     */
    private boolean isAreaCompleted(final Area area, final List<PlayerTaskStatistic> playerTaskStatistics) {
        return area
            .getMinigameTasks()
            .parallelStream()
            .filter(minigameTask -> Minigame.isConfigured(minigameTask.getGame()))
            .allMatch(minigameTask ->
                playerTaskStatistics
                    .parallelStream()
                    .filter(playerTaskStatistic -> playerTaskStatistic.getMinigameTask().equals(minigameTask))
                    .anyMatch(PlayerTaskStatistic::isCompleted)
            );
    }

    private World getFirstWorld(final int courseId) {
        return worldService.getWorldByIndexFromCourse(courseId, 1);
    }
}
