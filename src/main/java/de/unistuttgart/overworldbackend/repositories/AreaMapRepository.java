package de.unistuttgart.overworldbackend.repositories;

import de.unistuttgart.overworldbackend.data.AreaMap;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaMapRepository extends JpaRepository<AreaMap, UUID> {
    Optional<AreaMap> findByCourseIdAndAreaId(int courseId, UUID areaID);
}
