package de.unistuttgart.overworldbackend.service;

import de.unistuttgart.overworldbackend.data.*;
import de.unistuttgart.overworldbackend.data.mapper.PlayerTaskStatisticMapper;
import de.unistuttgart.overworldbackend.repositories.MinigameTaskRepository;
import de.unistuttgart.overworldbackend.repositories.PlayerStatisticRepository;
import de.unistuttgart.overworldbackend.repositories.PlayerTaskActionLogRepository;
import de.unistuttgart.overworldbackend.repositories.PlayerTaskStatisticRepository;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class PlayerTaskStatisticService {

    public static final long COMPLETED_SCORE = 50;
    private static final long MAX_KNOWLEDGE = 100;
    private static final double RETRY_KNOWLEDGE = 0.02;

    @Autowired
    MinigameTaskService minigameTaskService;

    @Autowired
    PlayerStatisticService playerStatisticService;

    @Autowired
    PlayerTaskStatisticMapper playerTaskStatisticMapper;

    @Autowired
    PlayerTaskStatisticRepository playerTaskStatisticRepository;

    @Autowired
    MinigameTaskRepository minigameTaskRepository;

    @Autowired
    PlayerStatisticRepository playerstatisticRepository;

    @Autowired
    PlayerTaskActionLogRepository playerTaskActionLogRepository;

    @Autowired
    PlayerService playerService;

    private List<PlayerDTO> allPlayerStatistics;

    /**
     * Gets a list of all playerTaskStatistics of a player of the given course
     *
     * @param courseId course which the statistics belong to
     * @param playerId player which the statistics belong to
     * @return List of playerTaskStatisticDTOs of the player of the course
     */
    public List<PlayerTaskStatisticDTO> getAllStatisticsOfPlayer(final int courseId, final String playerId) {
        final List<PlayerTaskStatistic> statisticList = playerTaskStatisticRepository
            .findByCourseId(courseId)
            .parallelStream()
            .filter(playerTaskStatistic -> playerTaskStatistic.getPlayerStatistic().getUserId().equals(playerId))
            .toList();
        return playerTaskStatisticMapper.playerTaskStatisticsToPlayerTaskStatisticDTO(statisticList);
    }

    /**
     * Gets the playerStatistic of the course of the player of the statisticId
     *
     * @param courseId    course which the statistic belongs to
     * @param playerId    player which the statistic belongs to
     * @param statisticId id of the statistic, which is returned
     * @return playerTaskStatistic with the given statisticId
     */
    public PlayerTaskStatisticDTO getStatisticOfPlayer(
        final int courseId,
        final String playerId,
        final UUID statisticId
    ) {
        return playerTaskStatisticMapper.playerTaskStatisticToPlayerTaskStatisticDTO(
            playerTaskStatisticRepository
                .findById(statisticId)
                .filter(statistic ->
                    statistic.getCourse().getId() == courseId &&
                    statistic.getPlayerStatistic().getUserId().equals(playerId)
                )
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format(
                            "Statistic with the id %s of the course %s of the player %s not found",
                            statisticId,
                            courseId,
                            playerId
                        )
                    )
                )
        );
    }

    /**
     * update PlayerTaskStatistic with the given data
     * This method gets a data object with a Player, a Game, a Configuration and a score.
     * The given data gets logged as a PlayerTaskActionLog.
     * It calculates the progress of the player with the given score and updates the value in the correct PlayerStatistic object.
     *
     * @param data Data of a game run
     * @return updated playerTaskStatistic
     */
    public PlayerTaskStatisticDTO submitData(final PlayerTaskStatisticData data) {
        final MinigameTask minigameTask = minigameTaskRepository
            .findByGameAndConfigurationId(data.getGame(), data.getConfigurationId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Minigame not found"));
        final Course course = minigameTask.getCourse();
        final PlayerStatistic playerStatistic = playerstatisticRepository
            .findByCourseIdAndUserId(course.getId(), data.getUserId())
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("Player %s not found", data.getUserId())
                )
            );
        final PlayerTaskStatistic playerTaskStatistic = playerTaskStatisticRepository
            .findByMinigameTaskIdAndCourseIdAndPlayerStatisticId(
                minigameTask.getId(),
                course.getId(),
                playerStatistic.getId()
            )
            .orElseGet(() -> {
                final PlayerTaskStatistic newPlayerTaskStatistic = new PlayerTaskStatistic();
                newPlayerTaskStatistic.setPlayerStatistic(playerStatistic);
                newPlayerTaskStatistic.setMinigameTask(minigameTask);
                newPlayerTaskStatistic.setCourse(course);

                playerStatistic.addPlayerTaskStatistic(newPlayerTaskStatistic);
                return playerTaskStatisticRepository
                    .findByMinigameTaskIdAndCourseIdAndPlayerStatisticId(
                        minigameTask.getId(),
                        course.getId(),
                        playerStatistic.getId()
                    )
                    .orElseThrow(() ->
                        new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            String.format(
                                "Statistic for the minigame with the id %s of the player %s in the course %s not found.",
                                minigameTask.getId(),
                                playerStatistic.getId(),
                                course.getId()
                            )
                        )
                    );
            });

        final long gainedKnowledge = calculateKnowledge(data.getScore(), playerTaskStatistic.getHighscore());

        playerTaskStatistic.setHighscore(Math.max(playerTaskStatistic.getHighscore(), data.getScore()));
        playerTaskStatistic.setCompleted(playerTaskStatistic.isCompleted() || checkCompleted(data.getScore()));
        playerTaskStatistic.setRewards(data.getRewards());

        logData(data, course, playerTaskStatistic, gainedKnowledge);

        final Area area = minigameTask.getArea();
        if (area instanceof Dungeon dungeon) {
            calculateCompletedDungeon(dungeon, playerStatistic);
        }

        playerStatisticService.checkForUnlockedAreas(minigameTask.getArea(), playerStatistic);

        playerStatistic.addRewards(data.getRewards());
        playerStatistic.addCredit(data.getRewards());
        playerStatistic.addKnowledge(gainedKnowledge);

        playerstatisticRepository.save(playerStatistic);

        return playerTaskStatisticMapper.playerTaskStatisticToPlayerTaskStatisticDTO(
            playerTaskStatisticRepository.save(playerTaskStatistic)
        );
    }

    private void calculateCompletedDungeon(final Dungeon dungeon, final PlayerStatistic playerStatistic) {
        final List<PlayerTaskStatistic> playerTaskStatistics = playerTaskStatisticRepository.findByPlayerStatisticId(
            playerStatistic.getId()
        );
        final boolean dungeonCompleted = dungeon
            .getMinigameTasks()
            .parallelStream()
            .allMatch(minigameTask ->
                playerTaskStatistics
                    .parallelStream()
                    .filter(playerTaskStatistic -> playerTaskStatistic.getMinigameTask().equals(minigameTask))
                    .anyMatch(PlayerTaskStatistic::isCompleted)
            );
        if (dungeonCompleted) {
            final List<Area> completedDungeons = playerStatistic.getCompletedDungeons();
            completedDungeons.add(dungeon);
        }
    }

    private void logData(
        final PlayerTaskStatisticData data,
        final Course course,
        final PlayerTaskStatistic currentPlayerTaskStatistic,
        final long gainedKnowledge
    ) {
        final PlayerTaskActionLog actionLog = new PlayerTaskActionLog();
        actionLog.setPlayerTaskStatistic(currentPlayerTaskStatistic);
        actionLog.setCourse(course);
        actionLog.setScore(data.getScore());
        actionLog.setCurrentHighscore(currentPlayerTaskStatistic.getHighscore());
        actionLog.setGainedKnowledge(gainedKnowledge);
        actionLog.setGame(data.getGame());
        actionLog.setConfigurationId(data.getConfigurationId());
        currentPlayerTaskStatistic.addActionLog(actionLog);
    }

    /**
     * This method calculates the knowledge out of the score and the highscore.
     * <p>
     * You get all the points you gained above the high score.
     * The points below the high score get multiplied by the RETRY_KNOWLEDGE constant.
     *
     * @param score     score achieved in the game
     * @param highscore high score achieved in the game
     * @return knowledge to be added
     */
    private long calculateKnowledge(final long score, final long highscore) {
        return (long) (
            MAX_KNOWLEDGE *
            (double) Math.max(0, score - highscore) /
            100 +
            MAX_KNOWLEDGE *
            RETRY_KNOWLEDGE *
            Math.max(0, score - Math.max(0, score - highscore)) /
            100
        );
    }

    private boolean checkCompleted(final long score) {
        return score >= COMPLETED_SCORE;
    }
}
