package de.unistuttgart.overworldbackend.service;

import de.unistuttgart.overworldbackend.data.Area;
import de.unistuttgart.overworldbackend.data.Dungeon;
import de.unistuttgart.overworldbackend.data.PlayerStatistic;
import de.unistuttgart.overworldbackend.data.PlayerTaskStatistic;
import de.unistuttgart.overworldbackend.data.comparator.AreaComparator;
import de.unistuttgart.overworldbackend.data.statistics.ActivePlayersPlaytime;
import de.unistuttgart.overworldbackend.data.statistics.CompletedMinigames;
import de.unistuttgart.overworldbackend.data.statistics.PlayerJoinedStatistic;
import de.unistuttgart.overworldbackend.data.statistics.UnlockedAreaAmount;
import de.unistuttgart.overworldbackend.repositories.PlayerStatisticRepository;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
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
     * @param CourseId id of the course
     * @return player joined statistic
     */
    public PlayerJoinedStatistic getPlayerJoinedStatistic(final int CourseId) {
        final PlayerJoinedStatistic playerJoinedStatistic = new PlayerJoinedStatistic();
        playerStatisticRepository
            .findByCourseId(CourseId)
            .forEach(playerStatistic -> addPlayer(playerStatistic, playerJoinedStatistic));
        return playerJoinedStatistic;
    }

    /**
     * Inoperative method.
     * @param courseId
     * @return
     */
    public Set<ActivePlayersPlaytime> getActivePlayersPlaytime(final int courseId) {
        return null;
    }

    /**
     * Get amount of players that unlocked an area
     * @param courseId id of the course
     * @return set of unlocked areas amount
     */
    public Set<UnlockedAreaAmount> getUnlockedAreas(final int courseId) {
        final Set<UnlockedAreaAmount> unlockedAreaAmounts = new HashSet<>();
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
        return unlockedAreaAmounts;
    }

    /**
     * Get the distribution of players with a specific amount of completed minigames
     * @param courseId id of the course
     * @return set of completed minigames with the amount of players that completed this amount of minigames
     */
    public Set<CompletedMinigames> getCompletedMinigames(final int courseId) {
        final Set<CompletedMinigames> completedMinigames = new HashSet<>();
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

    private static void addPlayer(
        final PlayerStatistic playerStatistic,
        final PlayerJoinedStatistic playerJoinedStatistic
    ) {
        playerJoinedStatistic
            .getJoined()
            .stream()
            .filter(playerJoined ->
                isSameDay(dateToCalendar(playerJoined.getDate()), dateToCalendar(playerStatistic.getDate()))
            )
            .findFirst()
            .ifPresent(playerJoined -> playerJoined.setPlayers(playerJoined.getPlayers() + 1));
    }

    private static boolean isSameDay(final Calendar cal1, final Calendar cal2) {
        return (
            cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
        );
    }

    private static Calendar dateToCalendar(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
}
