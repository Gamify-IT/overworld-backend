package de.unistuttgart.overworldbackend.service;

import de.unistuttgart.overworldbackend.data.*;
import de.unistuttgart.overworldbackend.data.statistics.*;
import de.unistuttgart.overworldbackend.repositories.PlayerStatisticRepository;
import java.time.Duration;
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

    static final List<Integer> LAST_PLAYED_RANGE = List.of(1, 3, 12, 24, 3 * 24, 7 * 24, 14 * 24);

    @Autowired
    CourseService courseService;

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
    public List<LastPlayed> getLastPlayed(final int courseId) {
        final List<LastPlayed> lastPlayed = new ArrayList<>();
        LAST_PLAYED_RANGE.forEach(range -> lastPlayed.add(new LastPlayed(range, 0)));
        playerStatisticRepository
            .findByCourseId(courseId)
            .forEach(playerStatistic -> addLastPlayed(playerStatistic, lastPlayed));
        lastPlayed.sort(Comparator.comparing(LastPlayed::getHour));
        return lastPlayed;
    }

    /**
     * Get amount of players that unlocked an area
     * @param courseId id of the course
     * @return set of unlocked areas amount
     */
    public List<UnlockedAreaAmount> getUnlockedAreas(final int courseId) {
        final List<UnlockedAreaAmount> unlockedAreaAmounts = createInitialAreaAmount(courseService.getCourse(courseId));
        playerStatisticRepository
            .findByCourseId(courseId)
            .forEach(playerStatistic ->
                playerStatistic
                    .getUnlockedAreas()
                    .forEach(area -> {
                        final String name = getAreaName(area);
                        unlockedAreaAmounts
                            .parallelStream()
                            .filter(unlockedAreaAmount -> unlockedAreaAmount.getName().equals(name))
                            .findFirst()
                            .ifPresent(unlockedArea -> unlockedArea.setPlayers(unlockedArea.getPlayers() + 1));
                    })
            );
        unlockedAreaAmounts.sort(Comparator.comparingInt(UnlockedAreaAmount::getLevel));
        return unlockedAreaAmounts;
    }

    /**
     * Create initial area amount wit
     * @param course course to get the areas from
     * @return initial list of unlocked area amount
     */
    private List<UnlockedAreaAmount> createInitialAreaAmount(final Course course) {
        final List<UnlockedAreaAmount> unlockedAreaAmounts = new ArrayList<>();
        int level = 0;
        for (int i = 0; i < course.getWorlds().size(); i++) {
            final World world = course.getWorlds().get(i);
            if (world.isConfigured() || world.isActive()) {
                level++;
                unlockedAreaAmounts.add(new UnlockedAreaAmount(level, getAreaName(world), 0));
                for (int j = 0; j < world.getDungeons().size(); j++) {
                    final Dungeon dungeon = world.getDungeons().get(j);
                    if (dungeon.isConfigured() || dungeon.isActive()) {
                        level++;
                        unlockedAreaAmounts.add(new UnlockedAreaAmount(level, getAreaName(dungeon), 0));
                    }
                }
            }
        }

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
        if (!completedMinigames.isEmpty()) {
            if (completedMinigames.get(0).getAmountOfCompletedMinigames() != 0) {
                completedMinigames.add(0, new CompletedMinigames(0, 0));
            }
        }
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
            .filter(lastPlayedStatistic -> isInRange(lastPlayedStatistic, playerStatistic.getDate()))
            .findFirst()
            .ifPresent(lastPlayedStatistic -> lastPlayedStatistic.setPlayers(lastPlayedStatistic.getPlayers() + 1));
    }

    private static boolean isInRange(final LastPlayed lastPlayed, final LocalDateTime date) {
        final long minutes = Duration.between(LocalDateTime.now(), date).toMinutes();
        if (minutes < lastPlayed.getHour() * 60L) {
            for (int i = 0; i < LAST_PLAYED_RANGE.size(); i++) {
                if (lastPlayed.getHour() == LAST_PLAYED_RANGE.get(i)) {
                    if (i == 0) {
                        return true;
                    } else return LAST_PLAYED_RANGE.get(i - 1) * 60L > minutes;
                }
            }
        } else return lastPlayed.getHour() == LAST_PLAYED_RANGE.get(LAST_PLAYED_RANGE.size() - 1);
        return false;
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
