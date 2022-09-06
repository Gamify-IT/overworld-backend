package de.unistuttgart.overworldbackend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.unistuttgart.overworldbackend.data.*;
import de.unistuttgart.overworldbackend.data.mapper.CourseMapper;
import java.io.File;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.ContainerState;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@AutoConfigureMockMvc
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CloneTest {

  @Container
  public static DockerComposeContainer compose = new DockerComposeContainer(
    new File("src/test/resources/docker-compose-test.yaml")
  )
    .withExposedService("overworld-db", 5432, Wait.forListeningPort())
    .withExposedService("reverse-proxy", 80)
    .waitingFor("reverse-proxy", Wait.forHttp("/minigames/chickenshock/api/v1/configurations").forPort(80))
    .waitingFor("reverse-proxy", Wait.forHttp("/minigames/crosswordpuzzle/api/v1/configurations").forPort(80))
    .waitingFor("reverse-proxy", Wait.forHttp("/minigames/finitequiz/api/v1/configurations").forPort(80));

  @DynamicPropertySource
  public static void properties(DynamicPropertyRegistry registry) {
    //wait till setup has finished
    ContainerState state = (ContainerState) compose.getContainerByServiceName("setup_1").get();
    while (state.isRunning()) {}
    registry.add(
      "spring.datasource.url",
      () ->
        String.format(
          "jdbc:postgresql://%s:%d/postgres",
          compose.getServiceHost("overworld-db", 5432),
          compose.getServicePort("overworld-db", 5432)
        )
    );
    registry.add(
      "chickenshock.url",
      () -> String.format("http://%s/minigames/chickenshock/api/v1", compose.getServiceHost("reverse-proxy", 80))
    );
    registry.add(
      "finitequiz.url",
      () -> String.format("http://%s/minigames/finitequiz/api/v1", compose.getServiceHost("reverse-proxy", 80))
    );
    registry.add(
      "crosswordpuzzle.url",
      () -> String.format("http://%s/minigames/crosswordpuzzle/api/v1", compose.getServiceHost("reverse-proxy", 80))
    );
  }

  @Autowired
  MockMvc mvc;

  @Autowired
  private CourseMapper courseMapper;

  private String fullURL;
  private ObjectMapper objectMapper;

  @BeforeEach
  public void createBasicData() {
    fullURL = "/courses";
    objectMapper = new ObjectMapper();
  }

  @Test
  public void cloneCourseTest() throws Exception {
    final MvcResult resultGet = mvc
      .perform(get(fullURL + "/1").contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andReturn();

    final Course course = courseMapper.courseDTOToCourse(
      objectMapper.readValue(resultGet.getResponse().getContentAsString(), CourseDTO.class)
    );

    CourseInitialData initialData = new CourseInitialData();
    initialData.setCourseName("CloneCourse");
    initialData.setDescription("CloneDescription");
    initialData.setSemester("WS-23");

    final String bodyValue = objectMapper.writeValueAsString(initialData);

    final MvcResult resultClone = mvc
      .perform(post(fullURL + "/1/clone").content(bodyValue).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andReturn();
    final Course cloneCourse = courseMapper.courseDTOToCourse(
      objectMapper.readValue(resultClone.getResponse().getContentAsString(), CourseDTO.class)
    );

    for (int i = 0; i < course.getWorlds().size(); i++) {
      World world = course.getWorlds().get(i);
      World cloneWorld = cloneCourse.getWorlds().get(i);
      world
        .getMinigameTasks()
        .forEach(minigameTask -> {
          MinigameTask cloneMinigameTask = cloneWorld
            .getMinigameTasks()
            .stream()
            .filter(minigameTask1 -> minigameTask1.getIndex() == minigameTask.getIndex())
            .findAny()
            .get();
          assertEquals(minigameTask.getGame(), cloneMinigameTask.getGame());
          assertEquals(minigameTask.getDescription(), cloneMinigameTask.getDescription());
        });
      world
        .getNpcs()
        .forEach(npc -> {
          NPC cloneNpc = cloneWorld
            .getNpcs()
            .stream()
            .filter(npc1 -> npc1.getIndex() == npc.getIndex())
            .findAny()
            .get();
          AtomicInteger k = new AtomicInteger(0);
          for (String text : npc.getText()) {
            cloneNpc.getText().get(k.getAndIncrement());
          }
          assertEquals(npc.getDescription(), cloneNpc.getDescription());
        });
      for (int j = 0; j < world.getDungeons().size(); j++) {
        Dungeon dungeon = world.getDungeons().get(i);
        Dungeon cloneDungeon = cloneWorld.getDungeons().get(i);
        dungeon
          .getMinigameTasks()
          .forEach(minigameTask -> {
            MinigameTask cloneMinigameTask = cloneDungeon
              .getMinigameTasks()
              .stream()
              .filter(minigameTask1 -> minigameTask1.getIndex() == minigameTask.getIndex())
              .findAny()
              .get();
            assertEquals(minigameTask.getGame(), cloneMinigameTask.getGame());
            assertEquals(minigameTask.getDescription(), cloneMinigameTask.getDescription());
          });
        dungeon
          .getNpcs()
          .forEach(npc -> {
            NPC cloneNpc = cloneDungeon
              .getNpcs()
              .stream()
              .filter(npc1 -> npc1.getIndex() == npc.getIndex())
              .findAny()
              .get();
            AtomicInteger k = new AtomicInteger(0);
            for (String text : npc.getText()) {
              cloneNpc.getText().get(k.getAndIncrement());
            }
            assertEquals(npc.getDescription(), cloneNpc.getDescription());
          });
      }
    }
  }
}
