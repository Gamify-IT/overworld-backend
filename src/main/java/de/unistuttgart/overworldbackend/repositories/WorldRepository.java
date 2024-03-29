package de.unistuttgart.overworldbackend.repositories;

import de.unistuttgart.overworldbackend.data.World;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorldRepository extends JpaRepository<World, UUID> {
    Optional<World> findByIndexAndCourseId(int index, int courseId);

    Set<World> findAllByCourseId(int courseId);
}
