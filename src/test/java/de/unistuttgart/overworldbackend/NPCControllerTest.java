package de.unistuttgart.overworldbackend;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.unistuttgart.overworldbackend.data.*;
import de.unistuttgart.overworldbackend.data.mapper.DungeonMapper;
import de.unistuttgart.overworldbackend.data.mapper.NPCMapper;
import de.unistuttgart.overworldbackend.data.mapper.WorldMapper;
import de.unistuttgart.overworldbackend.repositories.CourseRepository;
import java.util.Arrays;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NPCControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private WorldMapper worldMapper;

  @Autowired
  private DungeonMapper dungeonMapper;

  @Autowired
  private NPCMapper npcMapper;

  private String fullURL;
  private String fullDungeonURL;
  private ObjectMapper objectMapper;

  private Course initialCourse;
  private World initialWorld;
  private WorldDTO initialWorldDTO;
  private Dungeon initialDungoen;
  private DungeonDTO initialDungeonDTO;

  private NPC initialNPC;
  private NPCDTO initialNPCDTO;
  private NPC initialDungeonNPC;
  private NPCDTO initialDungeonNPCDTO;

  @BeforeEach
  public void createBasicData() {
    courseRepository.deleteAll();

    final NPC npc = new NPC();
    npc.setText("You want to learn PSE?\nThis is so cool\nLet's go!");
    npc.setIndex(1);

    final NPC dungeonNPC = new NPC();
    npc.setText("You want to learn DSA?\nThis is so cool\nLet's go!");
    npc.setIndex(1);

    final Dungeon dungeon = new Dungeon();
    dungeon.setIndex(1);
    dungeon.setStaticName("Dark Dungeon");
    dungeon.setTopicName("Dark UML");
    dungeon.setActive(true);
    dungeon.setMinigameTasks(Set.of());
    dungeon.setNpcs(Set.of(dungeonNPC));

    final World world = new World();
    world.setIndex(1);
    world.setStaticName("Winter Wonderland");
    world.setTopicName("UML Winter");
    world.setActive(true);
    world.setMinigameTasks(Set.of());
    world.setNpcs(Set.of(npc));
    world.setDungeons(Arrays.asList(dungeon));

    final Course course = new Course(
      "PSE",
      "SS-22",
      "Basic lecture of computer science students",
      true,
      Arrays.asList(world)
    );
    initialCourse = courseRepository.save(course);

    initialWorld = initialCourse.getWorlds().stream().findFirst().get();
    initialWorldDTO = worldMapper.worldToWorldDTO(initialWorld);

    initialDungoen = initialWorld.getDungeons().stream().findFirst().get();
    initialDungeonDTO = dungeonMapper.dungeonToDungeonDTO(initialDungoen);

    initialNPC = initialWorld.getNpcs().stream().findFirst().get();
    initialNPCDTO = npcMapper.npcToNPCDTO(initialNPC);

    initialDungeonNPC = initialDungoen.getNpcs().stream().findFirst().get();
    initialDungeonNPCDTO = npcMapper.npcToNPCDTO(initialDungeonNPC);

    assertNotNull(initialWorld.getId());
    assertNotNull(initialWorldDTO.getId());

    assertNotNull(initialNPC.getId());
    assertNotNull(initialNPCDTO.getId());

    assertNotNull(initialDungeonNPC.getId());
    assertNotNull(initialDungeonNPCDTO.getId());

    fullURL = String.format("/courses/%d/worlds/%d/npcs", initialCourse.getId(), initialWorld.getIndex());
    fullDungeonURL =
      String.format(
        "/courses/%d/worlds/%d/dungeons/%d/npcs",
        initialCourse.getId(),
        initialWorld.getIndex(),
        initialDungoen.getIndex()
      );

    objectMapper = new ObjectMapper();
  }

  @Test
  void updateNPCFromWorld_DoesNotExist_ThrowsNotFound() throws Exception {
    final NPCDTO npcDTO = new NPCDTO();
    npcDTO.setText("Hey ho");
    final String bodyValue = objectMapper.writeValueAsString(npcDTO);
    mvc
      .perform(put(fullURL + "/" + Integer.MAX_VALUE).content(bodyValue).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNotFound())
      .andReturn();
  }

  @Test
  void updateNPCTaskFromWorld() throws Exception {
    final String newText = "New text incoming";
    final NPC updateNPCDTO = new NPC();
    updateNPCDTO.setText(newText);

    final String bodyValue = objectMapper.writeValueAsString(updateNPCDTO);

    final MvcResult result = mvc
      .perform(put(fullURL + "/" + initialNPCDTO.getIndex()).content(bodyValue).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andReturn();

    final NPCDTO updatedNPCDTOResult = objectMapper.readValue(result.getResponse().getContentAsString(), NPCDTO.class);

    assertEquals(initialNPCDTO.getId(), updatedNPCDTOResult.getId());
    assertEquals(newText, updatedNPCDTOResult.getText());
    assertEquals(initialNPCDTO.getIndex(), updatedNPCDTOResult.getIndex());
  }

  @Test
  void updateNPCFromDungeon_DoesNotExist_ThrowsNotFound() throws Exception {
    final NPCDTO npcDTO = new NPCDTO();
    npcDTO.setText("Hey ho");
    final String bodyValue = objectMapper.writeValueAsString(npcDTO);
    mvc
      .perform(put(fullDungeonURL + "/" + Integer.MAX_VALUE).content(bodyValue).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNotFound())
      .andReturn();
  }

  @Test
  void updateNPCTaskFromDungeon() throws Exception {
    final String newText = "New text incoming";
    final NPC updateNPCDTO = new NPC();
    updateNPCDTO.setText(newText);

    final String bodyValue = objectMapper.writeValueAsString(updateNPCDTO);

    final MvcResult result = mvc
      .perform(
        put(fullDungeonURL + "/" + initialDungeonNPCDTO.getIndex())
          .content(bodyValue)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andReturn();

    final NPCDTO updatedNPCDTOResult = objectMapper.readValue(result.getResponse().getContentAsString(), NPCDTO.class);

    assertEquals(initialDungeonNPCDTO.getId(), updatedNPCDTOResult.getId());
    assertEquals(newText, updatedNPCDTOResult.getText());
    assertEquals(initialDungeonNPCDTO.getIndex(), updatedNPCDTOResult.getIndex());
  }
}
