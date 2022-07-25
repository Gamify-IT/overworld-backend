package de.unistuttgart.overworldbackend.repositories;

import de.unistuttgart.overworldbackend.data.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AreaBaseRepository<T extends Area> extends JpaRepository<T, UUID> {
  Optional<Area> findByStaticNameAndLectureId(String staticName, int lectureId);
}
