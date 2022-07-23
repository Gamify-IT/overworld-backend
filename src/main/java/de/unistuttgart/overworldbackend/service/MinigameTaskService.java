package de.unistuttgart.overworldbackend.service;

import de.unistuttgart.overworldbackend.data.*;
import de.unistuttgart.overworldbackend.data.mapper.MinigameTaskMapper;
import de.unistuttgart.overworldbackend.repositories.AreaBaseRepository;
import de.unistuttgart.overworldbackend.repositories.MinigameTaskRepository;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MinigameTaskService {

  @Autowired
  private MinigameTaskRepository minigameTaskRepository;

  @Autowired
  private AreaService areaService;

  @Autowired
  private MinigameTaskMapper minigameTaskMapper;

  /**
   * Get a minigame task of an area
   *
   * @throws ResponseStatusException if area or task with its id could not be found in the lecture
   * @param lectureId the id of the lecture the minigame task is part of
   * @param areaId the id of the area the task is part of
   * @param taskId the id of the task searching for
   * @return the found task object
   */
  public MinigameTask getMinigameTaskFromAreaOrThrowNotFound(
    final int lectureId,
    final UUID areaId,
    final UUID taskId
  ) {
    return areaService
      .getAreaFromLectureOrThrowNotFound(lectureId, areaId)
      .getMinigameTasks()
      .stream()
      .filter(task -> task.getId().equals(taskId))
      .findAny()
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          String.format("There is no area with id %s in lecture with id %s.", areaId, lectureId)
        )
      );
  }

  /**
   * Get a list of minigame tasks of a lecture and an area
   *
   * @throws ResponseStatusException if lecture or area with its id do not exist
   * @param lectureId the id of the lecture the minigame tasks should be part of
   * @param areaId the id of the area where the minigame tasks should be part of
   * @return a list of minigame tasks as DTO
   */
  public Set<MinigameTaskDTO> getMinigameTasksFromArea(final int lectureId, final UUID areaId) {
    final Area area = areaService.getAreaFromLectureOrThrowNotFound(lectureId, areaId);
    return minigameTaskMapper.minigameTasksToMinigameTaskDTOs(area.getMinigameTasks());
  }

  /**
   * Get a minigame task by its id from a lecture and an area
   *
   * @throws ResponseStatusException if lecture, area or task by its id do not exist
   * @param lectureId the id of the lecture the minigame task should be part of
   * @param areaId the id of the area where the minigame task should be part of
   * @param taskId the id of the minigame task searching for
   * @return the found minigame task as DTO
   */
  public MinigameTaskDTO getMinigameTaskFromArea(final int lectureId, final UUID areaId, final UUID taskId) {
    final MinigameTask minigameTask = getMinigameTaskFromAreaOrThrowNotFound(lectureId, areaId, taskId);
    return minigameTaskMapper.minigameTaskToMinigameTaskDTO(minigameTask);
  }

  /**
   * Update a minigame task by its id from a lecture and an area.
   *
   * Only the game and configuration id is updatable.
   *
   * @throws ResponseStatusException if lecture, world or dungeon by its id do not exist
   * @param lectureId the id of the lecture the minigame task should be part of
   * @param areaId the id of the area where the minigame task should be part of
   * @param taskId the id of the minigame task that should get updated
   * @param taskDTO the updated parameters
   * @return the updated area as DTO
   */
  public MinigameTaskDTO updateMinigameTaskFromArea(
    final int lectureId,
    final UUID areaId,
    final UUID taskId,
    final MinigameTaskDTO taskDTO
  ) {
    final MinigameTask minigameTask = getMinigameTaskFromAreaOrThrowNotFound(lectureId, areaId, taskId);
    minigameTask.setGame(taskDTO.getGame());
    minigameTask.setConfigurationId(taskDTO.getConfigurationId());
    final MinigameTask updatedMinigameTask = minigameTaskRepository.save(minigameTask);
    return minigameTaskMapper.minigameTaskToMinigameTaskDTO(updatedMinigameTask);
  }
}