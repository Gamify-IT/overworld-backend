package de.unistuttgart.overworldbackend.repositories;

import de.unistuttgart.overworldbackend.data.PlayerTaskStatistic;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerTaskStatisticRepository extends JpaRepository<PlayerTaskStatistic, UUID> {
    List<PlayerTaskStatistic> findByCourseId(int courseId);

    List<PlayerTaskStatistic> findByMinigameTaskId(UUID minigameTaskId);

    List<PlayerTaskStatistic> findByMinigameTaskIdOrderByHighscore(UUID minigameTaskId);
    List<PlayerTaskStatistic> findByMinigameTaskIdAndCompleted(UUID minigameTaskId, boolean completed);

    Optional<PlayerTaskStatistic> findByMinigameTaskIdAndCourseIdAndPlayerStatisticId(
        UUID minigameTaskId,
        int courseId,
        UUID playerStatisticId
    );

    List<PlayerTaskStatistic> findByPlayerStatisticId(UUID playerStatisticId);
}
