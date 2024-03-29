package de.unistuttgart.overworldbackend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.unistuttgart.gamifyit.authentificationvalidator.JWTValidatorService;
import de.unistuttgart.overworldbackend.data.*;
import de.unistuttgart.overworldbackend.data.mapper.CourseMapper;
import de.unistuttgart.overworldbackend.data.mapper.NPCMapper;
import de.unistuttgart.overworldbackend.data.mapper.PlayerStatisticMapper;
import de.unistuttgart.overworldbackend.repositories.CourseRepository;
import de.unistuttgart.overworldbackend.repositories.PlayerNPCActionLogRepository;
import de.unistuttgart.overworldbackend.repositories.PlayerNPCStatisticRepository;
import de.unistuttgart.overworldbackend.repositories.PlayerStatisticRepository;
import de.unistuttgart.overworldbackend.service.PlayerNPCStatisticService;
import java.util.*;
import javax.servlet.http.Cookie;
import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest
@Testcontainers
class NPCInputControllerTest {

    @Container
    public static PostgreSQLContainer postgresDB = new PostgreSQLContainer("postgres:14-alpine")
        .withDatabaseName("postgres")
        .withUsername("postgres")
        .withPassword("postgres");

    @DynamicPropertySource
    public static void properties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresDB::getJdbcUrl);
        registry.add("spring.datasource.username", postgresDB::getUsername);
        registry.add("spring.datasource.password", postgresDB::getPassword);
    }

    @Autowired
    private MockMvc mvc;

    @MockBean
    JWTValidatorService jwtValidatorService;

    final Cookie cookie = new Cookie("access_token", "testToken");

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private PlayerStatisticRepository playerstatisticRepository;

    @Autowired
    private PlayerNPCActionLogRepository playerNPCActionLogRepository;

    @Autowired
    private PlayerNPCStatisticService playerNPCStatisticService;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private PlayerStatisticMapper playerstatisticMapper;

    @Autowired
    private NPCMapper npcMapper;

    @Autowired
    private PlayerNPCStatisticRepository playerNPCStatisticRepository;

    private String fullURL;
    private String npcURL;
    private ObjectMapper objectMapper;

    private Course initialCourse;
    private CourseDTO initialCourseDTO;

    private PlayerStatistic initialPlayerStatistic;
    private PlayerStatisticDTO initialPlayerStatisticDTO;

    private NPC initialNpc;

    private NPCDTO initialNpcDTO;

    @BeforeEach
    public void createBasicData() {
        courseRepository.deleteAll();

        final Dungeon dungeon = new Dungeon();
        dungeon.setStaticName("Dark Dungeon");
        dungeon.setTopicName("Dark UML");
        dungeon.setActive(true);
        dungeon.setMinigameTasks(Set.of());
        dungeon.setNpcs(Set.of());
        dungeon.setBooks(Set.of());
        final List<Dungeon> dungeons = new ArrayList<>();

        final List<String> npcText = new ArrayList<>();
        npcText.add("NPCText");

        final NPC npc = new NPC();
        npc.setText(npcText);
        npc.setIndex(1);

        final Set<NPC> npcs = new HashSet<>();
        npcs.add(npc);

        final World world = new World();
        world.setStaticName("Winter Wonderland");
        world.setTopicName("UML Winter");
        world.setActive(true);
        world.setMinigameTasks(Set.of());
        world.setNpcs(npcs);
        world.setBooks(Set.of());
        world.setDungeons(dungeons);
        final List<World> worlds = new ArrayList<>();
        worlds.add(world);

        final Course course = new Course("PSE", "SS-22", "Basic lecture of computer science students", true, worlds);

        initialCourse = courseRepository.save(course);
        initialCourseDTO = courseMapper.courseToCourseDTO(initialCourse);

        final World initialWorld = initialCourse.getWorlds().stream().findFirst().get();
        initialNpc = initialWorld.getNpcs().stream().findFirst().get();
        initialNpcDTO = npcMapper.npcToNPCDTO(initialNpc);

        final PlayerStatistic playerstatistic = new PlayerStatistic();
        playerstatistic.setUserId("45h23o2j432");
        playerstatistic.setUsername("testUser");
        playerstatistic.setCourse(initialCourse);
        playerstatistic.setCurrentArea(initialCourse.getWorlds().stream().findFirst().get());
        playerstatistic.setKnowledge(new Random(10).nextLong());
        playerstatistic.setUnlockedAreas(new ArrayList<>());
        playerstatistic.setCompletedDungeons(new ArrayList<>());
        initialPlayerStatistic = playerstatisticRepository.save(playerstatistic);
        initialPlayerStatisticDTO = playerstatisticMapper.playerStatisticToPlayerstatisticDTO(initialPlayerStatistic);

        assertNotNull(initialCourse.getCourseName());

        assertEquals(initialCourse.getId(), initialNpc.getCourse().getId());
        assertEquals(initialCourse.getId(), initialPlayerStatistic.getCourse().getId());
        fullURL = "/internal";
        npcURL = String.format("/courses/%d/worlds/%d/npcs", initialCourse.getId(), initialWorld.getIndex());
        objectMapper = new ObjectMapper();

        doNothing().when(jwtValidatorService).validateTokenOrThrow("testToken");
        when(jwtValidatorService.extractUserId("testToken")).thenReturn("testUser");
    }

    @Test
    void submitNPCData() throws Exception {
        final PlayerNPCStatisticData playerNPCStatisticData = new PlayerNPCStatisticData();
        playerNPCStatisticData.setUserId(initialPlayerStatistic.getUserId());
        playerNPCStatisticData.setNpcId(initialNpcDTO.getId());
        playerNPCStatisticData.setCompleted(true);

        final String bodyValue = objectMapper.writeValueAsString(playerNPCStatisticData);

        final MvcResult result = mvc
            .perform(
                post(fullURL + "/submit-npc-pass")
                    .cookie(cookie)
                    .content(bodyValue)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn();

        final PlayerNPCStatisticDTO playerNPCStatisticDTO = objectMapper.readValue(
            result.getResponse().getContentAsString(),
            PlayerNPCStatisticDTO.class
        );
        assertEquals(playerNPCStatisticData.isCompleted(), playerNPCStatisticDTO.isCompleted());

        // check that action log was created
        final PlayerNPCActionLog actionLog = playerNPCActionLogRepository
            .findAll()
            .stream()
            .filter(log ->
                log.getPlayerNPCStatistic().getPlayerStatistic().getId().equals(initialPlayerStatistic.getId())
            )
            .findAny()
            .get();
        assertNotNull(actionLog);
        assertEquals(playerNPCStatisticData.getNpcId(), actionLog.getPlayerNPCStatistic().getNpc().getId());
        assertEquals(
            ReflectionTestUtils.getField(playerNPCStatisticService, "GAINED_KNOWLEDGE_PER_NPC"),
            actionLog.getGainedKnowledge()
        );
    }

    @Test
    void submitNPCDataTwice() throws Exception {
        final PlayerNPCStatisticData playerNPCStatisticData = new PlayerNPCStatisticData();
        playerNPCStatisticData.setUserId(initialPlayerStatistic.getUserId());
        playerNPCStatisticData.setNpcId(initialNpcDTO.getId());
        playerNPCStatisticData.setCompleted(false);

        final String bodyValue = objectMapper.writeValueAsString(playerNPCStatisticData);

        final MvcResult result = mvc
            .perform(
                post(fullURL + "/submit-npc-pass")
                    .cookie(cookie)
                    .content(bodyValue)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn();

        final PlayerNPCStatisticDTO playerNPCStatisticDTO = objectMapper.readValue(
            result.getResponse().getContentAsString(),
            PlayerNPCStatisticDTO.class
        );
        assertEquals(playerNPCStatisticData.isCompleted(), playerNPCStatisticDTO.isCompleted());

        // check that action log was created
        final PlayerNPCActionLog actionLog = playerNPCActionLogRepository
            .findAll()
            .stream()
            .filter(log ->
                log.getPlayerNPCStatistic().getPlayerStatistic().getId().equals(initialPlayerStatistic.getId())
            )
            .findAny()
            .get();
        assertNotNull(actionLog);
        assertEquals(playerNPCStatisticData.getNpcId(), actionLog.getPlayerNPCStatistic().getNpc().getId());
        assertEquals(
            ReflectionTestUtils.getField(playerNPCStatisticService, "GAINED_KNOWLEDGE_PER_NPC"),
            actionLog.getGainedKnowledge()
        );

        final PlayerNPCStatisticData playerNPCStatisticData2 = new PlayerNPCStatisticData();
        playerNPCStatisticData2.setUserId(initialPlayerStatistic.getUserId());
        playerNPCStatisticData2.setNpcId(initialNpcDTO.getId());
        playerNPCStatisticData2.setCompleted(true);

        final String bodyValue2 = objectMapper.writeValueAsString(playerNPCStatisticData2);

        final MvcResult result2 = mvc
            .perform(
                post(fullURL + "/submit-npc-pass")
                    .cookie(cookie)
                    .content(bodyValue2)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn();
        final PlayerNPCStatisticDTO playerNPCStatisticDTO2 = objectMapper.readValue(
            result2.getResponse().getContentAsString(),
            PlayerNPCStatisticDTO.class
        );
        assertEquals(playerNPCStatisticData2.isCompleted(), playerNPCStatisticDTO2.isCompleted());
    }

    @Test
    void submitNPCData_PlayerDoesNotExist_ThrowNotFound() throws Exception {
        final PlayerNPCStatisticData playerNPCStatisticData = new PlayerNPCStatisticData();
        playerNPCStatisticData.setUserId(UUID.randomUUID().toString());
        playerNPCStatisticData.setNpcId(initialNpc.getId());

        final String bodyValue = objectMapper.writeValueAsString(playerNPCStatisticData);

        mvc
            .perform(
                post(fullURL + "/submit-npc-pass")
                    .cookie(cookie)
                    .content(bodyValue)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void submitNPCData_NPCDoesNotExist_ThrowNotFound() throws Exception {
        final PlayerNPCStatisticData playerNPCStatisticData = new PlayerNPCStatisticData();
        playerNPCStatisticData.setUserId(initialPlayerStatistic.getUserId());
        playerNPCStatisticData.setNpcId(UUID.randomUUID());

        final String bodyValue = objectMapper.writeValueAsString(playerNPCStatisticData);

        mvc
            .perform(
                post(fullURL + "/submit-npc-pass")
                    .cookie(cookie)
                    .content(bodyValue)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void submitNPCData_ResetNPC() throws Exception {
        final PlayerNPCStatisticData playerNPCStatisticData = new PlayerNPCStatisticData();
        playerNPCStatisticData.setUserId(initialPlayerStatistic.getUserId());
        playerNPCStatisticData.setNpcId(initialNpcDTO.getId());
        playerNPCStatisticData.setCompleted(true);

        final String bodyValue = objectMapper.writeValueAsString(playerNPCStatisticData);

        final MvcResult result = mvc
            .perform(
                post(fullURL + "/submit-npc-pass")
                    .cookie(cookie)
                    .content(bodyValue)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn();

        final PlayerNPCStatisticDTO playerNPCStatisticDTO = objectMapper.readValue(
            result.getResponse().getContentAsString(),
            PlayerNPCStatisticDTO.class
        );
        assertEquals(playerNPCStatisticData.isCompleted(), playerNPCStatisticDTO.isCompleted());
        final List<String> newText = Arrays.asList("New text incoming");
        final NPC updateNPCDTO = initialNpc;
        updateNPCDTO.setText(newText);

        final String bodyValueNPC = objectMapper.writeValueAsString(updateNPCDTO);

        final MvcResult resultNPC = mvc
            .perform(
                put(npcURL + "/" + initialNpcDTO.getIndex())
                    .cookie(cookie)
                    .content(bodyValueNPC)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn();
        final PlayerNPCStatistic currentPlayerNPCStatistic = playerNPCStatisticRepository
            .findByNpcIdAndCourseIdAndPlayerStatisticId(
                initialNpc.getId(),
                initialCourse.getId(),
                initialPlayerStatistic.getId()
            )
            .get();
        assertFalse(currentPlayerNPCStatistic.isCompleted());
    }
}
