package de.unistuttgart.overworldbackend;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.unistuttgart.overworldbackend.data.*;
import de.unistuttgart.overworldbackend.data.mapper.*;
import de.unistuttgart.overworldbackend.repositories.LectureRepository;
import de.unistuttgart.overworldbackend.repositories.MinigameTaskRepository;
import de.unistuttgart.overworldbackend.repositories.PlayerTaskActionLogRepository;
import de.unistuttgart.overworldbackend.repositories.PlayerstatisticRepository;
import java.util.*;
import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MinigameInputControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private LectureRepository lectureRepository;

  @Autowired
  private PlayerstatisticRepository playerstatisticRepository;

  @Autowired
  private MinigameTaskRepository minigameTaskRepository;

  @Autowired
  private PlayerTaskActionLogRepository playerTaskActionLogRepository;

  @Autowired
  private LectureMapper lectureMapper;

  @Autowired
  private PlayerstatisticMapper playerstatisticMapper;

  @Autowired
  private WorldMapper worldMapper;

  @Autowired
  private DungeonMapper dungeonMapper;

  @Autowired
  private MinigameTaskMapper minigameTaskMapper;

  private final String API_URL = "/api/v1/overworld";
  private String fullURL;
  private ObjectMapper objectMapper;

  private Lecture initialLecture;
  private LectureDTO initialLectureDTO;

  private World initialWorld;
  private WorldDTO initialWorldDTO;

  private Dungeon initialDungeon;
  private DungeonDTO initialDungeonDTO;

  private Playerstatistic initialPlayerstatistic;
  private PlayerstatisticDTO initialPlayerstatisticDTO;

  private MinigameTask initialMinigameTask;
  private MinigameTaskDTO initialMinigameTaskDTO;

  @BeforeEach
  public void createBasicData() {
    lectureRepository.deleteAll();

    final MinigameTask dungeonMinigameTask1 = new MinigameTask();
    dungeonMinigameTask1.setConfigurationId(UUID.randomUUID());
    dungeonMinigameTask1.setGame("Bugfinder");
    dungeonMinigameTask1.setIndex(1);

    final MinigameTask dungeonMinigameTask2 = new MinigameTask();
    dungeonMinigameTask2.setConfigurationId(UUID.randomUUID());
    dungeonMinigameTask2.setGame("Moorhuhn");
    dungeonMinigameTask2.setIndex(2);

    final Set<MinigameTask> dungeonMinigameTasks = new HashSet<>();
    dungeonMinigameTasks.add(dungeonMinigameTask1);
    dungeonMinigameTasks.add(dungeonMinigameTask2);

    final Dungeon dungeon = new Dungeon();
    dungeon.setStaticName("Dark Dungeon");
    dungeon.setTopicName("Dark UML");
    dungeon.setActive(true);
    dungeon.setMinigameTasks(dungeonMinigameTasks);
    dungeon.setNpcs(Set.of());
    final List<Dungeon> dungeons = new ArrayList<>();
    dungeons.add(dungeon);

    final MinigameTask minigameTask = new MinigameTask();
    minigameTask.setConfigurationId(UUID.randomUUID());
    minigameTask.setGame("Bugfinder");
    minigameTask.setIndex(1);

    final Set<MinigameTask> worldMinigameTasks = new HashSet<>();
    worldMinigameTasks.add(minigameTask);

    final World world = new World();
    world.setStaticName("Winter Wonderland");
    world.setTopicName("UML Winter");
    world.setActive(true);
    world.setMinigameTasks(worldMinigameTasks);
    world.setNpcs(Set.of());
    world.setDungeons(dungeons);
    final List<World> worlds = new ArrayList<>();
    worlds.add(world);

    final Lecture lecture = new Lecture("PSE", "Basic lecture of computer science students", worlds);

    initialLecture = lectureRepository.save(lecture);
    initialLectureDTO = lectureMapper.lectureToLectureDTO(initialLecture);

    initialWorld = initialLecture.getWorlds().stream().findFirst().get();
    initialWorldDTO = worldMapper.worldToWorldDTO(initialWorld);

    initialDungeon = initialWorld.getDungeons().stream().findFirst().get();
    initialDungeonDTO = dungeonMapper.dungeonToDungeonDTO(initialDungeon);

    initialMinigameTask = initialWorld.getMinigameTasks().stream().findFirst().get();
    initialMinigameTaskDTO = minigameTaskMapper.minigameTaskToMinigameTaskDTO(initialMinigameTask);

    final Playerstatistic playerstatistic = new Playerstatistic();
    playerstatistic.setUserId("45h23o2j432");
    playerstatistic.setUsername("testUser");
    playerstatistic.setLecture(initialLecture);
    playerstatistic.setCurrentArea(initialLecture.getWorlds().stream().findFirst().get());
    playerstatistic.setKnowledge(new Random(10).nextLong());
    playerstatistic.setUnlockedAreas(new ArrayList<>());
    playerstatistic.setCompletedDungeons(new ArrayList<>());
    initialPlayerstatistic = playerstatisticRepository.save(playerstatistic);
    initialPlayerstatisticDTO = playerstatisticMapper.playerstatisticToPlayerstatisticDTO(initialPlayerstatistic);

    assertNotNull(initialLecture.getLectureName());
    assertNotNull(initialLectureDTO.getId());

    assertEquals(initialLecture.getId(), initialMinigameTask.getLecture().getId());
    assertEquals(initialLecture.getId(), initialPlayerstatistic.getLecture().getId());

    fullURL = "/internal";

    objectMapper = new ObjectMapper();
  }

  @Test
  void submitGameData() throws Exception {
    final PlayerTaskStatisticData playerTaskStatisticData = new PlayerTaskStatisticData();
    playerTaskStatisticData.setUserId(initialPlayerstatisticDTO.getUserId());
    playerTaskStatisticData.setGame(initialMinigameTask.getGame());
    playerTaskStatisticData.setConfigurationId(initialMinigameTask.getConfigurationId());
    playerTaskStatisticData.setScore(80);

    final String bodyValue = objectMapper.writeValueAsString(playerTaskStatisticData);

    final MvcResult result = mvc
      .perform(post(fullURL + "/submit-game-pass").content(bodyValue).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andReturn();

    final PlayerTaskStatisticDTO playerTaskStatisticDTO = objectMapper.readValue(
      result.getResponse().getContentAsString(),
      PlayerTaskStatisticDTO.class
    );
    assertEquals(playerTaskStatisticData.getScore(), playerTaskStatisticDTO.getHighscore());

    final Playerstatistic playerstatistic = playerstatisticRepository.findById(initialPlayerstatisticDTO.getId()).get();
    assertSame(0, playerstatistic.getCompletedDungeons().size());

    // check that action log was created
    final PlayerTaskActionLog actionLog = playerTaskActionLogRepository
      .findAll()
      .stream()
      .filter(log -> log.getPlayerTaskStatistic().getPlayerstatistic().getId().equals(initialPlayerstatistic.getId()))
      .findAny()
      .get();
    assertNotNull(actionLog);
    assertEquals(playerTaskStatisticData.getGame(), actionLog.getGame());
    assertEquals(playerTaskStatisticData.getConfigurationId(), actionLog.getConfigurationId());
    assertEquals(playerTaskStatisticData.getScore(), actionLog.getScore());
    assertEquals(
      playerTaskStatisticData.getUserId(),
      actionLog.getPlayerTaskStatistic().getPlayerstatistic().getUserId()
    );
  }

  @Test
  void submitAllMinigames_CompletesDungeon() throws Exception {
    assertSame(0, initialPlayerstatistic.getCompletedDungeons().size());
    for (MinigameTask minigameTask : initialDungeon.getMinigameTasks()) {
      final PlayerTaskStatisticData playerTaskStatisticData = new PlayerTaskStatisticData();
      playerTaskStatisticData.setUserId(initialPlayerstatisticDTO.getUserId());
      playerTaskStatisticData.setGame(minigameTask.getGame());
      playerTaskStatisticData.setConfigurationId(minigameTask.getConfigurationId());
      playerTaskStatisticData.setScore(80);

      final String bodyValue = objectMapper.writeValueAsString(playerTaskStatisticData);

      mvc
        .perform(post(fullURL + "/submit-game-pass").content(bodyValue).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    }

    final Playerstatistic playerstatistic = playerstatisticRepository.findById(initialPlayerstatisticDTO.getId()).get();
    assertSame(1, playerstatistic.getCompletedDungeons().size());
  }

  @Test
  void submitGameData_PlayerDoesNotExist_ThrowNotFound() throws Exception {
    final PlayerTaskStatisticData playerTaskStatisticData = new PlayerTaskStatisticData();
    playerTaskStatisticData.setUserId(UUID.randomUUID().toString());
    playerTaskStatisticData.setGame(initialMinigameTask.getGame());
    playerTaskStatisticData.setConfigurationId(initialMinigameTask.getConfigurationId());
    playerTaskStatisticData.setScore(80);

    final String bodyValue = objectMapper.writeValueAsString(playerTaskStatisticData);

    mvc
      .perform(post(fullURL + "/submit-game-pass").content(bodyValue).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNotFound());
  }

  @Test
  void submitGameData_MinigameDoesNotExist_ThrowNotFound() throws Exception {
    final PlayerTaskStatisticData playerTaskStatisticData = new PlayerTaskStatisticData();
    playerTaskStatisticData.setUserId(initialPlayerstatisticDTO.getUserId());
    playerTaskStatisticData.setGame(UUID.randomUUID().toString());
    playerTaskStatisticData.setConfigurationId(initialMinigameTask.getConfigurationId());
    playerTaskStatisticData.setScore(80);

    final String bodyValue = objectMapper.writeValueAsString(playerTaskStatisticData);

    mvc
      .perform(post(fullURL + "/submit-game-pass").content(bodyValue).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNotFound());
  }
}
