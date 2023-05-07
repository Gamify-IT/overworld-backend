package de.unistuttgart.overworldbackend.service;

import de.unistuttgart.overworldbackend.data.Area;
import de.unistuttgart.overworldbackend.data.Dungeon;
import de.unistuttgart.overworldbackend.data.PlayerStatistic;
import de.unistuttgart.overworldbackend.data.PlayerTaskStatistic;
import de.unistuttgart.overworldbackend.data.comparator.AreaComparator;
import de.unistuttgart.overworldbackend.data.statistics.*;
import de.unistuttgart.overworldbackend.repositories.PlayerStatisticRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseStatisticService {

    @Autowired
    PlayerStatisticRepository playerStatisticRepository;

    /**
     * Get the amount of players that joined at a specific date
     * @param courseId id of the course
     * @return player joined statistic
     */
    public PlayerJoinedStatistic getPlayerJoinedStatistic(final int courseId) {
        final PlayerJoinedStatistic playerJoinedStatistic = new PlayerJoinedStatistic();
        playerStatisticRepository
            .findByCourseId(courseId)
            .forEach(playerStatistic -> addPlayerJoined(playerStatistic, playerJoinedStatistic));
        return playerJoinedStatistic;
    }

    /**
     * Inoperative method.
     * @param courseId id of the course
     * @return last played statistic
     */
    public List<LastPlayed> getActivePlayersPlaytime(final int courseId) {
        final List<LastPlayed> lastPlayed = new ArrayList<>();
        playerStatisticRepository
            .findByCourseId(courseId)
            .forEach(playerStatistic -> addLastPlayed(playerStatistic, lastPlayed));
        lastPlayed.sort(Comparator.comparing(LastPlayed::getLastPlayed));
        return lastPlayed;
    }

    /**
     * Get amount of players that unlocked an area
     * @param courseId id of the course
     * @return set of unlocked areas amount
     */
    public List<UnlockedAreaAmount> getUnlockedAreas(final int courseId) {
        final List<UnlockedAreaAmount> unlockedAreaAmounts = new ArrayList<>();
        playerStatisticRepository
            .findByCourseId(courseId)
            .forEach(playerStatistic ->
                unlockedAreaAmounts
                    .parallelStream()
                    .filter(unlockedAreaAmount ->
                        unlockedAreaAmount.getLevel() == (playerStatistic.getUnlockedAreas().size())
                    )
                    .findFirst()
                    .ifPresentOrElse(
                        unlockedAreaAmount -> unlockedAreaAmount.setPlayers(unlockedAreaAmount.getPlayers() + 1),
                        () -> {
                            final AreaComparator areaComparator = new AreaComparator();
                            final Area maxArea = playerStatistic
                                .getUnlockedAreas()
                                .parallelStream()
                                .max(areaComparator)
                                .orElse(playerStatistic.getCurrentArea());
                            unlockedAreaAmounts.add(
                                new UnlockedAreaAmount(
                                    playerStatistic.getUnlockedAreas().size(),
                                    getAreaName(maxArea),
                                    1
                                )
                            );
                        }
                    )
            );
        unlockedAreaAmounts.sort(Comparator.comparingInt(UnlockedAreaAmount::getLevel));
        return unlockedAreaAmounts;
    }

    /**
     * Get the distribution of players with a specific amount of completed minigames
     * @param courseId id of the course
     * @return set of completed minigames with the amount of players that completed this amount of minigames
     */
    public List<CompletedMinigames> getCompletedMinigames(final int courseId) {
        final List<CompletedMinigames> completedMinigames = new ArrayList<>();
        playerStatisticRepository
            .findByCourseId(courseId)
            .forEach(playerStatistic -> {
                final int amountCompletedMinigames = playerStatistic
                    .getPlayerTaskStatistics()
                    .stream()
                    .filter(PlayerTaskStatistic::isCompleted)
                    .toList()
                    .size();
                completedMinigames
                    .parallelStream()
                    .filter(completedMinigame ->
                        completedMinigame.getAmountOfCompletedMinigames() == amountCompletedMinigames
                    )
                    .findFirst()
                    .ifPresentOrElse(
                        completedMinigame -> completedMinigame.setPlayers(completedMinigame.getPlayers() + 1),
                        () -> completedMinigames.add(new CompletedMinigames(amountCompletedMinigames, 1))
                    );
            });
        completedMinigames.sort(Comparator.comparingInt(CompletedMinigames::getAmountOfCompletedMinigames));
        return completedMinigames;
    }

    private String getAreaName(final Area maxArea) {
        final String name;
        if (maxArea instanceof Dungeon dungeon) {
            name = dungeon.getWorld().getStaticName() + " - " + dungeon.getStaticName();
        } else {
            name = maxArea.getStaticName();
        }
        return name;
    }

    private static void addLastPlayed(final PlayerStatistic playerStatistic, final List<LastPlayed> lastPlayed) {
        lastPlayed
            .parallelStream()
            .filter(lastPlayedStatistic -> isSameDay(lastPlayedStatistic.getLastPlayed(), playerStatistic.getDate()))
            .findFirst()
            .ifPresentOrElse(
                lastPlayedStatistic -> lastPlayedStatistic.setPlayers(lastPlayedStatistic.getPlayers() + 1),
                () -> lastPlayed.add(new LastPlayed(playerStatistic.getDate(), 1))
            );
    }

    private static void addPlayerJoined(
        final PlayerStatistic playerStatistic,
        final PlayerJoinedStatistic playerJoinedStatistic
    ) {
        playerJoinedStatistic
            .getJoined()
            .parallelStream()
            .filter(playerJoined -> isSameDay(playerJoined.getDate(), playerStatistic.getDate()))
            .findFirst()
            .ifPresentOrElse(
                playerJoined -> {
                    playerJoined.setPlayers(playerJoined.getPlayers() + 1);
                    playerJoinedStatistic.addPlayer();
                },
                () -> {
                    playerJoinedStatistic.getJoined().add(new PlayerJoined(playerStatistic.getDate(), 1));
                    playerJoinedStatistic.addPlayer();
                }
            );
    }

    private static boolean isSameDay(final LocalDateTime date1, final LocalDateTime date2) {
        return (
            date1.getDayOfYear() == date2.getDayOfYear() &&
            date1.getYear() == date2.getYear() &&
            date1.getMonthValue() == date2.getMonthValue()
        );
    }
}
