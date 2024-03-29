package de.unistuttgart.overworldbackend.repositories;

import de.unistuttgart.overworldbackend.data.MinigameTask;
import de.unistuttgart.overworldbackend.data.enums.Minigame;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MinigameTaskRepository extends JpaRepository<MinigameTask, UUID> {
    Optional<MinigameTask> findByGameAndConfigurationId(Minigame game, UUID configurationId);
}
