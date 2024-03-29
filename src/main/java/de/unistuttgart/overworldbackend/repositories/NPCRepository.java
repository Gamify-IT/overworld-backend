package de.unistuttgart.overworldbackend.repositories;

import de.unistuttgart.overworldbackend.data.NPC;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NPCRepository extends JpaRepository<NPC, UUID> {
    Optional<NPC> findByIndexAndCourseIdAndAreaId(int npcIndex, int courseId, UUID areaId);
}
