package de.unistuttgart.overworldbackend.repositories;

import de.unistuttgart.overworldbackend.data.Dungeon;
import de.unistuttgart.overworldbackend.data.MinigameTask;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MinigameTaskRepository extends JpaRepository<MinigameTask, UUID> {}