package de.unistuttgart.overworldbackend.controller;

import static de.unistuttgart.overworldbackend.data.Roles.LECTURER_ROLE;

import de.unistuttgart.gamifyit.authentificationvalidator.JWTValidatorService;
import de.unistuttgart.overworldbackend.data.NPCDTO;
import de.unistuttgart.overworldbackend.service.NPCService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "NPC", description = "Get and update NPCs from areas")
@RestController
@Slf4j
@RequestMapping("/courses/{courseId}/worlds/{worldIndex}")
public class NPCController {

    @Autowired
    JWTValidatorService jwtValidatorService;

    @Autowired
    private NPCService npcService;

    @Operation(summary = "Update a NPC by its index in a world")
    @PutMapping("/npcs/{npcIndex}")
    public NPCDTO updateNPCFromWorld(
        @PathVariable final int courseId,
        @PathVariable final int worldIndex,
        @PathVariable final int npcIndex,
        @RequestBody final NPCDTO npcDTO,
        @CookieValue("access_token") final String accessToken
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        jwtValidatorService.hasRolesOrThrow(accessToken, LECTURER_ROLE);
        log.debug("update npc {} of world {} of course {}", npcIndex, worldIndex, courseId);
        return npcService.updateNPCFromWorld(courseId, worldIndex, npcIndex, npcDTO);
    }

    @Operation(summary = "Update a NPC by its index in a dungeon")
    @PutMapping("/dungeons/{dungeonIndex}/npcs/{npcIndex}")
    public NPCDTO updateNPCFromDungeon(
        @PathVariable final int courseId,
        @PathVariable final int worldIndex,
        @PathVariable final int dungeonIndex,
        @PathVariable final int npcIndex,
        @RequestBody final NPCDTO npcDTO,
        @CookieValue("access_token") final String accessToken
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        jwtValidatorService.hasRolesOrThrow(accessToken, LECTURER_ROLE);
        log.debug(
            "update npc {} of dungeon {} from world {} of course {} with {}",
            npcIndex,
            dungeonIndex,
            worldIndex,
            courseId,
            npcDTO
        );
        return npcService.updateNPCFromDungeon(courseId, worldIndex, dungeonIndex, npcIndex, npcDTO);
    }
}
